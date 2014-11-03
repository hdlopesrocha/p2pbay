package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;
import ist.p2p.dto.HistoryDto;

import java.util.UUID;

/**
 * The Class SearchItemService.
 */
public class BidAnItemService extends P2PBayService<Boolean> {

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
	public Boolean execute() {
		final Item item = (Item) get("item:" + id);
		if (item != null) {
			final String bidKey = UUID.randomUUID().toString();
			final BidDto bid = new BidDto(offer,username, item.getId());
			put("bid:"+bidKey,bid);
			
			HistoryDto profile = (HistoryDto) get("profile:" + username);
			if (profile == null) {
				profile = new HistoryDto();
			} 
			
			profile.getBids().add(bidKey);
			put("hist:" + username, profile);

			/* add to item bids */
			item.getBids().put(bid.getOffer(),bidKey);
			put("item:" + item.getId(), item);
			return true;
		}
		return false;
	}

}
