package seprs.unstructured;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GossipMessage implements Serializable, Message{

	private int messageId;
	private float value;
	private float weight;

	public GossipMessage(int mId, float weight, float value){
		this.messageId = mId;
		this.value = value;
		this.weight = weight;
	}

	public int getMessageId(){
		return this.messageId;
	}
	public float getValue(){
		return this.value;
	}
	public float getWeight(){
		return this.weight;
	}

	@Override
	public MessageType getMSGType(){
		return MessageType.GOSSIP;
	}

}