package src.Network;

import java.nio.ByteBuffer;

public class MessageProcessor {
	static final int INITIAL = 1;
	static final int MIDDLE = 2;
	static final int FINAL = 3;
	static final int REQUEST = 4;
	
	static final int BUFFER_SIZE = 1000;
	
	private static void storeString(ByteBuffer buf, String filename) {
		buf.putInt(filename.length());
		for (char c : filename.toCharArray()) {
		    buf.putChar(c);
		}
	}
	
	private static void skipType(ByteBuffer buf) {
		buf.getInt();
	}
	
	private static void skipUsername(ByteBuffer buf) {
		skipType(buf);
		int length = buf.getInt();
		for (int i = 0; i < length; i++) {
			buf.getChar();
		}
	}
	
	private static void skipFilename(ByteBuffer buf) {
		skipUsername(buf);
		int length = buf.getInt();
		for (int i = 0; i < length; i++) {
			buf.getChar();
		}
	}
	
	private static void addPadding(ByteBuffer buf) {
		while (buf.position() < buf.capacity()) {
			buf.put((byte) 0);
		}
	}
	
	public static ByteBuffer initialMessage(String username, String filename, int fileLength) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(INITIAL);
		storeString(buf, username);
		storeString(buf, filename);
		buf.putInt(fileLength);
		addPadding(buf);
		buf.flip();
		return buf;
	}
/*
	public static ByteBuffer middleMessage(String username, String filename, String chunk) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(MIDDLE);
		storeString(buf, username);
		storeString(buf, filename);
		buf.putInt(chunk.length());
		for (char c : chunk.toCharArray()) {
			buf.putChar(c);
		}
		addPadding(buf);
		buf.flip();
		return buf;
	}
	*/
	public static ByteBuffer middleMessage(String username, String filename, byte[] chunk) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(MIDDLE);
		storeString(buf, username);
		storeString(buf, filename);
		buf.putInt(chunk.length);
		for (byte c : chunk) {
			buf.put(c);
		}
		addPadding(buf);
		buf.flip();
		return buf;
	}

	public static ByteBuffer finalMessage(String username, String filename) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(FINAL);
		storeString(buf, username);
		storeString(buf, filename);
		addPadding(buf);
		buf.flip();
		return buf;
	}
	
	public static ByteBuffer requestMessage(String username, String filename) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(REQUEST);
		storeString(buf, username);
		storeString(buf, filename);
		addPadding(buf);
		buf.flip();
		return buf;
	}
/*
	public static String getChunk(ByteBuffer msg) {
		if (getMessageType(msg) != MIDDLE) {
			return null;
		}
		skipFilename(msg);
		String chunk = new String();
		int length = msg.getInt();
		for (int i = 0; i < length; i++) {
			chunk += msg.getChar();
		}
		msg.rewind();
		return chunk;
	}
	*/
	public static byte[] getByteChunk(ByteBuffer msg) {
		if (getMessageType(msg) != MIDDLE) {
			return null;
		}
		skipFilename(msg);
		int length = msg.getInt();
		byte[] chunk = new byte[length];
		for (int i = 0; i < length; i++) {
			chunk[i] = msg.get();
		}
		msg.rewind();
		return chunk;
	}

	public static int getFileLength(ByteBuffer msg) {
		if (getMessageType(msg) != INITIAL) {
			return -1;
		}
		skipFilename(msg);
		int fileLength = msg.getInt();
		msg.rewind();
		return fileLength;
	}

	public static String getUsername(ByteBuffer msg) {
		skipType(msg);
		String username = new String();
		int length = msg.getInt();
		for (int i = 0; i < length; i++) {
			username += msg.getChar();
		}
		msg.rewind();
		return username;
	}
	public static String getFilename(ByteBuffer msg) {
		skipUsername(msg);
		String filename = new String();
		int length = msg.getInt();
		for (int i = 0; i < length; i++) {
			filename += msg.getChar();
		}
		msg.rewind();
		return filename;
	}

	public static int getMessageType(ByteBuffer msg) {
		int messageType = msg.getInt();
		msg.rewind();
		return messageType;
	}
}
