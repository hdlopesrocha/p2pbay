package seprs.unstructured;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SearchMessageReply implements Serializable, Message {
	String srcId;
	
	public SearchMessageReply( String src) {
		srcId = src;
	}
	
	public String getSrcId() {
		return srcId;
	}

	@Override
	public MessageType getMSGType() {
		return MessageType.SEARCH_REPLY;
	}

}
