package ist.p2p.service;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class ViewUserBidsService extends P2PBayService<List<String>> {

	/** The search. */
	private String username;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param username
	 *            the username
	 */
	public ViewUserBidsService(String username) {

		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public List<String> execute() {

		List<String> userBids = (List<String>) get("userBids:" + username);
		if (userBids != null) {
			return userBids;
		} else {
			return new ArrayList<String>();
		}

	}

}
