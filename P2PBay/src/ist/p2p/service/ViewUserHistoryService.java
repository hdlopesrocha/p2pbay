package ist.p2p.service;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class ViewUserHistoryService extends P2PBayService {

	/** The search. */
	private String username;

	
	/** The bids. */
	private List<String> bids;

	/** The bids. */
	private List<String> purchases;

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
	/**
	 * Instantiates a new search item service.
	 *
	 * @param username
	 *            the username
	 */
	public ViewUserHistoryService(String username) {

		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		bids = (List<String>) get("bids:" + username);
		purchases = (List<String>) get("buys:" + username);		
		return true;
	}

}
