package ist.p2p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONObject;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class P2PBay.
 */
public class P2PBay {

	/** The peer. */
	static private Peer peer = null;

	/** The Constant random. */
	private static final Random RANDOM = new Random();

	/** The Constant chars. */
	private static final String CHARS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";

	/**
	 * Random string.
	 *
	 * @param size
	 *            the size
	 * @return the string
	 */
	private static String randomString(final int size) {
		String str = "";
		int charsLen = CHARS.length();
		for (int i = 0; i < size; i++) {
			str += CHARS.charAt(RANDOM.nextInt(charsLen));
		}
		return str;
	}

	/**
	 * Sha1.
	 *
	 * @param input
	 *            the input
	 * @return the string
	 */
	static String sha1(String input) {
		StringBuffer sb = new StringBuffer();

		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(input.getBytes());
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Arg exists.
	 *
	 * @param arg
	 *            the arg
	 * @param args
	 *            the args
	 * @return true, if successful
	 */
	public static boolean argExists(String arg, String[] args) {
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals(arg)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the arg value.
	 *
	 * @param arg
	 *            the arg
	 * @param args
	 *            the args
	 * @return the arg value
	 */
	public static String getArgValue(String arg, String[] args) {
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals(arg) && i + 1 < args.length) {
				return args[i + 1];
			}
		}
		return null;
	}

	/**
	 * Load users file.
	 *
	 * @param filename
	 *            the filename
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void loadUsersFile(String filename) throws IOException {
		File file = new File(filename);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";

		while ((line = reader.readLine()) != null) {
			String[] splits = line.split(":");
			String salt = randomString(16);
			String hash = sha1(salt + splits[1]);
			JSONObject obj = new JSONObject();
			obj.put("salt", salt);
			obj.put("hash", hash);
			store("user:" + splits[0], obj.toString());
		}

		reader.close();

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
	private static Object get(String key) throws ClassNotFoundException,
			IOException {
		FutureDHT futureDHT = peer.get(Number160.createHash(key)).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return futureDHT.getData().getObject();
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
	private static void store(String key, Object value) throws IOException {
		peer.put(Number160.createHash(key))
				.setData(new Data(value).setTTLSeconds(30)).start()
				.awaitUninterruptibly();
	}

	// -i ip:port of known peer (bootstrap if not exists)
	// -u loads users file

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		String usersFileName = getArgValue("-u", args);

		Random random = new Random();
		int myPeerPort = !argExists("-i", args) ? 1024 : 1025 + random
				.nextInt(60000);

		Bindings bindings = new Bindings();
		bindings.addInterface("eth0");
		bindings.addInterface("wlan0");
		bindings.addInterface("lo");

		peer = new PeerMaker(new Number160(random)).setPorts(myPeerPort)
				.setBindings(bindings).setEnableIndirectReplication(true)
				.makeAndListen();
		peer.getConfiguration().setBehindFirewall(true);

		if (argExists("-i", args)) {
			String[] knownPeerIpPort = getArgValue("-i", args).split(":");
			int knownPeerPort = Integer.valueOf(knownPeerIpPort[1]);
			String knownPeerIp = knownPeerIpPort[0];
			InetAddress address = Inet4Address.getByName(knownPeerIp);
			FutureDiscover futureDiscover = peer.discover()
					.setInetAddress(address).setPorts(knownPeerPort).start();
			futureDiscover.awaitUninterruptibly();
		}

		if (usersFileName != null) {
			loadUsersFile(usersFileName);
		}

		System.out.println("***********************");
		commandLine();
	}

	/**
	 * Command line.
	 */
	public static void commandLine() {
		Scanner scanner = new Scanner(System.in);

		while (true) {

			System.out.print("Username: ");
			String username = scanner.nextLine();

			System.out.print("Password: ");
			String password = scanner.nextLine();

			try {
				String saltPlusHash = (String) get("user:" + username);
				if (saltPlusHash != null) {
					JSONObject obj = new JSONObject(saltPlusHash);
					String salt = obj.getString("salt");
					String hash = obj.getString("hash");

					if (sha1(salt + password).equals(hash)) {
						break;
					}
				}
				
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			System.err.println("Authentication failed!");
			
		}
		System.out.println("Authentication Succeded!");

		while (true) {
			System.out.println("\t1) offer an item for sale");
			System.out.println("\t2) accept a bid");
			System.out.println("\t3) search for an item to buy");
			System.out.println("\t4) bid on an item");
			System.out.println("\t5) view the details of an item");
			System.out.println("\t6) view purchase and bidding history");
			System.out.println("\t0) quit");
			System.out.print("\toption: ");
			int option = scanner.nextInt();
			if (option == 1) {

			} else if (option == 2) {

			} else if (option == 3) {

			} else if (option == 4) {

			} else if (option == 5) {

			} else if (option == 6) {

			} else if (option == 0) {
				break;
			} else {
				System.out.println("Option not found!");
			}
		}

		System.out.println("See you next time!");
		scanner.close();
	}

}
