package ist.p2p.service;

import ist.p2p.dto.GossipDto;

import java.io.IOException;
import java.util.List;

import net.tomp2p.dht.FutureSend;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class LaunchGossipService extends P2PBayService {
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
				if (arg1 instanceof Number160) {
					if (!isLeader) {
						// i am the leader!
						isLeader = true;
						System.out.println("I am the leader!");
						synchronized (LaunchGossipService.this) {
							temp.setWeight(1);
						}
						advertiseNeighbors();
						return "ACK";
					}
				} else if (arg1 instanceof GossipDto) {
					GossipDto dto = (GossipDto) arg1;
					synchronized (LaunchGossipService.this) {
						if (dto.isReset()) {
							temp = new GossipDto(0);
						} else {
							double newWeight = (temp.getWeight() + dto
									.getWeight()) / 2f;
							temp.setWeight(newWeight);
						}
						dto = new GossipDto(temp.getWeight());
					}
					advertiseNeighbors();
					return dto;
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
					if (!future.isSuccess()) {
						System.out.println(future.failedReason());
					}
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

						List<PeerAddress> neighbors = peer.peer().peerBean()
								.peerMap().all();

						PeerAddress address = neighbors.size() > 0 ? neighbors
								.get(RANDOM.nextInt(neighbors.size())) : null;
						if (address != null) {

							GossipDto copy;
							synchronized (LaunchGossipService.this) {
								copy = new GossipDto(temp);
							}
							System.out.println("W=" + 1f / copy.getWeight()
									+ " sending to:" + address.peerSocketAddress().tcpPort());

							FutureDirect future = peer.peer()
									.sendDirect(address).object(copy).start();
							future.awaitUninterruptibly();
							if (future.isSuccess()) {
							
								if(future.object()!=null){
								
									GossipDto myW = (GossipDto) future.object();
									synchronized (LaunchGossipService.this) {
										temp.setWeight(myW.getWeight());
									}
									break;
								}
							} else {
								System.out.println(future.failedReason());
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
