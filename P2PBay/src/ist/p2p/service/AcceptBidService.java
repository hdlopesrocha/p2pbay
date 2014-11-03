package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class AcceptBidService extends P2PBayService {

	/** The search. */
	private String id;

	/** The username. */
	private String username;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param username
	 *            the username
	 * @param id
	 *            the id
	 */
	public AcceptBidService(String username, String id) {
		this.id = id;
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		final Item item = (Item) get("item:" + id);
		if (item != null && item.getOwner().equals(username) && item.getBids().size()>0) {

			String lastBidId = item.getBids().lastEntry().getValue();
			BidDto bid = (BidDto) get("bid:" + lastBidId);

			List<String> userBuys = (List<String>) get("buys:" + bid.getUser());
			userBuys.add(id);
			put("buys:" + bid.getUser(),userBuys);
			
			return true;
		}

		return false;
	}

}
