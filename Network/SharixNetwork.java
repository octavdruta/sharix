// Network module implementation.

import java.util.HashMap;
import SharixInterface.Network;

class Socket {
	int phony;
};

public class SharixNetwork implements Network {
  HashMap<String, Socket> connectedUsers;
  SharixMediator mediator;

  public SharixNetwork(SharixMediator mediator) {
    this.mediator = mediator;
    connectedUsers = new HashMap<String, Address>();
  }

  private User findUser(String name) {
    Vector<User> users = mediator.getUserList();
    for (User u : users) {
      if (u.name == name) {
        return u;
      }
    }
    return null;
  }

  // Connects to user identified by username.
  public boolean connectToUser(String name) {
    User user = findUser(name);
    // connect to user.getAddress(), user.getPort() and get socket
    connectedUsers.put(name, socket);
    return true;
  }

  // Checks if user is connected.
  public boolean isUserConnected(String name) {
    return connectedUsers.containsKey(name);
  }

  // Disconnects from user identified by username.
  public boolean disconnectFromUser(String name) {
    // disconnect socket connectedUsers.get(name)
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
