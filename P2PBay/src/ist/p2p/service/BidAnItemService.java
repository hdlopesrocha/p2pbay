package ist.p2p.service;

import ist.p2p.dto.BidDto;
import ist.p2p.dto.ItemDto;
import ist.p2p.dto.UserDto;

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
			
			UserDto profile = (UserDto) get("profile:" + username);
			if (profile == null) {
				profile = new UserDto();
			} 
			
			profile.getBids().add(bidKey);
			put("profile:" + username, profile);

			/* add to item bids */
			item.getBids().add(bidKey);
			put("item:" + item.getId(), item);
			return true;
		}
		return false;
	}

}
