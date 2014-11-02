package ist.p2p.dto;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class UserDto.
 */
public class UserDto {

	/** The bids. */
	private List<String> bids;

	/** The bids. */
	private List<String> purchases;

	/**
	 * Instantiates a new user dto.
	 */
	public UserDto() {
		bids = new ArrayList<String>();
		purchases = new ArrayList<String>();
	}

	/**
	 * Gets the bids.
	 *
	 * @return the bids
	 */
	public List<String> getBids() {
		return bids;
	}

	/**
	 * Gets the bids.
	 *
	 * @return the bids
	 */
	public List<String> getPurchases() {
		return purchases;
	}
}
