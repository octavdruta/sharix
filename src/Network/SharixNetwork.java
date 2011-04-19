// Network module implementation.

package src.Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.Mediator.SharixMediator;
import src.SharixInterface.Network;
import src.SharixInterface.User;

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
    
    // This method is called when data is available on a certain channel.
    public static void read(final SelectionKey key) throws IOException {
		key.interestOps(0);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.clear();
		pool.execute(new Runnable() {
			public void run() {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				int size = 0;
				try {
					while ((size = socketChannel.read(buf)) > 0) {
					}
				} catch (IOException e) {
					System.out.print("Error: Could not read data from channel.");
					e.printStackTrace();
				}
				if (size == -1) {
					System.out.println("Error: Channel is dead.");
				}
				// TODO: Check if buffer is full, but there is still data to come.
				// TODO: call method to process buffers.
				// parseReceivedBuffer(username, buffer)  - this must be thread safe - async calls 
			}
		});
    }

    // Sends bytebuffer data to user.
    public void send(String username, ByteBuffer buffer) {
    	
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
        // disconnect socket connectedUsers.get(name)
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
