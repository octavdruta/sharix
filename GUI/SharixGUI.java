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
import java.awt.*;
import javax.swing.*;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class SharixGUI extends JPanel implements GUI  {
    private static final long serialVersionUID = 1L;
    private TreeMap<String, Vector<String>> users =
			new TreeMap<String, Vector<String>>();;
    private DefaultListModel usersModel = new DefaultListModel();
    private DefaultListModel filesModel = new DefaultListModel();
    private JScrollPane filesPanel;
    private JScrollPane usersPanel;
    private JPanel activityPanel;
    private JList userList;
    private JList fileList;
	final int VERTICAL_SPLIT_POINT = 400;
	final int HORIZONTAL_SPLIT_POINT = 600;

    public SharixGUI() {
        init();
    }
    
    private void init() {
		setLayout(new BorderLayout());

        fileList = new JList(filesModel);
        userList = new JList(usersModel);
        
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

