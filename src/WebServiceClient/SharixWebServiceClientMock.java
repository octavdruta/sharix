package src.WebServiceClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import src.SharixUser;
import src.Mediator.SharixMediator;
import src.SharixInterface.User;

public class SharixWebServiceClientMock extends SharixWebServiceClient {
	public SharixWebServiceClientMock(SharixMediator mediator, String configPath) {
		super(mediator);
		loadConfig(configPath);
	}
	
	private void loadConfig(String configPath) {
		File configDir = new File(configPath);
		for (File f : configDir.listFiles()) {
			if (f.isFile() && f.getName().endsWith(".txt")) {
				String username = f.getName().substring(0, f.getName().length() - 4);
				try {
					BufferedReader in = new BufferedReader(new FileReader(f.getPath()));
					String ip = in.readLine().trim();
					Integer port = Integer.parseInt(in.readLine().trim());
					User user = new SharixUser(username, ip, port);
					File fileListDir = new File(configPath + "/" + username);
					for (File userFile : fileListDir.listFiles()) {
						if (userFile.isFile()) {
							user.addFile(userFile.getName());
						}
					}
					users.add(user);
					System.out.println("Added user " + user.getName());
					if (!username.equals(mediator.getMyUsername())) {
						mediator.addUser(user.getName(), user.getFileList());
					}
				} catch (FileNotFoundException e) {
				} catch (IOException e) { }
			}
		}
	}
}