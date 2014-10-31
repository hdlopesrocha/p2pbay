package ist.p2p.service;

import ist.p2p.dto.ItemDto;

/**
 * The Class SearchItemService.
 */
public class GetItemByIdService extends P2PBayService<ItemDto> {

	/** The search. */
	private String id;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param search
	 *            the search
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
		return (ItemDto) get("item:" + id);
	}

}
