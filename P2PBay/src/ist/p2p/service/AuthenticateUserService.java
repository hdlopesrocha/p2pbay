package ist.p2p.service;

import ist.p2p.Utils;
import ist.p2p.dto.AuthenticationDto;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthenticateUserService.
 */
public class AuthenticateUserService extends P2PBayService<Boolean> {

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
	public AuthenticateUserService(final String username, final String password) {
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
		final AuthenticationDto auth = (AuthenticationDto) get("user:" + username);
		if (auth != null) {
			final String salt = auth.getSalt();
			final String hash = auth.getHash();
			if (Utils.sha1(salt + password).equals(hash)) {
				return true;
			}
		}
		return false;
	}

}
