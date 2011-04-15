// WebServiceClient Interface.

package src.SharixInterface;

import java.lang.Integer;
import java.lang.String;
import java.util.Vector;

public interface WebServiceClient {
  // Connects to filesharing server identified by address and port.
  public boolean connectToServer(String address, Integer port);

  // Checks if client is connected to filesharing server.
  public boolean isServerConnected();

  // Returns the list with all the users currently connected to the server.
  public Vector<User> getUserList();
};
