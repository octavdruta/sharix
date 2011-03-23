// Implementation of SharixGUI module.

package GUI;

import SharixInterface.GUI;

import java.awt.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class SharixGUI extends JPanel implements GUI  {
    private static final long serialVersionUID = 1L;
    private TreeMap<String, Vector<String>> users =
        new TreeMap<String, Vector<String>>();;
    private DefaultListModel usersModel = new DefaultListModel();
    private DefaultListModel filesModel = new DefaultListModel();
    private JScrollPane filesPanel;
    private JScrollPane usersPanel;
    private TrafficActivity activityPanel;
    private JList userList;
    private JList fileList;
    final int VERTICAL_SPLIT_POINT = 400;
    final int HORIZONTAL_SPLIT_POINT = 600;
    private String selectedUser;
    private String myself;

    public SharixGUI(String myName) {
        this.myself = myName;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        fileList = new JList(filesModel);
        fileList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String s = (String)fileList.getSelectedValue();
                if (s != null) {
                    updateTransfer(selectedUser, myself, s, "Receiving...",
                                   new Integer(0));
                }
            }
        });

        userList = new JList(usersModel);
        userList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String s = (String)userList.getSelectedValue();
                if (s != null) {
                    selectedUser = s;
                    filesModel.clear();
                    for (String file : users.get(selectedUser)) {
                        filesModel.addElement(file);
                    }
                }
            }
        });

        filesPanel = new JScrollPane();
        filesPanel.setViewportView(fileList);

        usersPanel = new JScrollPane();
        usersPanel.setViewportView(userList);

        activityPanel = new TrafficActivity();
        activityPanel.setOpaque(true);

        JSplitPane leftPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              filesPanel, activityPanel);
        leftPanel.setDividerLocation(VERTICAL_SPLIT_POINT);
        JSplitPane fullPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              leftPanel, usersPanel);
        fullPanel.setDividerLocation(HORIZONTAL_SPLIT_POINT);
        add(fullPanel, BorderLayout.CENTER);
    }

    public boolean addUser(String name, Vector<String> fileList) {
        users.put(name, fileList);
        usersModel.addElement(name);
        return true;
    }

    public boolean removeUser(String name) {
        users.remove(name);
        usersModel.removeElement(name);
        if (name == selectedUser) {
            filesModel.clear();
        }
		activityPanel.abortTrafficActivity(name);
        return true;
    }

    public boolean addFileToUser(String user, String file) {
        users.get(user).add(file);
        if (user == selectedUser) {
            filesModel.addElement(file);
        }
        return true;
    }

    public boolean removeFileFromUser(String user, String file) {
        users.get(user).remove(file);
        if (user == selectedUser) {
            filesModel.removeElement(file);
        }
        return true;
    }

    public void updateTransfer(String fromUser, String toUser,
                               String file, String status, Integer progress) {
        activityPanel.updateTrafficActivity(fromUser, toUser, file, status, progress);
    }

    public void randomUpdateTransfers() {
        SharixTableModel tableModel = activityPanel.getActivities();
        Random rnd = new Random();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String src = tableModel.getSource(i);
            String dst = tableModel.getDestination(i);
            String file = tableModel.getFilename(i);
            String status = tableModel.getStatus(i);
            int progress = tableModel.getProgress(i);
            int val = rnd.nextInt(10);
            progress += val % (101 - progress);
            if (progress == 100) {
                status = "Completed.";
            }
			if (!status.equals("Aborted") && !status.equals("Completed.")) {
	            updateTransfer(src, dst, file, status, progress);
			}
        }
    }

    public void buildGUI() {
        JFrame frame = new JFrame("Sharix");
        frame.setContentPane(this);
        frame.setSize(800, 600);
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
};

