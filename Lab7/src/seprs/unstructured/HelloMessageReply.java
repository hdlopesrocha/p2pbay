package seprs.unstructured;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HelloMessageReply implements Serializable, Message {
	int srcId;
	
	public HelloMessageReply( int src) {
		srcId = src;
	}
	
	public int getSrcId() {
		return srcId;
	}

	@Override
	public MessageType getMSGType() {
		return MessageType.HELLO_REPLY;
	}

}
