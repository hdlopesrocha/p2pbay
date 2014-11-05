package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

/**
 * The Class SearchItemService.
 */
public class BidAnItemService extends P2PBayService {

	/** The search. */
	private String id;
	private float offer;
	private String username;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param search
	 *            the search
	 */
	public BidAnItemService(String username, String id, float offer) {
		this.id = id;
		this.offer = offer;
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		final Item item = (Item) get("item", id);
		
		
		if (item != null && !item.isClosed()) {
			final String bidKey = UUID.randomUUID().toString();
			final BidDto bid = new BidDto(offer, username, id);
			put("bid", bidKey,bid);
			
			/* add to item userBids */
			add(DOMAIN_USER_BIDS , username, bidKey);

			/* add to item bids */
			add(DOMAIN_ITEM_BIDS,  id, bidKey);
			return true;
		}
		return false;
	}

}
