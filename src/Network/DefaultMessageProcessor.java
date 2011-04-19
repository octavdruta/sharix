package src.Network;

import java.nio.ByteBuffer;

public class DefaultMessageProcessor implements MessageProcessor {
	static final int CHAR_SIZE = 2;
	static final int INT_SIZE = 4;
	
	@Override
	public ByteBuffer initialMessage(String filename, int fileLength) {
		ByteBuffer buf = ByteBuffer.allocateDirect(
				INT_SIZE + CHAR_SIZE * filename.length());
		buf.putInt(filename.length());
		for (char c : filename.toCharArray()) {
		    buf.putChar(c);
		}
		return buf;
	}

	@Override
	public ByteBuffer middleMessage(String filename, String chunk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuffer finalMessage(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuffer abortMessage(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChunk(ByteBuffer msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFileLength(ByteBuffer msg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFilename(ByteBuffer msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageType getMessageType(ByteBuffer msg) {
		// TODO Auto-generated method stub
		return null;
	}
}
