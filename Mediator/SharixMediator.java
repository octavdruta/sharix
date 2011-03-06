// SharixMediator module implementation.

class SharixMediator implements SharixNetwork, SharixGUI, SharixWebServiceClient {
  Network network;
  GUI gui;
  WebServiceClient webServiceClient;

  // Registeres network component to mediator.
  public void registerNetwork(SharixNetwork network) {
    this.network = network;
  }

  // Registers GUI component to mediator.
  public void registerGUI(SharixGUI gui) {
    this.gui = gui;
  }

  // Registers WebServiceClient to mediator.
  public void registerWebServiceClient(SharixWebServiceClient client) {
    webServiceClient = client;
  }

  // Adds a new user and its corresponding list of shared files.
  public boolean addUser(String name, Vector<String> fileList) {
    return true;
  }

  // Removes user.
  public boolean removeUser(String name) {
    return true;
  }

  // Adds a new shared file to the user's list.
  public boolean addFileToUser(String user, String file) {
    return true;
  }

  // Removes a shared file from user's list.
  public boolean removeFileFromUser(String user, String file) {
    return true;
  }

  // Updates file transfer status.
  public void updateTransfer(String fromUser, String toUser, String file,
                             String status, Integer progress) { }

  // Returns the list with all the users currently connected to the server.
  public Vector<User> getUserList() {
    return new Vector<User>();
  }

  // Initializes fname file download.
  public boolean downloadFile(String fromUser, String fname) {
    if (!network.connectedToUSer(user))
      network.connect(fromUser);
  }

  // Initializes fname file upload.
  public boolean uploadFile(String toUser, String fname) {
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
