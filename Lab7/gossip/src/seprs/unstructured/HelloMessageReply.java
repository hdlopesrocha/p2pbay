package seprs.unstructured;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HelloMessageReply implements Serializable, Message {
	
	public int replierId;
	//public String ip;
	public int messageId;


	public HelloMessageReply(int reply,int mid) {
		this.replierId = reply;
		this.messageId = mid; 
	//	this.ip = ip
	}
	
	@Override
	public MessageType getMSGType() {
		return MessageType.QUERY_REPLY;
	}

}
