package ist.p2p.service;

import ist.p2p.dto.ItemDto;

import java.util.UUID;

import org.json.JSONArray;

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

			String uuid = UUID.randomUUID().toString();
			String key = "product:" + uuid;
			store(key, item.toJSON().toString());
			for (String token : item.getTitle().split(" ")) {
				String existingIndex = (String) get("index:" + token);
				JSONArray array = existingIndex == null ? new JSONArray()
						: new JSONArray(existingIndex);
				array.put(key);
				replace("index:" + token, array.toString());
			}
			return true;
		}
		return false;
	}

}
