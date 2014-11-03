package ist.p2p.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class UserDto.
 */
public class HistoryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5806657813224236345L;

	/** The bids. */
	private List<String> bids;

	/** The bids. */
	private List<String> purchases;

	/**
	 * Instantiates a new user dto.
	 */
	public HistoryDto() {
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
