package ist.p2p.service;

import ist.p2p.domain.Item;

import java.util.HashSet;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class OfferAnItemForSaleService.
 */
public class OfferAnItemForSaleService extends P2PBayService {

	private String username;
	private String title;
	private String description;

	public OfferAnItemForSaleService(String username, String title,
			String description) {
		this.username = username;
		this.title = title;
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		if (title != null && description != null
				&& !title.isEmpty()
				&& !description.isEmpty()) {
			Item item = new Item(username, title, description);
			String itemId = UUID.randomUUID().toString();
			set(DOMAIN_ITEM , itemId, item);			
			
			final HashSet<String> uniqueTokens = new HashSet<String>();
			for (String token : title.split(" ")) {
				uniqueTokens.add(token);
			}

			for (String token : uniqueTokens) {
				add(DOMAIN_WORD, token, itemId);
			}
			Gossip.addItemOnSale(1);

			return true;
		}
		return false;
	}

}
