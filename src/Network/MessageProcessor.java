package src.Network;

import java.nio.ByteBuffer;

public class MessageProcessor {
	static final int INITIAL = 1;
	static final int MIDDLE = 2;
	static final int FINAL = 3;
	static final int REQUEST = 4;
	
	static final int BUFFER_SIZE = 1000;
	
	private static void storeFilename(ByteBuffer buf, String filename) {
		buf.putInt(filename.length());
		for (char c : filename.toCharArray()) {
		    buf.putChar(c);
		}
	}
	
	private static void skipType(ByteBuffer buf) {
		buf.getInt();
	}
	
	private static void skipFilename(ByteBuffer buf) {
		skipType(buf);
		int length = buf.getInt();
		for (int i = 0; i < length; i++) {
			buf.getChar();
		}
	}
	
	public static ByteBuffer initialMessage(String filename, int fileLength) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(INITIAL);
		storeFilename(buf, filename);
		buf.putInt(fileLength);
		buf.flip();
		return buf;
	}

	public static ByteBuffer middleMessage(String filename, String chunk) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(MIDDLE);
		storeFilename(buf, filename);
		buf.putInt(chunk.length());
		for (char c : chunk.toCharArray()) {
			buf.putChar(c);
		}
		buf.flip();
		return buf;
	}

	public static ByteBuffer finalMessage(String filename) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(FINAL);
		storeFilename(buf, filename);
		buf.flip();
		return buf;
	}
	
	public static ByteBuffer requestMessage(String filename) {
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		buf.putInt(REQUEST);
		storeFilename(buf, filename);
		buf.flip();
		return buf;
	}

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

	public static int getFileLength(ByteBuffer msg) {
		if (getMessageType(msg) != INITIAL) {
			return -1;
		}
		skipFilename(msg);
		int fileLength = msg.getInt();
		msg.rewind();
		return fileLength;
	}

	public static String getFilename(ByteBuffer msg) {
		skipType(msg);
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
