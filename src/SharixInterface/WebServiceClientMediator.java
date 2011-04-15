// WebServiceClient interface mediator.

package src.SharixInterface;

import java.util.Vector;

public interface WebServiceClientMediator {
  // Returns the list with all the users currently connected to the server.
  public Vector<User> getUserList();
};
