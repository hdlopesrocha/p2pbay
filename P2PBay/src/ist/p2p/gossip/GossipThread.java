package ist.p2p.gossip;


/**
 *	Implements main gossip threads, sleeps for some time and then does a gossip query
 *	The time is defined by GOSSIP_INTERVAL_TIME defined by the Gossip class
 *
 *	@see ist.p2p.gossip.Servent;
 */
class GossipThread extends Thread {

	Gossip gossip;
	
	public GossipThread(Gossip goss){
		this.gossip = goss;
	}
	
	@Override
	public void run(){
		while(true){
			try{
				sleep(gossip.GOSSIP_INTERVAL_TIME);
				gossip.query();
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}	
	}
}