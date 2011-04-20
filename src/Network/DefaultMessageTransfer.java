package src.Network;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import src.Sharix;
import src.Mediator.SharixMediator;
import src.SharixInterface.User;

// Stores information about files currently involved in transfers.
class FileData {
	DataOutputStream stream;
	int currentSize;
	int totalSize;
}

// Stores information about currently opened connections.
class ConnectionData {
	SocketChannel socketChannel;
	HashMap<String, FileData> download = new HashMap<String, FileData>();
}

public class DefaultMessageTransfer implements MessageTransfer{
	Selector selector;
	ServerSocketChannel serverSocketChannel;
    HashMap<String, ConnectionData> connectedUsers;
    SharixMediator mediator;
    HashSet<String> uploadTasks = new HashSet<String>();
    ExecutorService pool = Executors.newFixedThreadPool(5);
    Logger log = Logger.getLogger("DefaultMessageTransfer");

	// Constructor.
	public DefaultMessageTransfer(SharixMediator mediator) {
		this.mediator = mediator;
		connectedUsers = new HashMap<String, ConnectionData>();
		if (!initServer()) {
			log.error("Could not initialize server");
			System.exit(-1);
		}
	}

	// Initializes server and polling mechanism.
    private boolean initServer() {
    	User user = findUser(mediator.getMyUsername());
    	if (user == null)
    		return false;
    	
    	wakeUpThread();
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
    		log.error("Could not initialize server.");
    		return false;
    	}
    	log.info("Server succesfully initialized.");
		return true;
    }

    // Returns user data for a given user name.
    private User findUser(String name) {
        Vector<User> users = mediator.getUserList();
        for (User u : users) {
        	if (name.equals(u.getName())) return u;
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
        	conn.socketChannel.configureBlocking(false);

        	ByteBuffer buffer =
				MessageProcessor.requestMessage(mediator.getMyUsername(), "#");

        	conn.socketChannel.write(buffer);
        	log.info("Established connection with user " + name);
         } catch (IOException e) {
        	log.error("Could not connect to user: " + name);
        	return false;
        }
        connectedUsers.put(name, conn);
       
        try {
			conn.socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			log.error("Could not register channel to selector.");
			e.printStackTrace();
			System.exit(-1);
		}
        return true;
    }

    // Checks if user is connected.
    private boolean isUserConnected(String name) {
        return connectedUsers.containsKey(name);
    }
        
    // Thread running selector mechanism.
    private void selectorThread() {
    	pool.execute(new Runnable() {
    		public void run() {
    			try {
    				while (true) {
    					selector.select();

    					Iterator<SelectionKey> it =
							selector.selectedKeys().iterator();

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
    				try {
						selector.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
    		};
    	});
    }
    
    // Transfers buffer data to a given user via channels.
    public synchronized boolean send(String username, ByteBuffer buffer)
								 	throws IOException {
    	ConnectionData conn = connectedUsers.get(username);
    	if (conn == null) {
    		if (!connectToUser(username)) {
    			return false;    			
    		}
    		conn = connectedUsers.get(username);
    	}
    	log.info("Sending message type " +
			MessageProcessor.getMessageType(buffer) + " to user " + username);

    	while (buffer.hasRemaining()) {
    		if (conn.socketChannel.write(buffer) < 0) {
    			log.error("Protocol may have lost consistency.");
    			return false;
    		}
    	}
    	return true;    	
    }
    
    // This method is called when new connection is required.
    private void accept(final SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel =
			(ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept(); 
		socketChannel.configureBlocking(false);
		
		ByteBuffer buffer = ByteBuffer.allocate(MessageProcessor.BUFFER_SIZE);
		buffer.clear();

		try {
			while (buffer.hasRemaining()) {
				int size;
				if ((size = socketChannel.read(buffer)) < 0) {
					log.error("Protocol have lost consistency. Fatal!.");
					System.exit(-1);
				}
			}					
		} catch (IOException e) {
			log.error("Accept: Could not read data from channel.");
			e.printStackTrace();
		}
		buffer.flip();
		String username = MessageProcessor.getUsername(buffer);
		log.info("Accepted connection from: " + username);
		
		ConnectionData conn = new ConnectionData();
		conn.socketChannel = socketChannel;
		connectedUsers.put(username, conn);
		
		socketChannel.register(key.selector(), SelectionKey.OP_READ);
    }
    
    // This method is called when data is available on a certain channel.
    private void read(final SelectionKey key) throws IOException {
    	key.interestOps(0);
    	
		pool.execute(new Runnable() {
			public void run() {

				SocketChannel socketChannel = (SocketChannel) key.channel();
				ByteBuffer buffer =
					ByteBuffer.allocate(MessageProcessor.BUFFER_SIZE);
				buffer.clear();

				try {
					while (buffer.hasRemaining()) {
						
						if (socketChannel.read(buffer) <= 0) {
							log.error("Protocol have lost consistency. Fatal!");
							System.exit(-1);
						}
					}					
				} catch (IOException e) {
					log.error("Read: Could not read data from channel.");
					e.printStackTrace();
				}

				parseReceivedBuffer(buffer);
				key.interestOps(SelectionKey.OP_READ);
				key.selector().wakeup();
			}
		});
    }
   
    // Parses received byte buffer and takes identified actions.
    private synchronized void parseReceivedBuffer(ByteBuffer buffer) {
		buffer.flip();
		String username = MessageProcessor.getUsername(buffer);
    	int type = MessageProcessor.getMessageType(buffer);
    	String fname = MessageProcessor.getFilename(buffer);
    	ConnectionData conn;
    	DataOutputStream output;
    	FileData fdata;
    	byte[] chunk;
    	
    	switch (type) {
    		case MessageProcessor.REQUEST:
    			log.info("User " + username + " requested file " + fname);
 				String task = username + fname;
    			if (!uploadTasks.contains(task)) {
    				mediator.uploadFile(username, fname);
    				uploadTasks.add(task);
    			}
    			break;
    		
    		case MessageProcessor.INITIAL:
    			try {
    				log.info("Receiving initial package for " + fname);
    				output = new DataOutputStream(new FileOutputStream(fname));
    				fdata = new FileData();
    				fdata.stream = output;
    				fdata.totalSize = MessageProcessor.getFileLength(buffer);
    				fdata.currentSize = 0;
    				conn = connectedUsers.get(username);
        			conn.download.put(fname, fdata);
    			} catch (IOException e) {
    				log.error("Could not create output file.");
    			}
    			break;
    			
    		case MessageProcessor.MIDDLE:
    			log.info("Received chunk package for " + fname);
    			conn = connectedUsers.get(username);
    			chunk = MessageProcessor.getByteChunk(buffer);
    			fdata = conn.download.get(fname);
    			fdata.currentSize += chunk.length;

    			int progress = 100 - 100 *
					(fdata.totalSize - fdata.currentSize) / fdata.totalSize;

    			try {
    				fdata.stream.write(chunk);

    				mediator.updateTransfer(username, mediator.getMyUsername(),
						fname, "Receiving", progress);

    			} catch (IOException e) {
    				System.out.println("Error: Could not append chunk to file");
    			}
    			break;
    		
    		case MessageProcessor.FINAL:
    			log.info("Received final package for " + fname);
    			conn = connectedUsers.get(username);
    			fdata = conn.download.get(fname);
    			try {
    				fdata.stream.close();
    				conn.download.remove(fname);

    				mediator.updateTransfer(username, mediator.getMyUsername(),
						fname, "Completed", 100);

    			} catch (IOException e) {
    				log.error("Could not close file " + fname);
    			}
    			break;
    	}

    }
    
	// Thread sending wake-up requests to selector.
    private void wakeUpThread() {
    	pool.execute(new Runnable() {
    		public void run() {
    			while (true) {
    				try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    				selector.wakeup();
    			}
    		}
    	});
    }
    
}
