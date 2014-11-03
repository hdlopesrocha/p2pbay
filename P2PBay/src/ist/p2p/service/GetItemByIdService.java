package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;
import ist.p2p.dto.ItemDto;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class GetItemByIdService extends P2PBayService<ItemDto> {

	/** The search. */
	private String id;

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
	public ItemDto execute() {
		final Item item = (Item) get("item:" + id);
		final ItemDto ret = new ItemDto(item.getOwner(), item.getTitle(),
				item.getDescription());
		for (String str : item.getBids().values()) {
			final BidDto bid = (BidDto) get("bid:" + str);
			ret.getBids().add(bid);
		}

		return ret;
	}

}
