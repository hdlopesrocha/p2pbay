package ist.p2p;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils {

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
	public static String randomString(final int size) {
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
	public static String sha1(String input) {
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
}
