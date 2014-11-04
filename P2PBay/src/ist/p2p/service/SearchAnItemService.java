package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.ItemDto;
import ist.p2p.logic.LogicNode;
import ist.p2p.logic.LogicString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

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

	private void rec(HashSet<String> tokens, LogicNode node) {
		if (node instanceof LogicString) {
			tokens.add(node.toString());
		}
		for (LogicNode n : node.getArgs()) {
			rec(tokens, n);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		items = new ArrayList<ItemDto>();

		final HashSet<String> neededTokens = new HashSet<String>();
		final LogicNode rootNode = LogicNode.extractFromString(search);
		rec(neededTokens, rootNode);

		final TreeMap<String, List<String>> temp = new TreeMap<String, List<String>>(); // <fileId,
																					// tokens>
		for (String token : neededTokens) {
			final List<String> indexs = (List<String>) get(DOMAIN_WORD , token);
			if (indexs != null) {
				for (String f : indexs) {
					List<String> ftokens = temp.get(f);
					if (ftokens == null) {
						ftokens = new ArrayList<String>();
						temp.put(f, ftokens);
					}
					ftokens.add(token);
				}
			}
		}

		for (Entry<String, List<String>> fileTokens : temp.entrySet()) {
			if (rootNode.check(fileTokens.getValue())) {
				final Item product = (Item) get(DOMAIN_ITEM , fileTokens.getKey());
				if (product != null) {
					items.add(new ItemDto(fileTokens.getKey(), product
							.getOwner(), product.getTitle(), product
							.getDescription()));
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
