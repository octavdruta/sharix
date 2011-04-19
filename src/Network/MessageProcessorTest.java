package src.Network;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class MessageProcessorTest extends TestCase {
	String filename;
	int fileLength;
	String chunk;
	
    protected void setUp() {
		filename = "Test";
		fileLength = 99;
		chunk = "chunk";
	}
	
	protected void tearDown() {	}
	
	public void testInitialMessage() {
		ByteBuffer buf = MessageProcessor.initialMessage(filename, fileLength);
		assertEquals(MessageProcessor.BUFFER_SIZE, buf.limit());
		assertNull(MessageProcessor.getChunk(buf));
		assertEquals(fileLength, MessageProcessor.getFileLength(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.INITIAL, MessageProcessor.getMessageType(buf));
	}
	
	public void testMiddleMessage() {
		ByteBuffer buf = MessageProcessor.middleMessage(filename, chunk);
		assertEquals(chunk, MessageProcessor.getChunk(buf));
		assertEquals(-1, MessageProcessor.getFileLength(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.MIDDLE, MessageProcessor.getMessageType(buf));
	}
	
	public void testFinalMessage() {
		ByteBuffer buf = MessageProcessor.finalMessage(filename);
		assertNull(chunk, MessageProcessor.getChunk(buf));
		assertEquals(-1, MessageProcessor.getFileLength(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.FINAL, MessageProcessor.getMessageType(buf));
	}
	
	public void testRequestMessage() {
		ByteBuffer buf = MessageProcessor.requestMessage(filename);
		assertNull(chunk, MessageProcessor.getChunk(buf));
		assertEquals(-1, MessageProcessor.getFileLength(buf));
		assertEquals(filename, MessageProcessor.getFilename(buf));
		assertEquals(MessageProcessor.REQUEST, MessageProcessor.getMessageType(buf));
	}
}
