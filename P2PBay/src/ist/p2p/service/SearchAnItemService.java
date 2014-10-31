package ist.p2p.service;

import ist.p2p.P2PBay;
import ist.p2p.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class SearchAnItemService implements P2PBayService<List<ItemDto>> {

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
		List<ItemDto> items = new ArrayList<ItemDto>();

		String res = (String) P2PBay.get("index:" + search);
		if (res != null) {
			JSONArray array = new JSONArray(res);
			for (int i = 0; i < array.length(); ++i) {
				String product = (String) P2PBay.get(array.getString(i));
				if (product != null) {
					items.add(new ItemDto(new JSONObject(product)));
				}
			}
		}
		return items;
	}

}
