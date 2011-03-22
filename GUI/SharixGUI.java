// Implementation of SharixGUI module.

package GUI;

import SharixInterface.GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.lang.Integer;
import java.lang.String;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class SharixGUI extends JPanel implements GUI  {
    private static final long serialVersionUID = 1L;
    private TreeMap<String, Vector<String>> users = new TreeMap<String, Vector<String>>();;
    private DefaultListModel usersModel = new DefaultListModel();
    private DefaultListModel filesModel = new DefaultListModel();
    private JPanel filesPanel;
    private JPanel usersPanel;
    private JPanel activityPanel;
    private JList userList;
    private JList fileList;

    public SharixGUI() {
        init();
    }
    
    private void init() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gridbag);
        
        
        fileList = new JList(filesModel);
        userList = new JList(usersModel);
        
        filesPanel = new JPanel();
        usersPanel = new JPanel();
        activityPanel = new TrafficActivity();
        activityPanel.setOpaque(true);
        
        filesPanel.add(new JScrollPane(fileList));
        usersPanel.add(new JScrollPane(userList));
        
        JPanel leftSide = new JPanel(new GridLayout(2, 1, 0, 50));
        leftSide.add(filesPanel);
        leftSide.add(activityPanel);
       
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 4.0;
        gridbag.setConstraints(leftSide, c);
        this.add(leftSide);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(usersPanel, c);
        this.add(usersPanel);
    }

    public boolean addUser(String name, Vector<String> fileList) {
        users.put(name, fileList);
        return true;
    }

    public boolean removeUser(String name) {
        users.remove(name);
        return true;
    }

    public boolean addFileToUser(String user, String file) {
        users.get(user).add(file);
        return true;
    }

    public boolean removeFileFromUser(String user, String file) {
        users.get(user).remove(file);
        return true;
    }

    public void updateTransfer(String formUser, String toUser,
                               String file, String status, Integer progress) { }
    
    private static void buildGUI() {
        JFrame frame = new JFrame("Sharix");
        frame.setContentPane(new SharixGUI());
        frame.setSize(800, 600);
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                buildGUI();
            }
        });
    }
};

