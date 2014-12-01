package seprs.unstructured;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HelloMessage implements Serializable, Message {
	public String query;
	public int messageId;

	public HelloMessage(String query, int mid) {
		this.query = query;
		this.messageId = mid;
	}

	@Override
	public MessageType getMSGType() {
		return MessageType.QUERY;
	}

}
