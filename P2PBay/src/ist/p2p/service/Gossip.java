package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.GossipDto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number480;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageGeneric;

public class Gossip extends P2PBayService {
	private static RequestP2PConfiguration CONFIG = new RequestP2PConfiguration(
			1, 10, 0);
	private static boolean isLeader = false;
	private static GossipDto currentResult = new GossipDto();
	private static GossipDto lastResult = new GossipDto();
	private static int consecutiveCounter = 0;
	private static int lastPeersCount = -1;
	private static boolean resetRequest = false;
	private static Gossip gossip;

	private Gossip() {

	}

	public static void addUser(int count) {
		synchronized (currentResult) {
			currentResult.setAvgRegisteredUsers(currentResult
					.getAvgRegisteredUsers() + count);
		}
	}

	public static void addItemOnSale(int count) {
		synchronized (currentResult) {
			currentResult.setAvgItemsOnSale(currentResult.getAvgItemsOnSale()
					+ count);
		}
	}

	public GossipDto getResult() {
		GossipDto ret = null;
		synchronized (currentResult) {
			ret = new GossipDto(currentResult);
		}
		return ret;
	}

	private static void reset() {
		resetRequest = true;
	}

	private double getItemsOnSaleAux() {
		StorageGeneric storage = peer.getPeerBean().getStorage();
		int salesCount = 0;
		for (Entry<Number480, Data> i : storage.map().entrySet()) {
			Number160 responsible = storage.findPeerIDForResponsibleContent(i
					.getKey().getLocationKey());

			if (i.getKey().getDomainKey().equals(DOMAIN_ITEM)
					&& peer.getPeerID().equals(responsible)) {
				try {
					Item item = (Item) i.getValue().getObject();
					if (!item.isClosed()) {
						++salesCount;
					}
					// System.out.println(i.getKey().getDomainKey());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return salesCount;
	}

	private double getUsersCountAux() {
		StorageGeneric storage = peer.getPeerBean().getStorage();
		int userCount = 0;
		for (Entry<Number480, Data> i : storage.map().entrySet()) {
			Number160 responsible = storage.findPeerIDForResponsibleContent(i
					.getKey().getLocationKey());

			if (i.getKey().getDomainKey().equals(DOMAIN_AUTH)
					&& peer.getPeerID().equals(responsible)) {
				++userCount;
			}
		}
		return userCount;
	}

	@Override
	public boolean execute() {
		// System.out.println("LAUNCH GOSSIP");
		peer.setObjectDataReply(new ObjectDataReply() {
			@Override
			public Object reply(PeerAddress arg0, Object arg1) throws Exception {
				if (arg1 instanceof Number160) {
					Number160 receivedNumber = (Number160) arg1;
					if (Number160.ONE.equals(receivedNumber)) {
						if (!isLeader) {
							// i am the leader!
							isLeader = true;
							synchronized (currentResult) {
								lastResult = new GossipDto(currentResult);
								currentResult.setWeight(1);
								currentResult.setWaveId(currentResult
										.getWaveId() + 1);
								currentResult
										.setAvgRegisteredUsers(getUsersCountAux());
								currentResult
										.setAvgItemsOnSale(getItemsOnSaleAux());

							}
							advertiseNeighbors();
						}
					} else {
						isLeader = false;
					}
				} else if (arg1 instanceof GossipDto) {
					GossipDto dto = (GossipDto) arg1;
					synchronized (currentResult) {

						if (currentResult.getWaveId() < dto.getWaveId()) {
							lastResult = new GossipDto(currentResult);
							currentResult.setWeight(0);
							currentResult
									.setAvgRegisteredUsers(getUsersCountAux());
							currentResult
									.setAvgItemsOnSale(getItemsOnSaleAux());

						}

						double newWeight = (currentResult.getWeight() + dto
								.getWeight()) / 2f;
						currentResult.setWeight(newWeight);
						currentResult.setWaveId(dto.getWaveId());
						currentResult.setAvgRegisteredUsers((currentResult
								.getAvgRegisteredUsers() + dto
								.getAvgRegisteredUsers()) / 2f);
						currentResult.setAvgItemsOnSale((currentResult
								.getAvgItemsOnSale() + dto.getAvgItemsOnSale()) / 2f);

					}
					advertiseNeighbors();

				}
				GossipDto ret = null;
				synchronized (currentResult) {
					ret = new GossipDto(currentResult);
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
				// System.out.println("RESET");
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

						List<PeerAddress> neighbors = peer.getPeerBean()
								.getPeerMap().getAll();

						if (!resetRequest
								&& (lastPeersCount == -1 || neighbors.size() >= lastPeersCount)) {
							lastPeersCount = neighbors.size();

							PeerAddress address = neighbors.size() > 0 ? neighbors
									.get(RANDOM.nextInt(neighbors.size()))
									: peer.getPeerAddress();

							GossipDto copy;
							synchronized (currentResult) {
								copy = new GossipDto(currentResult);
							}

							FutureResponse future = peer.sendDirect(address)
									.setObject(copy).start();
							future.awaitUninterruptibly();

							if (!future.isFailed()) {
								GossipDto myW = (GossipDto) future.getObject();
								if (myW != null) {
									synchronized (currentResult) {
										if (currentResult.sameValues(myW)) {
											consecutiveCounter++;
										} else {
											consecutiveCounter = 0;
										}

										if (consecutiveCounter >= 32) {
											// System.out.println("CONVERGED! "+
											// currentResult.toString());
											consecutiveCounter = 0;
											reset();
										} else {

											currentResult.setWeight(myW
													.getWeight());
											currentResult.setAvgRegisteredUsers(myW
													.getAvgRegisteredUsers());
											currentResult.setAvgItemsOnSale(myW
													.getAvgItemsOnSale());

											try {
												File file = new File("data.txt");

												// if file doesnt exists, then
												// create it
												if (!file.exists()) {
													file.createNewFile();
												}

												// true = append file
												FileWriter fileWritter = new FileWriter(
														file.getName(), true);
												BufferedWriter bufferWritter = new BufferedWriter(
														fileWritter);
												bufferWritter
														.write(currentResult
																.toString());
												bufferWritter.close();
											} catch (Exception e) {
												e.printStackTrace();
											}

										}
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

	public static int getNodeCount() {
		return (int) lastResult.getNodeCount();
	}

	public static int getRegisteredUsers() {
		return (int) lastResult.getRegisteredUsers();
	}

	public static int getItemsOnSale() {
		return (int) lastResult.getItemsOnSale();
	}

	public static void start() {
		if (gossip == null) {
			gossip = new Gossip();
			gossip.execute();
		}
	}

}
