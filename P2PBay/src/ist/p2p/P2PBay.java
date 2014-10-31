package ist.p2p;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class P2PBay {
	/** The peer. */
	static private Peer peer = null;
	
	
	public static void connect(String [] args) throws IOException{
		Random random = new Random();
		int myPeerPort = !Utils.argExists("-i", args) ? 1024 : 1025 + random
				.nextInt(60000);

		Bindings bindings = new Bindings();
		bindings.addInterface("eth0");
		bindings.addInterface("wlan0");
		bindings.addInterface("lo");

		peer = new PeerMaker(new Number160(random)).setPorts(myPeerPort)
				.setBindings(bindings).setEnableIndirectReplication(true)
				.makeAndListen();
		peer.getConfiguration().setBehindFirewall(true);

		if (Utils.argExists("-i", args)) {
			String[] knownPeerIpPort = Utils.getArgValue("-i", args).split(":");
			int knownPeerPort = Integer.valueOf(knownPeerIpPort[1]);
			String knownPeerIp = knownPeerIpPort[0];
			InetAddress address = Inet4Address.getByName(knownPeerIp);
			FutureDiscover futureDiscover = peer.discover()
					.setInetAddress(address).setPorts(knownPeerPort).start();
			futureDiscover.awaitUninterruptibly();
		}
	}
	
	/**
	 * Gets the.
	 *
	 * @param key
	 *            the key
	 * @return the object
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Object get(String key) {
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
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void store(String key, Object value) {

		try {
			peer.put(Number160.createHash(key))
					.setData(new Data(value).setTTLSeconds(30)).start()
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
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void replace(String key, Object value) {

		Number160 hash = Number160.createHash(key);
		try {
			peer.remove(hash);
			peer.put(hash)
				.setData(new Data(value).setTTLSeconds(30)).start()
				.awaitUninterruptibly();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}