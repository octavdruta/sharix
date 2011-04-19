package src.Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.Mediator.SharixMediator;
import src.SharixInterface.User;


class ConnectionData {
	SocketChannel socketChannel;
}

public class DefaultMessageTransfer implements MessageTransfer{
	Selector selector;
	ServerSocketChannel serverSocketChannel;
    HashMap<String, ConnectionData> connectedUsers;
    SharixMediator mediator;
    
	static ExecutorService pool = Executors.newFixedThreadPool(5);
	
	// Constructor.
	public DefaultMessageTransfer(SharixMediator mediator) {
		this.mediator = mediator;
		connectedUsers = new HashMap<String, ConnectionData>();
		if (!initServer()) {
			System.out.println("Error: Could not initialize server.");
		}
	}
	
	// Initializes server and polling mechanism.
    private boolean initServer() {
    	User user = findUser(mediator.getMyUsername());
    	if (user == null)
    		return false;
    	    	  	
    	try {
    		selector = Selector.open();
    		serverSocketChannel = ServerSocketChannel.open();
    		serverSocketChannel.configureBlocking(false);
    		InetSocketAddress address = new InetSocketAddress(user.getAddress(),
    														  user.getPort());
    		serverSocketChannel.socket().bind(address);
    		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    		selectorThread();
    	} catch (IOException e) {
    		System.out.println("Error: Could not initialize server.");
    		return false;
    	}
		return true;
    }
    
    // Returns user data for a given user name.
    private User findUser(String name) {
        Vector<User> users = mediator.getUserList();
        for (User u : users) {
        	if (name.equals(u.getName())) {
        		return u;
        	}
        }
        return null;
    }
    
    // Returns username for a certain <address, port> pair.
    private String getUsername(String address, int port) {
    	Vector<User> users = mediator.getUserList();
        for (User u : users) {
        	if (address.equals(u.getAddress()) && port == u.getPort()) {
        		return u.getName();
        	}
        }
        return null;    	
    }
    
    // Connects to user identified by username.
    private boolean connectToUser(String name) {
    	if (isUserConnected(name))
    		return true;
    	
        User user = findUser(name);
        if (user == null)
        	return false;
        
        ConnectionData conn = new ConnectionData();
        InetSocketAddress address = new InetSocketAddress(user.getAddress(),
        												  user.getPort());
        try {
        	conn.socketChannel = SocketChannel.open(address);
        } catch (IOException e) {
        	System.out.println("Error: Could not connect to user: " + name);
        	return false;
        }
        connectedUsers.put(name, conn);
     
        return true;
    }

    // Checks if user is connected.
    private boolean isUserConnected(String name) {
        return connectedUsers.containsKey(name);
    }

    // Disconnects from user identified by username.
    private boolean disconnectFromUser(String name) {
        ConnectionData conn = connectedUsers.get(name);
        if (conn == null)
        	return true;
        try {
        	conn.socketChannel.close();
        } catch (IOException e) {
        	System.out.println("Error: Could not close channel to user: " + name);
        	return false;
        }
        connectedUsers.remove(name);
        return true;
    }
        
    // Thread running selector mechanism.
    private void selectorThread() {
    	pool.execute(new Runnable() {
    		public void run() {
    			try {
    				while (true) {
    					selector.select();
    					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
    					while (it.hasNext()) {
    						SelectionKey key = it.next();
    						it.remove();
    						if (key.isAcceptable())
    							accept(key);
    						else if (key.isReadable())
    							read(key);
    					}
    				}
    			} catch (IOException e) {
    				
    			} finally {
        			// TODO - cleanup selector.
        		}
    		};
    	});
    }
    
    // Transfers buffer data to a given user via channels.
    public boolean send(String username, ByteBuffer buffer) throws IOException {
    	ConnectionData conn = connectedUsers.get(username);
    	if (conn == null) {
    		if (!connectToUser(username))
    			return false;
    		conn = connectedUsers.get(username);
    	}
        	
    	while (buffer.hasRemaining()) {
    		if (conn.socketChannel.write(buffer) <= 0) {
    			System.out.println("Error: Protocol lost consistency while writing data.");
    			return false;
    		}
    	}
    	return true;    	
    }
    
    // This method is called when new connection is required.
    private void accept(final SelectionKey key) throws IOException {
		System.out.println("ACCEPT: ");
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept(); 
		socketChannel.configureBlocking(false);
		socketChannel.register(key.selector(), SelectionKey.OP_READ);
		
		String address = socketChannel.socket().getInetAddress().getHostAddress();
		int port = socketChannel.socket().getPort();
		
		String username = getUsername(address, port);
		if (username == null) {
			System.out.println("Error: Accept failed. Username not found.");
			return;
		}
		
		ConnectionData conn = new ConnectionData();
		conn.socketChannel = socketChannel;
		connectedUsers.put(username, conn);
		System.out.println("INFO: Accepted connection with user: " + username);
    }
    
    // This method is called when data is available on a certain channel.
    private void read(final SelectionKey key) throws IOException {
		pool.execute(new Runnable() {
			public void run() {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				
				String address = socketChannel.socket().getInetAddress().getHostAddress();
				int port = socketChannel.socket().getPort();
				String username = getUsername(address, port);
				
				ByteBuffer buffer = ByteBuffer.allocate(MessageProcessor.BUFFER_SIZE);
				buffer.clear();

				try {
					while (buffer.hasRemaining()) {
						if (socketChannel.read(buffer) <= 0) {
							System.out.println("ERROR: Protocol lost consistency.");
						}
					}					
				} catch (IOException e) {
					System.out.print("Error: Could not read data from channel.");
					e.printStackTrace();
				}
				// TODO: call method to process buffers.
				// parseReceivedBuffer(username, buffer, key)  - this must be thread safe - async calls
				// We need a way to decide when a certain connection must be removed from selector.
				parseReceivedBuffer(username, buffer);
			}
		});
    }
    
    private void parseReceivedBuffer(String username, ByteBuffer buffer) {
    	// if request to upload file
    	// if initial download message. => open file
    	// if middle message => append file
    	// if final message => close file
    
    	
    }
    
    
}
