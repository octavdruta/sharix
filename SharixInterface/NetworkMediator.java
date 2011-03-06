// Network interface mediator.

package SharixInterface;

import java.lang.String;

public interface NetworkMediator {
  // Initializes fname file download.
  public boolean downloadFile(String fromUser, String fname);

  // Initializes fname file upload.
  public boolean uploadFile(String toUser, String fname);

  // Aborts fname file download.
  public boolean abortDownload(String fromUser, String fname);

  // Aborts fname file upload.
  public boolean abortUpload(String toUser, String fname);
};

