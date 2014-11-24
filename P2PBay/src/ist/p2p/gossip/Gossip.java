package ist.p2p.gossip;

import ist.p2p.gossip.message.GossipMessage;

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

import net.tomp2p.p2p.Statistics;

public final class Gossip{

	//Implements this for use with gossip
	private PeerDHT tomp2p; 

	public static final int GOSSIP_INTERVAL_TIME = 1000;


	private final List<Integer> oldGossipIds = new ArrayList<Integer>();
	private Integer gossipId = 0;

	//Gossip current values
	private float weight=0;
	private float value; 
	private float auctions;
	private float users;

	public Gossip(PeerDHT tomp2p){
		System.out.println("Starting gossip");
		this.tomp2p = tomp2p;
		this.init();
	}


	//Initiates the gossip algorithm
	private final void init(){
		//start threading
		this.restart();
		new GossipThread(this).start();

		final Gossip gossip = this;
		tomp2p.peer().objectDataReply(new ObjectDataReply(){

			@Override
			public Object reply(final PeerAddress sender, Object object){
				synchronized (gossip){
	
					GossipMessage gmsg = (GossipMessage)object;
					final Integer id = gmsg.getMessageId();

					if (gossip.gossipId.equals(null) ||  !gossip.gossipId.equals(id)){//&& !gossip.oldGossipIds.contains(id)){
						gossip.oldGossipIds.add(gossip.gossipId);
						gossip.gossipId = id;
						gossip.restart();

					}

					gossip.weight += gmsg.getWeight();
					gossip.value += gmsg.getValue();
					gossip.auctions += gmsg.getAuctions();
					gossip.users += gmsg.getUsers();


					return null;
				}
			}
		});

	}
	public final void spreadGossip(){

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
	//		List<PeerAddress> myPeers = tomp2p.peer().peerBean().peerMap().all();
	//					System.out.println("got "+myPeers.size()+" peers ");
			if (this.weight <= 0)	
					return;
			//int size = tomp2p.peer().peerBean().peerMap().size();
/*
			if (myPeers.size() <= 0)
				return;
*/

			weight = weight/2;
			value = value/2;
			auctions = auctions/2;
			users = users/2;

			System.out.println("got  "+new Statistics(tomp2p.peer().peerBean().peerMap()).estimatedNumberOfNodes());
			GossipMessage msg = new GossipMessage(gossipId, weight, value, auctions, users);
		//	PeerAddress targetPeer = myPeers.get(new Random().nextInt(myPeers.size()));
		//	tomp2p.peer().sendDirect(targetPeer).object(msg).start();
		//tomp2p.send(new Number160(new Random())).object(msg)
	//		.requestP2PConfiguration(new RequestP2PConfiguration(1, 5, 0))
	//		.start();	

		}

	}
	public final void countStorage(){

		StorageLayer store = (StorageLayer)tomp2p.peer().peerBean().digestStorage();
		Map map = store.get();
		this.auctions = 0;
		this.users = 0;
		try{
			for (Object o:map.values()){
				String className = ((Data)o).object().getClass().getName();
				if(className.equals("ist.p2p.domain.Item")){	//temporary solutions
					this.auctions+=1;
				}else if (className.equals("ist.p2p.domain.Authentication")){
					this.users+=1;
				}

			}
		}catch(ClassNotFoundException | java.io.IOException e){
				System.out.println("Unable to find class");
				this.auctions = 0;
				this.users = 0;
		}
	}

	public final int calcUsers(){
		return Math.round(this.users/this.weight);
	}
	public final int calcAuctions(){
		return Math.round(this.auctions/this.weight);
	}
	public final int calcNodes(){
		return Math.round(this.value/this.weight);
	}


	/*Handle node leaving*/
	public final void logout(){
		//TODO
	}


}