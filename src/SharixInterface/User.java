// User Interface.

package src.SharixInterface;

import java.lang.Integer;
import java.lang.String;
import java.util.Vector;

public interface User {
  // Gets username.
  public String getName();

  // Gets user's address.
  public String getAddress();

  // Gets user's port.
  public Integer getPort();

  // Gets list of shared files.
  public Vector<String> getFileList();
};
