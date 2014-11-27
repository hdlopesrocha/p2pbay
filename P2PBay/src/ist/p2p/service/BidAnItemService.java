package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class BidAnItemService extends P2PBayService {

	/** The search. */
	private String id;

	/** The offer. */
	private float offer;

	/** The username. */
	private String username;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param username
	 *            the username
	 * @param id
	 *            the id
	 * @param offer
	 *            the offer
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
		final Item item = (Item) get(DOMAIN_ITEM, id);

		if (item != null && !item.isClosed()) {
			final String bidKey = UUID.randomUUID().toString();
			final BidDto bid = new BidDto(offer, username, id);
			set(DOMAIN_BID, bidKey, bid);

			/* add to item userBids */
			add(DOMAIN_USER_BIDS, username, bidKey);

			/* add to item bids */
			add(DOMAIN_ITEM_BIDS, id, bidKey);
			return true;
		}
		return false;
	}

}
