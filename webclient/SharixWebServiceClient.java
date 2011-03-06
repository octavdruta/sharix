// WebServiceClient module implementation.

class SharixWebServiceClient implementes WebServiceClient {
  // Connects to filesharing server identified by address and port.
  public boolean connectToServer(String address, Integer port) {
    return true;
  }

  // Checks if client is connected to filesharing server.
  public boolean isServerConnected() {
    return true;
  }

  // Returns the list with all the users currently connected to the server.
  public Vector<User> getUserList() {
    return null;
  }
};
