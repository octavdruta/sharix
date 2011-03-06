// Network module interface.

public interface Network {
  // Connects to user identified by username.
  public boolean connectToUser(String name);

  // Checks if user is connected.
  public boolean isUserConnected(String name);

  // Disconnects from user identified by username.
  public boolean disconnectFromUser(String name);

  // Initializes fname file download.
  public boolean downloadFile(String fromUser, String fname);

  // Initializes fname file upload.
  public boolean uploadFile(String toUser, String fname);

  // Aborts fname file download.
  public boolean abortDownload(String fromUser, String fname);

  // Aborts fname file upload.
  public boolean abortUpload(String toUser, String fname);
};
