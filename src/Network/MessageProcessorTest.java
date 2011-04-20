package src.Network;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class MessageProcessorTest extends TestCase {
	String username;
	String filename;
	int fileLength;
	byte[] byteChunk;
	
    protected void setUp() {
    	username = "gogu";
		filename = "Test";
		fileLength = 99;
		byteChunk = new byte[] {19, 23, 15};
	}
	
	protected void tearDown() {	}
	
	public void testInitialMessage() {
		ByteBuffer buf = MessageProcessor.initialMessage(username, filename, fileLength);
		assertEquals(MessageProcessor.BUFFER_SIZE, buf.limit());
		assertNull(MessageProcessor.getByteChunk(buf));
		assertEquals(fileLength, MessageProcessor.getFileLength(buf));
		assertEquals(username, MessageProcessor.getUsername(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.INITIAL, MessageProcessor.getMessageType(buf));
	}
	
	public void testMiddleMessage() {
		ByteBuffer buf = MessageProcessor.middleMessage(username, filename, byteChunk);
		assertEquals(MessageProcessor.BUFFER_SIZE, buf.limit());
		byte[] chunk = MessageProcessor.getByteChunk(buf);
		assertEquals(byteChunk[0], chunk[0]);
		assertEquals(byteChunk[1], chunk[1]);
		assertEquals(byteChunk[2], chunk[2]);
		assertEquals(-1, MessageProcessor.getFileLength(buf));
		assertEquals(username, MessageProcessor.getUsername(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.MIDDLE, MessageProcessor.getMessageType(buf));
	}
	
	public void testFinalMessage() {
		ByteBuffer buf = MessageProcessor.finalMessage(username, filename);
		assertEquals(MessageProcessor.BUFFER_SIZE, buf.limit());
		assertNull(MessageProcessor.getByteChunk(buf));
		assertEquals(-1, MessageProcessor.getFileLength(buf));
		assertEquals(username, MessageProcessor.getUsername(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.FINAL, MessageProcessor.getMessageType(buf));
	}
	
	public void testRequestMessage() {
		ByteBuffer buf = MessageProcessor.requestMessage(username, filename);
		assertEquals(MessageProcessor.BUFFER_SIZE, buf.limit());
		assertNull(MessageProcessor.getByteChunk(buf));
		assertEquals(-1, MessageProcessor.getFileLength(buf));
		assertEquals(username, MessageProcessor.getUsername(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.REQUEST, MessageProcessor.getMessageType(buf));
	}
}
