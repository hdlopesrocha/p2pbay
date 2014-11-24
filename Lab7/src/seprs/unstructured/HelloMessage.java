package seprs.unstructured;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HelloMessage implements Serializable, Message {
	int srcId;
	
	public HelloMessage( int src) {
		srcId = src;
	}
	
	public int getSrcId() {
		return srcId;
	}

	@Override
	public MessageType getMSGType() {
		return MessageType.HELLO;
	}

}
