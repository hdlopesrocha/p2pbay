package ist.p2p.service;

import ist.p2p.dto.GossipDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureResponse;
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
		peer.setObjectDataReply(new ObjectDataReply() {
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
					System.out.println("CHOOSE LEADER");
					FutureDHT future = peer.send(Number160.ZERO)
							.setObject(Number160.ZERO)
							.setRequestP2PConfiguration(CONFIG).start();
					future.awaitUninterruptibly();
					if (future.isFailed()) {
						System.out.println(future.getFailedReason());
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

						List<PeerAddress> neighbors = new ArrayList<PeerAddress>();
						neighbors.addAll(peer.getPeerBean().getPeerMap().getAll());
				
						if (neighbors.size() > 0) {
							for (PeerAddress pa : neighbors) {
								System.out.print(pa.portTCP() + "|");
							}
							System.out.println();
						}
						PeerAddress address = neighbors.size() > 0 ? neighbors
								.get(RANDOM.nextInt(neighbors.size())) : null;

						if (address != null) {

							GossipDto copy;
							synchronized (LaunchGossipService.this) {
								copy = new GossipDto(temp);
							}
							System.out.println("W=" + 1f / copy.getWeight());

							FutureResponse future = peer.sendDirect(address).setObject(copy).start();
							future.awaitUninterruptibly();

							
							
							if (!future.isFailed()) {
								GossipDto myW = (GossipDto) future.getObject();
								synchronized (LaunchGossipService.this) {
									temp.setWeight(myW.getWeight());
								}
								break;

							} else {
								System.out.println(future.getFailedReason());
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
