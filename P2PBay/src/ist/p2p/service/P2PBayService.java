package ist.p2p.service;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
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
public abstract class P2PBayService<T> {

	/**
	 * Execute.
	 *
	 * @return the t
	 */
	public abstract T execute();

	/** The peer. */
	static protected Peer peer = null;

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

		peer = new PeerMaker(new Number160(random)).setPorts(myPeerPort)
				.setBindings(bindings).setEnableIndirectReplication(true)
				.makeAndListen();
		peer.getConfiguration().setBehindFirewall(true);

		if (ip != null) {
			InetAddress address = Inet4Address.getByName(ip);
			FutureDiscover futureDiscover = peer.discover()
					.setInetAddress(address).setPorts(port).start();
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
	protected static Object get(String key) {
		try {
			FutureDHT futureDHT = peer.get(Number160.createHash(key)).start();
			futureDHT.awaitUninterruptibly();
			if (futureDHT.isSuccess()) {
				return futureDHT.getData().getObject();
			}
		} catch (IOException | ClassNotFoundException e) {
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
	protected static void put(String key, Object value) {

		try {
			PutBuilder builder = peer.put(Number160.createHash(key));
			builder.setData(new Data(value).setTTLSeconds(30)).start()
					.awaitUninterruptibly();
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

}
