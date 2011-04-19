// Network module implementation.

package src.Network;

import src.Mediator.SharixMediator;
import src.SharixInterface.Network;


public class SharixNetwork implements Network {
    SharixMediator mediator;
    DefaultMessageTransfer messageTransfer;
    
    public SharixNetwork(SharixMediator mediator) {
        this.mediator = mediator;
        messageTransfer = new DefaultMessageTransfer(mediator);
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
