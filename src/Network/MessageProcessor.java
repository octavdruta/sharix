package src.Network;

import java.nio.ByteBuffer;

public interface MessageProcessor {
	static enum MessageType {INITIAL, MIDDLE, FINAL};
	
	public ByteBuffer initialMessage(String filename, int fileLength);
    public ByteBuffer middleMessage(String filename, String chunk);
    public ByteBuffer finalMessage(String filename);
    public ByteBuffer abortMessage(String filename);
    
    public MessageType getMessageType(ByteBuffer msg);
    public String getFilename(ByteBuffer msg);
    public int getFileLength(ByteBuffer msg);
    public String getChunk(ByteBuffer msg);
}
