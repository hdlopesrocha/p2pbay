package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;
import ist.p2p.dto.PurchaseDto;

import java.util.List;
import java.util.TreeMap;

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
	@SuppressWarnings("unchecked")
	@Override
	public boolean execute() {
		final Item item = (Item) get(DOMAIN_ITEM, id);
		final TreeMap<Float,String> itemBids = (TreeMap<Float, String>) get(DOMAIN_ITEM_BIDS, id);
		
		
		if (item != null && !item.isClosed() && item.getOwner().equals(username) && itemBids != null && itemBids.size()>0) {

			String lastBidId = itemBids.lastEntry().getValue();
			BidDto bid = (BidDto) get(DOMAIN_BID , lastBidId);

			List<PurchaseDto> userBuys = (List<PurchaseDto>) get(DOMAIN_PURCHASES , bid.getUser());
			userBuys.add(new PurchaseDto(id, item.getTitle(), item.getDescription(), bid.getOffer()));
			put(DOMAIN_PURCHASES , bid.getUser(),userBuys);
			
			item.close();
			put(DOMAIN_ITEM, id,item);
			
			return true;
		}

		return false;
	}

}
