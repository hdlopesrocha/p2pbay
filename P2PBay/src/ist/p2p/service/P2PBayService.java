package ist.p2p.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.p2p.builder.BootstrapBuilder;
import net.tomp2p.p2p.builder.DiscoverBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number640;
import net.tomp2p.replication.IndirectReplication;
import net.tomp2p.storage.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class P2PBayService.
 */
public abstract class P2PBayService {

	/**
	 * Execute.
	 *
	 * @return the t
	 */
	public abstract boolean execute();

	/** The peer. */
	static protected PeerDHT peer = null;

	public static final Number160 DOMAIN_GOSSIP = Number160
			.createHash("gossip");

	/** The Constant DOMAIN_AUTH. */
	public static final Number160 DOMAIN_AUTH = Number160.createHash("auth");

	/** The Constant DOMAIN_ITEM. */
	public static final Number160 DOMAIN_ITEM = Number160.createHash("item");

	/** The Constant DOMAIN_BID. */
	public static final Number160 DOMAIN_BID = Number160.createHash("bid");

	/** The Constant DOMAIN_USER_BIDS. */
	public static final Number160 DOMAIN_USER_BIDS = Number160
			.createHash("user_bids");

	/** The Constant DOMAIN_ITEM_BIDS. */
	public static final Number160 DOMAIN_ITEM_BIDS = Number160
			.createHash("item_bids");

	/** The Constant DOMAIN_PURCHASES. */
	public static final Number160 DOMAIN_PURCHASES = Number160
			.createHash("purchases");

	/** The Constant DOMAIN_WORD. */
	public static final Number160 DOMAIN_WORD = Number160.createHash("word");

	/** The Constant RANDOM. */
	public static final Random RANDOM = new Random();

	/**
	 * Connect.
	 *
	 * @param masterIp
	 *            the ip
	 * @param masterPort
	 *            the port
	 * @param myPeerPort
	 *            the my peer port
	 * @param myPeerId
	 *            the my peer id
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected static void connect(final String masterIp, final int masterPort,
			final int myPeerPort, Number160 myPeerId) throws IOException {

		final Peer tempPeer = new PeerBuilder(myPeerId).ports(myPeerPort)
				.behindFirewall(true).start();

		final InetAddress address = InetAddress.getByName(masterIp);
		final DiscoverBuilder discover = tempPeer.discover()
				.inetAddress(address).ports(masterPort);
		final BootstrapBuilder bootstrap = tempPeer.bootstrap()
				.inetAddress(address).ports(masterPort);

		// Future Bootstrap - slave
		{
			final BaseFuture futureBootstrap = bootstrap.start()
					.awaitUninterruptibly();

			if (futureBootstrap.isFailed()) {
				System.out
						.println("Bootstrap with direct connection failed. Reason = "
								+ futureBootstrap.failedReason());
				tempPeer.shutdown();
			}
		}

		// Future Discover
		{
			final FutureDiscover futureDiscover = discover.start()
					.awaitUninterruptibly();

			if (futureDiscover.isFailed()) {
				System.out
						.println("Discover with direct connection failed. Reason = "
								+ futureDiscover.failedReason());
				tempPeer.shutdown();
			}
		}

		peer = new PeerBuilderDHT(tempPeer).start();
		new IndirectReplication(peer).autoReplication(true).start();
		new LaunchGossipService().execute();

		System.out.println("*** PORT " + myPeerPort + " ***");
	}

	/**
	 * Gets the all.
	 *
	 * @param domain
	 *            the domain
	 * @param key
	 *            the key
	 * @return the all
	 */
	protected static List<Object> getAll(final Number160 domain,
			final String key) {
		List<Object> ret = new ArrayList<Object>();

		try {
			final Number160 locationKey = Number160.createHash(key);
			final FutureGet future = peer.get(locationKey).domainKey(domain)
					.all().start().awaitUninterruptibly();
			Map<Number640, Data> data = future.dataMap();
			if (future.isSuccess() && data != null) {
				for (Data d : data.values()) {
					ret.add(d.object());
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Gets the.
	 *
	 * @param domain
	 *            the domain
	 * @param key
	 *            the key
	 * @return the object
	 */
	protected static Object get(final Number160 domain, final String key) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final FutureGet future = peer.get(locationKey).domainKey(domain)
					.start().awaitUninterruptibly();
			if (future.isSuccess()) {
				if (future.data() != null) {
					return future.data().object();
				}
			} else {
				System.out.println(future.failedReason());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Store.
	 *
	 * @param domain
	 *            the domain
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	protected static void set(final Number160 domain, final String key,
			final Object value) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final FuturePut future = peer.put(locationKey).domainKey(domain)
					.data(new Data(value)).start().awaitUninterruptibly();

			if (!future.isSuccess()) {
				System.out.println(future.failedReason());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds the.
	 *
	 * @param domain
	 *            the domain
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	protected static void add(final Number160 domain, final String key,
			final Object value) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final FuturePut future = peer.add(locationKey).domainKey(domain)
					.list(true).data(new Data(value)).start()
					.awaitUninterruptibly();
			if (!future.isSuccess()) {
				System.out.println(future.failedReason());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Store.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	/*
	 * protected static void replace(String key, Object value) {
	 * 
	 * Number160 hash = Number160.createHash(key); try { peer.remove(hash);
	 * peer.put(hash).setData(new Data(value).setTTLSeconds(30)).start()
	 * .awaitUninterruptibly(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } }
	 */
}
