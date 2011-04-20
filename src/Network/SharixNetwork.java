// Network module implementation.

package src.Network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import src.Mediator.SharixMediator;
import src.SharixInterface.Network;


public class SharixNetwork implements Network {
    SharixMediator mediator;
    MessageTransfer messageTransfer;
    final static int BUFFER_SIZE = 100;
    HashSet<String> requestPoll = new HashSet<String>();
    static Logger log = Logger.getLogger("SharixNetwork ");
    ExecutorService pool = Executors.newFixedThreadPool(5);
    
    // Constructor.
    public SharixNetwork(SharixMediator mediator) {
        this.mediator = mediator;
        mediator.registerNetwork(this);
        messageTransfer = new MessageTransfer(mediator);
    }

    // Initializes fname file download.
    public boolean downloadFile(String fromUser, String fname) {
    	String task = fromUser + fname;
    	if (requestPoll.contains(task)) {
    		return false;    		
    	}
    	requestPoll.add(task);
    	log.info("Request to download " + fname + " from user " + fromUser);
    	try {
    		messageTransfer.send(fromUser, MessageProcessor.requestMessage(
				mediator.getMyUsername(), fname));
    	} catch (IOException e) {
			log.error("Could not send request for file " + fname);
			return false;
		}
        return true;
    }

    // Initializes fname file upload.
    public boolean uploadFile(final String toUser, final String fname) {
    	log.info("Starting thread to upload file " + fname + " to " + toUser);
    	pool.execute(new Runnable() {
			public void run() {
				try {
					byte[] content = readFileAsString(fname);

					ByteBuffer buffer = MessageProcessor.initialMessage(
						mediator.getMyUsername(), fname, content.length);

					messageTransfer.send(toUser, buffer);
					int pos = 0;
					int end = 0;
					while (pos < content.length) {
						if (pos + BUFFER_SIZE <= content.length) {
							end = pos + BUFFER_SIZE;
						} else {
							end = content.length;
						}
						byte[] chunk = new byte[end - pos];
						for (int i = pos; i < end; ++i) {
							chunk[i - pos] = content[i];
						}
						log.info("Sending to " + toUser + " file chunk " + pos);
						pos = end;

						buffer = MessageProcessor.middleMessage(
							mediator.getMyUsername(), fname, chunk);

						messageTransfer.send(toUser, buffer);
					}

					buffer = MessageProcessor.finalMessage(
						mediator.getMyUsername(), fname);

					messageTransfer.send(toUser, buffer);
				} catch (IOException e) {
					log.error("Upload file failed.");
					e.printStackTrace();
				}
			}
    	});
    	return true;
    }

   // Read file content as byte stream.
    private byte[] readFileAsString(String filePath) {
    	filePath = "config/" + mediator.getMyUsername() + "/" + filePath; 
    	try {
    		byte[] buffer = new byte[(int) new File(filePath).length()];
    		FileInputStream f = new FileInputStream(filePath);
    		f.read(buffer);
    		f.close();
   	        return buffer;
    	} catch (IOException e) {
    		log.error("Could not read file.");
    		System.exit(-1);
    	}
        return null;
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
