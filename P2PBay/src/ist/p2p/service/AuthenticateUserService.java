package ist.p2p.service;

import ist.p2p.P2PBay;
import ist.p2p.Utils;

import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthenticateUserService.
 */
public class AuthenticateUserService implements P2PBayService<Boolean> {

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/**
	 * Instantiates a new authenticate user service.
	 *
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public AuthenticateUserService(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.P2PBayService#execute()
	 */
	@Override
	public Boolean execute() {
		String saltPlusHash = (String) P2PBay.get("user:" + username);
		if (saltPlusHash != null) {
			JSONObject obj = new JSONObject(saltPlusHash);
			String salt = obj.getString("salt");
			String hash = obj.getString("hash");
			if (Utils.sha1(salt + password).equals(hash)) {
				return true;
			}
		}
		return false;
	}

}
