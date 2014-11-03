package ist.p2p.service;

import ist.p2p.dto.HistoryDto;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchItemService.
 */
public class ViewUserHistoryService extends P2PBayService<HistoryDto> {

	/** The search. */
	private String username;

	/**
	 * Instantiates a new search item service.
	 *
	 * @param username
	 *            the username
	 */
	public ViewUserHistoryService(String username) {

		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public HistoryDto execute() {

		final HistoryDto profile = (HistoryDto) get("hist" + username);
		if (profile != null) {
			return profile;
		} else {
			return new HistoryDto();
		}

	}

}
