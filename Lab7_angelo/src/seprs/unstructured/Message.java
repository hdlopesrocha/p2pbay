package seprs.unstructured;

import java.io.Serializable;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5195168549141197582L;
	private MessageType messageType;
	private Object content;
	
	public Message(MessageType msgType, Object content){
		this.messageType = msgType;
		this.content = content;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	
	public Object getContent(){
		return content;
	}
}
