package src;

import java.util.Vector;

import src.SharixInterface.User;

public class SharixUser implements User {
	String name;
	String ip;
	int port;
	Vector<String> fileList;
	
	public SharixUser(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public String getAddress() {
		return ip;
	}

	@Override
	public Vector<String> getFileList() {
		return fileList;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getPort() {
		return port;
	}

	@Override
	public boolean addFile(String file) {
		return fileList.add(file);
	}

	@Override
	public boolean removeFile(String file) {
		return fileList.remove(file);
	}
}
