package ist.p2p.service;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureGet;
import net.tomp2p.futures.FuturePut;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.AddBuilder;
import net.tomp2p.p2p.builder.GetBuilder;
import net.tomp2p.p2p.builder.PutBuilder;
import net.tomp2p.peers.Number160;
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
	static protected Peer peer = null;

	public static final String DOMAIN_AUTH = "auth";
	public static final String DOMAIN_ITEM = "item";
	public static final String DOMAIN_BID =  "bid";
	public static final String DOMAIN_BIDS = "bids";
	public static final String DOMAIN_PURCHASES = "purchases";
	public static final String DOMAIN_WORD = "word";
	
	/**
	 * Connect.
	 *
	 * @param ip
	 *            the ip
	 * @param port
	 *            the port
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected static void connect(String ip, int port) throws IOException {
		Random random = new Random();
		int myPeerPort = ip == null ? 1024 : 1025 + random.nextInt(60000);

		Bindings bindings = new Bindings();
		bindings.addInterface("eth0");
		bindings.addInterface("wlan0");
		bindings.addInterface("lo");
		
		PeerMaker peerMaker = new PeerMaker(new Number160(random)).ports(myPeerPort).bindings(bindings).setEnableIndirectReplication(true).setBehindFirewall(true);
		peer = peerMaker.makeAndListen();
		
		// configuration = peer.getConfiguration();
		//configuration.setBehindFirewall(true);
		
		if (ip != null) {
			InetAddress address = Inet4Address.getByName(ip);
			FutureDiscover futureDiscover = peer.discover().inetSocketAddress(address,port).start();
			futureDiscover.awaitUninterruptibly();
		}
		System.out.println("*** PORT " + myPeerPort + " ***");
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
			final GetBuilder builder = peer.get(locationKey).setDomainKey(domainKey);
			final FutureGet futureDHT = builder.start();
			futureDHT.awaitUninterruptibly();
			if(futureDHT.isSuccess()){
				return futureDHT.getData().object();
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
	protected static void put(final String domain, final String key, final Object value) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final PutBuilder builder = peer.put(locationKey).setDomainKey(domainKey).setData(new Data(value));
			final FuturePut futureDHT = builder.start().awaitUninterruptibly();
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
	protected static void replace(String key, Object value) {

		Number160 hash = Number160.createHash(key);
		try {
			peer.remove(hash);
			peer.put(hash).setData(new Data(value).setTTLSeconds(30)).start()
					.awaitUninterruptibly();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
}
