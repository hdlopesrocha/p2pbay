package ist.p2p.service;

import ist.p2p.dto.GossipDto;

import java.util.List;
import java.util.Random;

import net.tomp2p.dht.FutureSend;
import net.tomp2p.dht.SendBuilder;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class LaunchGossipService extends P2PBayService {
	private static Random random = new Random();
	private static RequestP2PConfiguration CONFIG = new RequestP2PConfiguration(
			1, 10, 0);
	private boolean isLeader = false;
	private GossipDto result = null;
	private GossipDto temp;

	public GossipDto getResult() {
		return result;
	}

	@Override
	public boolean execute() {
		temp = new GossipDto(0);
		System.out.println("LAUNCH GOSSIP");

		peer.peer().objectDataReply(new ObjectDataReply() {
			@Override
			public Object reply(PeerAddress arg0, Object arg1) throws Exception {
				if (arg1 instanceof Number160 && !isLeader) {
					// i am the leader!
					isLeader = true;
					System.out.println("I am the leader!");
					temp.setWeight(1);
					advertiseNeighbors();
					return "ACK";
				}
				if (arg1 instanceof GossipDto) {
					GossipDto dto = (GossipDto) arg1;
					if (dto.isReset()) {
						temp = new GossipDto(0);
					} else {
						double newWeight =( temp.getWeight() + dto.getWeight())/2f;
						temp.setWeight(newWeight);
					}
					advertiseNeighbors();
					
					return new GossipDto(temp.getWeight());
				}

				return "UNKNOWN REQUEST";
			}
		});

		chooseLeader();
		return true;
	}

	private void chooseLeader() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// System.out.println("CHOOSE LEADER");
					FutureSend future = peer.send(Number160.ZERO)
							.object(Number160.ZERO)
							.requestP2PConfiguration(CONFIG).start();
					future.awaitUninterruptibly();
					if (!future.isSuccess())
						System.out.println(future.failedReason());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private void advertiseNeighbors() {


		// advertise neighbors
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("W=" + temp.getWeight());
					double newWeight = temp.getWeight();

					GossipDto dto = new GossipDto(newWeight);

					FutureSend future =peer.send(new Number160(RANDOM)).object(dto)
							.domainKey(DOMAIN_GOSSIP)
							.requestP2PConfiguration(CONFIG).start();
					future.awaitUninterruptibly();
					if(future.isSuccess()){
						GossipDto myW= (GossipDto) future.object();
						temp.setWeight(myW.getWeight());
						break;
					
					}
				

				}
			}
		}).start();
	}

}
