package ist.p2p.service;

import ist.p2p.dto.BidDto;
import ist.p2p.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;
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
		final ItemDto item = (ItemDto) get("item:" + id);
		if (item != null) {
			final String bidKey = "bid:"+ UUID.randomUUID().toString();
			final BidDto bid = new BidDto(offer,username, item.getId());
			put("bid:"+bidKey,bid);
			
			
			/* add to user bids */
			List<String> userBids = (List<String>) get("userBids:" + username);
			if (userBids == null) {
				userBids = new ArrayList<String>();
			}
			userBids.add(bidKey);
			put("userBids:" + username, userBids);

			/* add to item bids */
			List<String> itemBids = (List<String>) get("itemBids:"+ item.getId());
			if (itemBids == null) {
				itemBids = new ArrayList<String>();
			}
			itemBids.add(bidKey);
			put("itemBids:" + item.getId(), itemBids);
			return true;

		}
		return false;
	}

}
