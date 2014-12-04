package ist.p2p.service;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageGeneric;

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
	static protected Peer peer = null;
	
	static protected StorageGeneric storage = null;

	
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
			final int myPeerPort, Number160 myPeerId, boolean isBootStrap) throws IOException {

		
		Bindings bindings=new Bindings();
		PeerMaker pm =new PeerMaker(myPeerId).setPorts(myPeerPort).setBindings(bindings)
				.setEnableIndirectReplication(true);
		storage = pm.getStorage();
		peer = pm.makeAndListen();
		peer.getConfiguration().setBehindFirewall(true);

		final InetAddress address = Inet4Address.getByName(masterIp);

		// Future Discover
		{
			final FutureDiscover futureDiscover = peer.discover()
					.setInetAddress(address).setPorts(masterPort).start();
			futureDiscover.awaitUninterruptibly();

			if (!futureDiscover.isSuccess()) {
				System.out
						.println("Discover with direct connection failed. Reason = "
								+ futureDiscover.getFailedReason());
				peer.shutdown();
			}
		}
		
		
		if (!isBootStrap) {
			// Future Bootstrap - slave
			{
				final FutureBootstrap futureBootstrap = peer.bootstrap()
						.setInetAddress(address).setPorts(masterPort).start();
				futureBootstrap.awaitUninterruptibly();

				if (!futureBootstrap.isSuccess()) {
					System.out
							.println("Bootstrap with direct connection failed. Reason = "
									+ futureBootstrap.getFailedReason());
					peer.shutdown();
				}
			}
		}


		

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
			final FutureDHT future = peer.get(locationKey).setDomainKey(domain)
					.setAll(true).start().awaitUninterruptibly();
			Map<Number160, Data> data = future.getDataMap();
			if (future.isSuccess() && data != null) {
				for (Data d : data.values()) {
					ret.add(d.getObject());
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
			final FutureDHT future = peer.get(locationKey).setDomainKey(domain)
					.start().awaitUninterruptibly();
			if (future.isSuccess()) {
				if (future.getData() != null) {
					return future.getData().getObject();
				}
			} else {
				System.out.println(future.getFailedReason());
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
			final FutureDHT future = peer.put(locationKey).setDomainKey(domain)
					.setData(new Data(value)).start().awaitUninterruptibly();

			if (!future.isSuccess()) {
				System.out.println(future.getFailedReason());
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
			final FutureDHT future = peer.add(locationKey).setDomainKey(domain)
					.setList(true).setData(new Data(value)).start()
					.awaitUninterruptibly();
			if (!future.isSuccess()) {
				System.out.println(future.getFailedReason());
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
