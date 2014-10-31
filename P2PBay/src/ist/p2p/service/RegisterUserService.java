package ist.p2p.service;

import ist.p2p.P2PBay;
import ist.p2p.Utils;

import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class RegisterUserService.
 */
public class RegisterUserService implements P2PBayService<Boolean> {

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/**
	 * Instantiates a new register user service.
	 *
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public RegisterUserService(String username, String password) {
		this.username = username;
		this.password = password;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public Boolean execute() {
		String salt = Utils.randomString(16);
		String hash = Utils.sha1(salt + password);
		JSONObject obj = new JSONObject();
		obj.put("salt", salt);
		obj.put("hash", hash);
		P2PBay.store("user:" + username, obj.toString());
		return true;
	}

}
