package seprs.unstructured;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SearchMessage implements Serializable, Message {
	
	String word;

	public SearchMessage( String src) {
		word = src;
	}
	
	public String getWord() {
		return word;
	}
	
	@Override
	public MessageType getMSGType() {
		return MessageType.SEARCH;
	}

}