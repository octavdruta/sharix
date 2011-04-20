// Network module implementation.

package src.Network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.Mediator.SharixMediator;
import src.SharixInterface.Network;


public class SharixNetwork implements Network {
    SharixMediator mediator;
    MessageTransfer messageTransfer;
    final static int BUFFER_SIZE = 512;
    HashSet<String> requestPoll = new HashSet<String>();
    
    ExecutorService pool = Executors.newFixedThreadPool(5);
    
    public SharixNetwork(SharixMediator mediator) {
        this.mediator = mediator;
        mediator.registerNetwork(this);
        messageTransfer = new DefaultMessageTransfer(mediator);
    }

    // Initializes fname file download.
    public boolean downloadFile(String fromUser, String fname) {
    	String task = fromUser + fname;
    	if (requestPoll.contains(task)) {
    		return false;    		
    	}
    	requestPoll.add(task);
    	System.out.println("Request to download " + fname + " from user " + fromUser);
    	try {
    		messageTransfer.send(fromUser, MessageProcessor.requestMessage(mediator.getMyUsername(), fname));
    	} catch (IOException e) {
			System.out.println("Error: Sending request for file " + fname);
			return false;
		}
        return true;
    }

    // Initializes fname file upload.
    public boolean uploadFile(final String toUser, final String fname) {
    	System.out.println("Starting thread to upload file " + fname + " to " + toUser);
    	pool.execute(new Runnable() {
			public void run() {
				try {
					String content = readFileAsString(fname);
					ByteBuffer buffer = MessageProcessor.initialMessage(mediator.getMyUsername(), fname, content.length());
					messageTransfer.send(toUser, buffer);
					int pos = 0;
					int end = 0;
					while (pos < content.length()) {
						if (pos + BUFFER_SIZE <= content.length()) {
							end = pos + BUFFER_SIZE;
						} else {
							end = content.length();
						}
						String chunk = content.substring(pos, end);
						System.out.println("Sending to " + toUser + " file chunk " + pos);
						pos = end;
						buffer = MessageProcessor.middleMessage(mediator.getMyUsername(), fname, chunk);
						messageTransfer.send(toUser, buffer);
					}
					buffer = MessageProcessor.finalMessage(mediator.getMyUsername(), fname);
					messageTransfer.send(toUser, buffer);
					// TODO: Update transfer
				} catch (IOException e) {
					System.out.println("Error: Upload File failed.");
				}
			}
    	});
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
    
    private String readFileAsString(String filePath) {
    	filePath = "config/" + mediator.getMyUsername() + "/" + filePath; 
    	try {
    		byte[] buffer = new byte[(int) new File(filePath).length()];
    		FileInputStream f = new FileInputStream(filePath);
    		f.read(buffer);
    		f.close();
   	        return new String(buffer);
    	} catch (IOException e) {
    		System.out.println("Error: Could not read file.");
    		System.exit(-1);
    	}
        return null;
    }


    
};
