package ist.p2p.gossip.message;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
/*
*		Gossip message transmitted on the p2p network,
*	this message encapsulates all the counting gossip queries that are required
*	by the p2pbay requirements.
* 		Can also be used for custom queries.
*/

@SuppressWarnings("serial")
public final class GossipMessage implements Serializable, Message{

	private final Integer messageId;
	private final float value;
	private final float weight;
	private float auctions = 0;
	private float users = 0;
	private final Set<String> auctionSet;
	private final Set<String> userSet;


	public GossipMessage(final Integer mId, final float weight,
			final float value, final Set<String> auctions, final Set<String> users){

		this.messageId = mId;
		this.weight = weight;
		this.value = value;
		this.auctionSet = auctions;
		this.userSet = users;
		//this.auctions = numAuctions;
		//this.users = numUsers;


	}
	public GossipMessage(final Integer mId, final float weight,final float value) {
		this.messageId = mId;
		this.weight = weight;
		this.value = value;
		this.auctions = 0;
		this.users = 0;
		this.auctionSet = new HashSet<String>();
		this.userSet = new HashSet<String>();
	}

	public final Integer getMessageId(){
		return this.messageId;
	}
	public final float getWeight(){
		return this.weight;
	}
	public final float getValue(){
		return this.value;
	}
	public final float getAuctions(){
		return this.auctions;
	}
	public final float getUsers(){
		return this.users;
	}
	public final Set getUserSet(){
		return this.userSet;
	}
	public final Set getAuctionSet(){
		return this.auctionSet;
	}

	@Override
	public MessageType getMSGType(){
		return MessageType.GOSSIP;
	}

}