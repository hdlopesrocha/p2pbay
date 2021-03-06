package ist.p2p.service;

import ist.p2p.Utils;
import ist.p2p.domain.Authentication;

// TODO: Auto-generated Javadoc
/**
 * The Class RegisterUserService.
 */
public class RegisterUserService extends P2PBayService {

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
	public boolean execute() {
		final String salt = Utils.randomString(16);
		final String hash = Utils.sha1(salt + password);
		set(DOMAIN_AUTH, username, new Authentication(salt, hash));
		return true;
	}

}
