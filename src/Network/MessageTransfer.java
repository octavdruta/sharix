package src.Network;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface MessageTransfer {
	
	// Transfer buffer data to a certain user.
	public boolean send(String username, ByteBuffer buffer) throws IOException ;
	
}
