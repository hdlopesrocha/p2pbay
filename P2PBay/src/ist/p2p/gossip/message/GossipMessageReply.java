package ist.p2p.gossip.message;

import java.io.Serializable;

/**
*	Message used to confirm that the gossip message was received
*	with success.
*/
@SuppressWarnings("serial")
public class GossipMessageReply implements Serializable, Message{

	private final Integer messageId;

	public GossipMessageReply(final GossipMessage gmsg){
		this.messageId = gmsg.getMessageId();
	}

	@Override
	public MessageType getMSGType(){
		return MessageType.GOSSIP_REPLY;
	}
}