package ist.p2p.service;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.AddBuilder;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.GetBuilder;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.dht.PutBuilder;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
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
	public static final String DOMAIN_BID =  "bid";
	public static final String DOMAIN_USER_BIDS = "user_bids";
	public static final String DOMAIN_ITEM_BIDS = "item_bids";
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


		PeerBuilder builder = new PeerBuilder(new Number160(random)).ports(myPeerPort).bindings(bindings).behindFirewall(true);
		
	
		
		
		 peer = new PeerBuilderDHT(builder.start()).start();
		 new IndirectReplication(peer).autoReplication(true).start();

	
		
		
		if (ip != null) {
			InetAddress address = Inet4Address.getByName(ip);
			// Future Discover
			FutureDiscover futureDiscover = peer.peer().discover().inetAddress(address).ports(port).start();
			futureDiscover.awaitUninterruptibly();
		
			// Future Bootstrap - slave
			FutureBootstrap futureBootstrap = peer.peer().bootstrap().inetAddress(address).ports(port).start();
			futureBootstrap.awaitUninterruptibly();
			
			
		}

		System.out.println("*** PORT " + myPeerPort + " ***");
	}

	
	protected static List<Object> getAll(final String domain, final String key) {
		List<Object> ret = new ArrayList<Object>();
		
		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final GetBuilder builder = peer.get(locationKey).domainKey(domainKey).all();
			final FutureGet futureDHT = builder.start();
			futureDHT.awaitUninterruptibly();
			
			
			if(futureDHT.isSuccess() && futureDHT.data()!=null){
				for(Data d : futureDHT.dataMap().values()){
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
			final GetBuilder builder = peer.get(locationKey).domainKey(domainKey);
			final FutureGet futureDHT = builder.start();
			futureDHT.awaitUninterruptibly();
			
			
			if(futureDHT.isSuccess() && futureDHT.data()!=null){
				return futureDHT.data().object();
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
	@SuppressWarnings("unused")
	protected static void put(final String domain, final String key, final Object value) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final PutBuilder builder = peer.put(locationKey).domainKey(domainKey).object(value);
			final FuturePut futureDHT = builder.start().awaitUninterruptibly();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	@SuppressWarnings("unused")
	protected static void add(final String domain, final String key, final Object value) {
		try {
			final Number160 locationKey = Number160.createHash(key);
			final Number160 domainKey = Number160.createHash(domain);
			final AddBuilder builder = peer.add(locationKey).domainKey(domainKey).list(true).object(value);
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
