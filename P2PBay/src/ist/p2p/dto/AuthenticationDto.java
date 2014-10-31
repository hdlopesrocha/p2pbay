package ist.p2p.dto;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthenticationDto.
 */
public class AuthenticationDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6254705903420169696L;

	/** The salt. */
	private String salt;

	/** The hash. */
	private String hash;

	/**
	 * Instantiates a new authentication dto.
	 */
	public AuthenticationDto() {
		super();
	}

	/**
	 * Instantiates a new authentication dto.
	 *
	 * @param salt
	 *            the salt
	 * @param hash
	 *            the hash
	 */
	public AuthenticationDto(String salt, String hash) {
		super();
		this.salt = salt;
		this.hash = hash;
	}

	/**
	 * Gets the salt.
	 *
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

}
