package ist.p2p.gossip;

import ist.p2p.gossip.message.GossipMessage;
import ist.p2p.domain.Domain;
import ist.p2p.domain.DomainType;
import ist.p2p.domain.Authentication;
import ist.p2p.domain.Item;

import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.dht.StorageLayer;
import net.tomp2p.storage.Data;
import net.tomp2p.peers.Number160;
import net.tomp2p.p2p.RequestP2PConfiguration;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public final class Gossip{

	//Implements this for use with gossip
	private PeerDHT tomp2p; 

	public static final int GOSSIP_INTERVAL_TIME = 1000;


	private final List<Integer> oldGossipIds = new ArrayList<Integer>();
	private Integer gossipId = 0;

	//Gossip current values
	private float weight=0;
	private float value; 
	private Set<String> auctions;
	private Set<String> users;
	//private float auctions;
	//private float users;

	public Gossip(PeerDHT tomp2p){
		this.tomp2p = tomp2p;
		this.init();
	}


	//Initiates the gossip algorithm
	private final void init(){

		this.restart();
		new GossipThread(this).start();


		final Gossip gossip = this;
		tomp2p.peer().objectDataReply(new ObjectDataReply(){

			@Override
			public Object reply(final PeerAddress sender, Object object){
				synchronized (gossip){
	
					GossipMessage gmsg = (GossipMessage)object;
					final Integer id = gmsg.getMessageId();
					System.out.println("RCVD");
					//old gossip
					if (!gossip.gossipId.equals(id) && gossip.oldGossipIds.contains(id)){
						return null;
					}

					//reset for new gossip
					if (!gossip.gossipId.equals(id) && !gossip.oldGossipIds.contains(id)){
						gossip.oldGossipIds.add(gossip.gossipId);
						gossip.gossipId = id;
						gossip.restart();
					}

					gossip.weight += gmsg.getWeight();
					gossip.value += gmsg.getValue();

					gossip.handleSetOfUsers(gmsg.getUserSet());
					gossip.handleSetOfAuctions(gmsg.getAuctionSet());
					//gossip.auctions += gmsg.getAuctions();
					//gossip.users += gmsg.getUsers();

					return null;
				}
			}
		});

	}
	public final void spreadGossip(){

		this.oldGossipIds.add(this.gossipId);
		this.gossipId = new Random().nextInt(Integer.MAX_VALUE);
		restart(1);
	}
	private final void restart(){
		restart(0);
	}
	private final void restart(int weight){

		this.weight = weight;
		this.value = 1;
		countStorage();
	}
	/**
	 * 	Chooses a random peer to gossip with 
	 *	If current weight is 0, then it has to wait until someone sends a gossip
	 *
	 */
	public final void query(){
		synchronized (this) {
			List<PeerAddress> myPeers = tomp2p.peer().peerBean().peerMap().all();
			if (myPeers.size() == 0)
				return;

			if (this.weight <= 0)	
					return;

			weight = weight/2;
			value = value/2;
		
			GossipMessage msg = new GossipMessage(gossipId, weight, value, this.auctions, this.users);
			
			PeerAddress targetPeer = myPeers.get(new Random().nextInt(myPeers.size()));
			tomp2p.peer().sendDirect(targetPeer).object(msg).start();
			/*
			tomp2p.send(new Number160(new Random())).object(msg)
				.requestP2PConfiguration(new RequestP2PConfiguration(1, 5, 0))
				.start();
*/

		//System.out.println("got  "+new Statistics(tomp2p.peer().peerBean().peerMap()).estimatedNumberOfNodes());

		}

	}
	public final void countStorage(){
		StorageLayer store = (StorageLayer)tomp2p.peer().peerBean().digestStorage();
		Map map = store.get();
		this.auctions = new HashSet<String>();
		this.users = new HashSet<String>();
	
		try{
			for (Object o:map.values()){
				Object obj = ((Data)o).object();
				if (obj instanceof String)
					continue;
				Domain dom = (Domain)obj;
				switch (dom.getDomainType()){
					case AUTH:
						Authentication auth = (Authentication) dom;
						this.users.add(auth.getHash());	//user hash as id #safetylast
						break;

					case ITEM:
						Item item = (Item) dom;
						this.auctions.add(item.getTitle());
						break;
				}
			}
		}catch(ClassNotFoundException e){
			System.out.println("Unable to find class");
				this.auctions = new HashSet<String>();
				this.users = new HashSet<String>();


		}catch(java.io.IOException e){
			System.out.println(e.getMessage());
		}


//		System.out.println("GOT : "+this.users+" USERS");
//		System.out.println("GOT: " +this.auctions+" Auctions");

	}

	/**
	 *	Concatenates the recieved Set with the his Set	
	 *	The prefered Set is the biggest
	 *
	 *	@param newUserSet
	 *				Set of users to be added 
	 */
	public final void handleSetOfUsers(Set<String> newUserSet){
		newUserSet.addAll(this.users);
		if (newUserSet.size() > this.users.size())
			this.users = newUserSet;
	}
	public final void handleSetOfAuctions(Set<String> newAuctionSet){
		newAuctionSet.addAll(this.auctions);
		if (newAuctionSet.size() > this.auctions.size())
			this.auctions = newAuctionSet;		
	}


	public final int calcUsers(){
		return this.users.size();
	}
	public final int calcAuctions(){
		return this.auctions.size();
	}
	public final int calcNodes(){
		return Math.round(this.value/this.weight);
	}

}