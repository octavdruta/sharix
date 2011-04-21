// SharixMediator module implementation.

package src.Mediator;

import src.SharixInterface.GUIMediator;
import src.SharixInterface.NetworkMediator;
import src.SharixInterface.WebServiceClientMediator;
import src.SharixInterface.Network;
import src.SharixInterface.GUI;
import src.SharixInterface.WebServiceClient;
import src.SharixInterface.User;
import java.util.*;

import org.apache.log4j.Logger;

public class SharixMediator implements NetworkMediator, GUIMediator, WebServiceClientMediator {
	Network network;
	GUI gui;
	WebServiceClient webServiceClient;
	String username;

	public SharixMediator(String username) {
		this.username = username;
	}
	
	public String getMyUsername() {
		return username;
	}

	// Registeres network component to mediator.
	public void registerNetwork(Network network) {
		this.network = network;
	}

	// Registers GUI component to mediator.
	public void registerGUI(GUI gui) {
		this.gui = gui;
	}

	// Registers WebServiceClient to mediator.
	public void registerWebServiceClient(WebServiceClient client) {
		webServiceClient = client;
	}

	// Adds a new user and its corresponding list of shared files.
	@Override
	public boolean addUser(String name, Vector<String> fileList) {
		return gui.addUser(name, fileList);
	}

	// Removes user.
	@Override
	public boolean removeUser(String name) {
		return gui.removeUser(name);
	}

	// Adds a new shared file to the user's list.
	@Override
	public boolean addFileToUser(String user, String file) {
		return gui.addFileToUser(user, file);
	}

	// Removes a shared file from user's list.
	@Override
	public boolean removeFileFromUser(String user, String file) {
		return gui.removeFileFromUser(user, file);
	}

	// Updates file transfer status.
	@Override
	public synchronized void updateTransfer(String fromUser, String toUser, String file,
			String status, Integer progress) {
		gui.updateTransfer(fromUser, toUser, file, status, progress);
	}

	// Returns the list with all the users currently connected to the server.
	@Override
	public Vector<User> getUserList() {
		return webServiceClient.getUserList();
	}

	// Initializes fname file download.
	@Override
	public boolean downloadFile(String fromUser, String fname) {
		return network.downloadFile(fromUser, fname);
	}

	// Initializes fname file upload.
	@Override
	public boolean uploadFile(String toUser, String fname) {
		return network.uploadFile(toUser, fname);
	}

	// Aborts fname file download.
	@Override
	public boolean abortDownload(String fromUser, String fname) {
		return network.abortDownload(fromUser, fname);
	}

	// Aborts fname file upload.
	@Override
	public boolean abortUpload(String toUser, String fname) {
		return network.abortUpload(toUser, fname);
	}
};
