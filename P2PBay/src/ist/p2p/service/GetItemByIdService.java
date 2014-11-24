package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;
import ist.p2p.dto.ItemDto;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class GetItemByIdService extends P2PBayService {

	/** The search. */
	private String id;

	/** The item. */
	private ItemDto item;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param id
	 *            the id
	 */
	public GetItemByIdService(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		final Item concreteItem = (Item) get(DOMAIN_ITEM, id);
		final List<Object> itemBids = getAll(DOMAIN_ITEM_BIDS, id);
		if (concreteItem != null) {
			item = new ItemDto(id, concreteItem.getOwner(),
					concreteItem.getTitle(), concreteItem.getDescription());

			for (Object obj : itemBids) {
				final BidDto bid = (BidDto) get(DOMAIN_BID, (String) obj);
				item.getBids().add(bid);
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public ItemDto getItem() {
		return item;
	}

}
