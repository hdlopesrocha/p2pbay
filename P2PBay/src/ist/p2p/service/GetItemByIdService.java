package ist.p2p.service;

import ist.p2p.domain.Item;
import ist.p2p.dto.BidDto;
import ist.p2p.dto.ItemDto;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class GetItemByIdService extends P2PBayService {

	/** The search. */
	private String id;
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
		final Item concreteItem = (Item) get("item" , id);
		item = new ItemDto(id,concreteItem.getOwner(), concreteItem.getTitle(),
				concreteItem.getDescription());
		for (String str : concreteItem.getBids().values()) {
			final BidDto bid = (BidDto) get("bid" , str);
			item.getBids().add(bid);
		}

		return true;
	}

	public ItemDto getItem(){
		return item;
	}
	
}
