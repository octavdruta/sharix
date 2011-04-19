// Network module implementation.

package src.Network;

import java.util.HashMap;

import src.SharixInterface.*;
import src.Mediator.*;
import java.util.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ConnectionData {
	SocketChannel socketChannel;
	Vector<String> uploadFiles;
	Vector<String> downloadFiles;
};

public class SharixNetwork implements Network {
    HashMap<String, ConnectionData> connectedUsers;
    SharixMediator mediator;
    String myName;
	Selector selector;
	ServerSocketChannel serverSocketChannel;
	static ExecutorService pool = Executors.newFixedThreadPool(5);
	static final int BUFFER_SIZE = 1000;

    public SharixNetwork(SharixMediator mediator) {
        this.mediator = mediator;
        connectedUsers = new HashMap<String, ConnectionData>();
        
        // TODO: Modify constructor to specify the username for the client.
        this.myName = "vlad";
        initServer();
    }
        
    // Thread running selector mechanism.
    public void selectorThread() {
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
    
    // This method is called when new connection is required.
    public void accept(final SelectionKey key) throws IOException {
		System.out.println("ACCEPT: ");
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept(); 
		socketChannel.configureBlocking(false);
		socketChannel.register(key.selector(), SelectionKey.OP_READ);
		// Connection is accepted, but it is not yet registered as ConnectionData.
		// We need to wait for the name identification package and then register it.
		// ParseReceivedBuffer is responsible for registering this to ConnectionData.
    }
    
    // This method is called when data is available on a certain channel.
    public void read(final SelectionKey key) throws IOException {
		pool.execute(new Runnable() {
			public void run() {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
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
			}
		});
    }

    // Sends bytebuffer data to user.
    public boolean send(String username, ByteBuffer buffer) throws IOException {
    	ConnectionData conn = connectedUsers.get(username);
    	if (conn == null)
    		return false;
    	
    	// TODO: What happens if socket refuses more data ?
    	while (buffer.hasRemaining()) {
    		conn.socketChannel.write(buffer);
    	}
    	return true;    	
    }
    
    // Initializes server and polling mechanism.
    private boolean initServer() {
    	User user = findUser(myName);
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
        	if (name.equals(u.getName()))
        		return u;
        }
        return null;
    }

    // Connects to user identified by username.
    public boolean connectToUser(String name) {
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
    public boolean isUserConnected(String name) {
        return connectedUsers.containsKey(name);
    }

    // Disconnects from user identified by username.
    public boolean disconnectFromUser(String name) {
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

    // Initializes fname file download.
    public boolean downloadFile(String fromUser, String fname) {
        // initiates download and reports progress via mediator.updateTransfer()
        return true;
    }

    // Initializes fname file upload.
    public boolean uploadFile(String toUser, String fname) {
        // initiates upload and reports progress via mediator.updateTransfer()
        return true;
    }

    // Aborts fname file download.
    public boolean abortDownload(String fromUser, String fname) {
        return true;
    }

    // Aborts fname file upload.
    public boolean abortUpload(String toUser, String fname) {
        return true;
    }
};
