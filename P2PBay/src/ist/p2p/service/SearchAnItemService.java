package ist.p2p.service;

import ist.p2p.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class SearchAnItemService extends P2PBayService<List<ItemDto>> {

	/** The search. */
	private String search;

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
	public List<ItemDto> execute() {
		final List<ItemDto> items = new ArrayList<ItemDto>();
		@SuppressWarnings("unchecked")
		final List<String> indexs  = (List<String>) get("index:" + search);
		if (indexs != null) {
			for (String key : indexs) {
				final ItemDto product = (ItemDto) get(key);
				if (product != null) {
					items.add(product);
				}
			}
		}
		return items;
	}

}
