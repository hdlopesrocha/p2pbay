package ist.p2p.service;

import ist.p2p.dto.GossipDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number480;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.KeyLock;
import net.tomp2p.storage.Storage;

public class LaunchGossipService extends P2PBayService {
	private static RequestP2PConfiguration CONFIG = new RequestP2PConfiguration(
			1, 10, 0);
	private boolean isLeader = false;
	private GossipDto result;
	private int lastPeersCount = -1;
private boolean resetRequest=false;
	public GossipDto getResult() {
		GossipDto ret = null;
		synchronized (LaunchGossipService.this) {
			ret = new GossipDto(result);
		}
		return ret;
	}

	public void reset() {
		resetRequest = true;
	}
	
	@Override
	public boolean execute() {
		result = new GossipDto();
	//	System.out.println("LAUNCH GOSSIP");
		peer.setObjectDataReply(new ObjectDataReply() {
			@Override
			public Object reply(PeerAddress arg0, Object arg1) throws Exception {
				if (arg1 instanceof Number160) {
					Number160 receivedNumber = (Number160) arg1;
					if (Number160.ONE.equals(receivedNumber)) {
						if (!isLeader) {
							// i am the leader!
							isLeader = true;
							synchronized (LaunchGossipService.this) {
								result.setWeight(1);
								result.setWaveId(result.getWaveId() + 1);
								result.setAvgRegisteredUsers(userCount);
								result.setAvgItemsOnSale(saleCount);								
							}
							advertiseNeighbors();
						}
					} else {
						isLeader = false;
					}
				} else if (arg1 instanceof GossipDto) {
					GossipDto dto = (GossipDto) arg1;
					synchronized (LaunchGossipService.this) {

						if (result.getWaveId() < dto.getWaveId()) {
							result.setWeight(0);
							result.setAvgRegisteredUsers(userCount);
							result.setAvgItemsOnSale(saleCount);								

						}

						double newWeight = (result.getWeight() + dto
								.getWeight()) / 2f;
						result.setWeight(newWeight);
						result.setWaveId(dto.getWaveId());
						result.setAvgRegisteredUsers((result.getAvgRegisteredUsers() + dto.getAvgRegisteredUsers())/2f);
						result.setAvgItemsOnSale((result.getAvgItemsOnSale() + dto.getAvgItemsOnSale())/2f);

					}
					advertiseNeighbors();

				}
				GossipDto ret=null;
				synchronized (LaunchGossipService.this) {
					ret = new GossipDto(result);
				}
				return ret;
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
					FutureDHT future = peer.send(Number160.ZERO)
							.setObject(Number160.ONE)
							.setRequestP2PConfiguration(CONFIG).start();
					future.awaitUninterruptibly();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private void sendReset() {
		lastPeersCount = -1;

		new Thread(new Runnable() {
			@Override
			public void run() {
			//	System.out.println("RESET");
				FutureDHT future = peer.send(Number160.ZERO)
						.setObject(Number160.ZERO)
						.setRequestP2PConfiguration(CONFIG).start();
				future.awaitUninterruptibly();
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
						Thread.sleep(10);

						List<PeerAddress> neighbors = new ArrayList<PeerAddress>();
						neighbors.addAll(peer.getPeerBean().getPeerMap()
								.getAll());

						if (!resetRequest && (lastPeersCount == -1
								|| neighbors.size() >= lastPeersCount)) {
							lastPeersCount = neighbors.size();

							PeerAddress address = neighbors.size() > 0 ? neighbors
									.get(RANDOM.nextInt(neighbors.size()))
									: null;

							if (address != null) {

								GossipDto copy;
								synchronized (LaunchGossipService.this) {
									copy = new GossipDto(result);
								}

								FutureResponse future = peer
										.sendDirect(address).setObject(copy)
										.start();
								future.awaitUninterruptibly();

								if (!future.isFailed()) {
									GossipDto myW = (GossipDto) future
											.getObject();
									synchronized (LaunchGossipService.this) {
										result.setWeight(myW.getWeight());
										result.setAvgRegisteredUsers(myW.getAvgRegisteredUsers());
										result.setAvgItemsOnSale(myW.getAvgItemsOnSale());
									}
									break;

								} 
							}

						} else {
							sendReset();
							resetRequest = false;
							break;
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
