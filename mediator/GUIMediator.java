// Graphical User Interface Mediator.

public interface GUIMediator {
  // Adds a new user and its corresponding list of shared files.
  public boolean addUser(String name, Vector<String> fileList);

  // Removes user.
  public boolean removeUser(String name);

  // Adds a new shared file to the user's list.
  public boolean addFileToUser(String user, String file);

  // Removes a shared file from user's list.
  public boolean removeFileFromUser(String user, String file);

  // Updates file transfer status.
  public void updateTransfer(String fromUser, String toUser, String file,
                             String status, Integer progress);
};

