package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class SearchAnItemService extends P2PBayService {

	/** The search. */
	private String search;

	/** The items. */
	private List<ItemDto> items;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param search
	 *            the search
	 */
	public SearchAnItemService(String search) {
		this.search = search;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		items = new ArrayList<ItemDto>();
		@SuppressWarnings("unchecked")
		final List<String> indexs = (List<String>) get("index:" + search);
		if (indexs != null) {
			for (String id : indexs) {
				final Item product = (Item) get("item:" + id);
				if (product != null) {
					items.add(new ItemDto(id,product.getOwner(), product.getTitle(), product.getDescription()));
				}
			}
		}
		return true;
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public List<ItemDto> getItems() {
		return items;
	}

}
