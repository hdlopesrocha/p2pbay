package ist.p2p.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.GetBuilder;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number640;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.replication.IndirectReplication;
import net.tomp2p.storage.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class P2PBayService.
 *
 * @param <T>
 *            the generic type
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

	public static final String DOMAIN_AUTH = "auth";
	public static final String DOMAIN_ITEM = "item";
	public static final String DOMAIN_BID = "bid";
	public static final String DOMAIN_USER_BIDS = "user_bids";
	public static final String DOMAIN_ITEM_BIDS = "item_bids";
	public static final String DOMAIN_PURCHASES = "purchases";
	public static final String DOMAIN_WORD = "word";
	public static final Random RANDOM = new Random();

	/**
	 * Connect.
	 *
	 * @param masterIp
	 *            the ip
	 * @param masterPort
	 *            the port
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected static void connect(final String masterIp, final int masterPort, final int myPeerPort, Number160 myPeerId)
			throws IOException {

		final Bindings bindings = new Bindings().addInterface("eth0").addInterface("wlan0").addInterface("lo");

		peer = new PeerBuilderDHT(new PeerBuilder(myPeerId).ports(myPeerPort).bindings(bindings).behindFirewall(true).start()).start();
		new IndirectReplication(peer).autoReplication(true).start();

		final PeerAddress address = new PeerAddress(Number160.ONE, masterIp, masterPort, masterPort);
		// Future Discover
		final FutureDiscover futureDiscover = peer.peer().discover().peerAddress(address).start();
		futureDiscover.awaitUninterruptibly();

		if (!futureDiscover.isSuccess()) {
			System.out.println("Discover with direct connection failed. Reason = "	+ futureDiscover.failedReason());
			peer.shutdown().awaitUninterruptibly();
		}

		// Future Bootstrap - slave
		final FutureBootstrap futureBootstrap = peer.peer().bootstrap().peerAddress(address).start();
		futureBootstrap.awaitUninterruptibly();

		if (!futureBootstrap.isSuccess()) {
			System.out.println("Bootstrap with direct connection failed. Reason = "	+ futureBootstrap.failedReason());
			peer.shutdown().awaitUninterruptibly();
		}
	

		
		
		System.out.println("*** PORT " + myPeerPort + " ***");
	}

	protected static List<Object> getAll(final String domain, final String key) {
		List<Object> ret = new ArrayList<Object>();

		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final FutureGet future = peer.get(locationKey).domainKey(domainKey)
					.all().start().awaitUninterruptibly();
			Map<Number640, Data> data = future.dataMap();
			if (future.isSuccess() &&  data!= null) {
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
	 * @param key
	 *            the key
	 * @return the object
	 */
	protected static Object get(final String domain, final String key) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final FutureGet future = peer.get(locationKey).domainKey(domainKey)
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
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	protected static void set(final String domain, final String key,
			final Object value) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final FuturePut future = peer.put(locationKey).domainKey(domainKey)
					.data(new Data(value)).start().awaitUninterruptibly();

			if (!future.isSuccess()) {
				System.out.println(future.failedReason());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void add(final String domain, final String key,
			final Object value) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final FuturePut future = peer.add(locationKey).domainKey(domainKey)
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
