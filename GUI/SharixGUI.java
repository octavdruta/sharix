// Implementation of SharixGUI module.

package GUI;

import SharixInterface.GUI;
import java.lang.Integer;
import java.lang.String;
import java.util.Vector;

public class SharixGUI implements GUI {
    public SharixGUI() { }

    public boolean addUser(String name, Vector<String> fileList) {
        return true;
    }

    public boolean removeUser(String name) {
        return true;
    }

    public boolean addFileToUser(String user, String file) {
        return true;
    }

    public boolean removeFileFromUser(String user, String file) {
        return true;
    }

    public void updateTransfer(String formUser, String toUser,
                               String file, String status, Integer progress) { }
};
