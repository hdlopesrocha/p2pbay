package ist.p2p.service;

import ist.p2p.dto.GossipDto;

import java.util.List;
import java.util.Random;

import net.tomp2p.dht.FutureSend;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class LaunchGossipService extends P2PBayService{
	private static Random random = new Random();
	private static RequestP2PConfiguration CONFIG=	new RequestP2PConfiguration(1, 1, 0);
	private boolean isLeader=false;
	private GossipDto result=null;
	private GossipDto temp;

	public GossipDto getResult() {
		return result;
	}


	@Override
	public boolean execute() {
		temp = new GossipDto();
		System.out.println("LAUNCH GOSSIP");
	
		
		peer.peer().objectDataReply(new ObjectDataReply() {
			@Override
			public Object reply(PeerAddress arg0, Object arg1) throws Exception {		
				if(arg1 instanceof Number160 && !isLeader){
					// i am the leader!
					isLeader = true;
					System.out.println("I am the leader!");
					temp.setWeight(1);
					advertiseNeighbors();
				}
				if(arg1 instanceof GossipDto){
					GossipDto dto = (GossipDto)arg1;
					if(dto.isReset()){
						temp = new GossipDto();
					}
					else{
						double newWeight = temp.getWeight() + dto.getWeight();
						temp.setWeight(newWeight);
					}
					advertiseNeighbors();
				}
				
				return null;
			}
		});
		
		chooseLeader();
		return true;
	}
	
	
	private void chooseLeader(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					System.out.println("CHOOSE LEADER");
					FutureSend future= peer.send(Number160.ZERO).object(Number160.ZERO).requestP2PConfiguration(CONFIG).start();
					future.awaitUninterruptibly();
				//	if(!future.isSuccess())
				//	System.out.println(future.failedReason());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}
	
	private void advertiseNeighbors(){
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// advertise neighbors
		new Thread(new Runnable() {
			@Override
			public void run() {
					while(true){
						List<PeerAddress> list = peer.peer().peerBean().peerMap().all();
					
						if(list.size()>0){
							System.out.println("W="+temp.getWeight()+ " ("+list.size()+")");
							int randomNum = random.nextInt(list.size());
							PeerAddress nextPeer = list.get(randomNum);
							if(nextPeer!=null){
								double newWeight = temp.getWeight()/2; 
								
								GossipDto dto = new GossipDto();
								dto.setWeight(newWeight);
								temp.setWeight(newWeight);
								
								peer.send(nextPeer.peerId()).object(dto).domainKey(DOMAIN_GOSSIP).requestP2PConfiguration(CONFIG).start();
								
							}
							break;
						}
						else {
							System.out.println("no neighbors found!");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					
					
					}
			}
		}).start();		
	}
	
}
