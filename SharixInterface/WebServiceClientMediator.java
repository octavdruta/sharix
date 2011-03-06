// WebServiceClient interface mediator.

package SharixInterface;

import java.lang.Integer
import java.lang.String;
import java.util.Vector;

public interface WebServiceClientMediator {
  // Returns the list with all the users currently connected to the server.
  public Vector<User> getUserList();
};
