package seprs.unstructured;

import java.io.Serializable;

/**
*	Message used to confirm that the gossip message was received
*	with success.
*/
@SuppressWarnings("serial")
public class GossipMessageReply implements Serializable, Message{

	private int messageId;
	private float value;
	private float weight;



	public GossipMessageReply(GossipMessage gmsg){
		this.messageId = gmsg.getMessageId();
	}
	public GossipMessageReply(int mId, float weight, float value){
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
		return MessageType.GOSSIP_REPLY;
	}
}