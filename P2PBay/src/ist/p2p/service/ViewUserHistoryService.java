package ist.p2p.service;

import ist.p2p.dto.BidDto;
import ist.p2p.dto.PurchaseDto;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class ViewUserHistoryService extends P2PBayService {

	/** The search. */
	private String username;

	
	/** The bids. */
	private List<BidDto> bids;

	/** The bids. */
	private List<PurchaseDto> purchases;

	/**
	 * Gets the bids.
	 *
	 * @return the bids
	 */
	public List<BidDto> getBids() {
		return bids;
	}

	/**
	 * Gets the bids.
	 *
	 * @return the bids
	 */
	public List<PurchaseDto> getPurchases() {
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
	@SuppressWarnings("unchecked")
	@Override
	public boolean execute() {
		List<Object> bidIds = getAll(DOMAIN_USER_BIDS , username);
		
		bids = new ArrayList<BidDto>();
		for(Object bidId: bidIds){
			BidDto bid =(BidDto) get(DOMAIN_BID, (String) bidId);
			if(bid!=null){
				bids.add(bid);
			}
		}
		
		purchases  = (List<PurchaseDto>) get(DOMAIN_PURCHASES , username);		

		
		
		return true;
	}

}
