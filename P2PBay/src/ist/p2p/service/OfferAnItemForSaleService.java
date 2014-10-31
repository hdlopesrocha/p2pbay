package ist.p2p.service;

import ist.p2p.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class OfferAnItemForSaleService.
 */
public class OfferAnItemForSaleService extends P2PBayService<Boolean> {

	/** The item. */
	private ItemDto item;

	/**
	 * Instantiates a new offer an item for sale service.
	 *
	 * @param item
	 *            the item
	 */
	public OfferAnItemForSaleService(ItemDto item) {
		this.item = item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public Boolean execute() {
		if (item.getTitle() != null && item.getDescription() != null
				&& !item.getTitle().isEmpty()
				&& !item.getDescription().isEmpty()) {

			final String key = "item:" + item.getId();
			put(key, item);
			
			final HashSet<String> uniqueTokens = new HashSet<String>();
			for (String token : item.getTitle().split(" ")) {
				uniqueTokens.add(token);
			}
			
			for (String token : uniqueTokens) {
				@SuppressWarnings("unchecked")
				List<String> existingIndexs =  (List<String>) get("index:" + token);
				
				if(existingIndexs == null)
					existingIndexs = new ArrayList<String>();
				
				existingIndexs.add(key);
				put("index:" + token, existingIndexs);
			}
			return true;
		}
		return false;
	}

}
