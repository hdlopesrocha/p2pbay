package ist.p2p.test;

import ist.p2p.dto.ItemDto;
import ist.p2p.service.AcceptBidService;
import ist.p2p.service.BidAnItemService;
import ist.p2p.service.OfferAnItemForSaleService;
import ist.p2p.service.SearchAnItemService;

public class AcceptBidTest extends p2pServiceTestCase {

	/** The Constant CLIENT_USERNAME. */
	private static final String CLIENT_OWNER = "admin";

	private static final String CLIENT_USER_BID = "user1";

	private static final String TITLE = "Stuff title";
	
	private static final String TITLE_SEARCH = "(and Stuff title)";

	private static final String DESCRIPTION = "Stuff Description";

	private static final float AMOUNT = 14.99f;
	
	private static boolean initialize = true;

	public AcceptBidTest() {
		super();
	}

	public AcceptBidTest(final String msg) {
		super(msg);
	}

	@Override
	public final void setUp() throws Exception {
		init();
		super.setUp();
	}
	
	 public void init() {
	     if(!initialize) return;
	     initialize = false;

	     serviceMaster.execute();
	     {
	    	 final OfferAnItemForSaleService offerAnItemForSaleService = new OfferAnItemForSaleService(
	    			 CLIENT_OWNER, TITLE, DESCRIPTION);
	    	 assertTrue("Oferta de um item",offerAnItemForSaleService.execute());
	     }
	     servicePeer.execute();
	 }

	public final void testAcceptBid() {

		String id = "";

		{
			final SearchAnItemService searchAnItemService = new SearchAnItemService(TITLE_SEARCH);
			boolean search = searchAnItemService.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertFalse("Nao encontrou o item",id.equals(""));
			
			final BidAnItemService bidAnItemService = new BidAnItemService(
					CLIENT_USER_BID, id, AMOUNT);
			assertTrue("Oferta de uma licitacao",bidAnItemService.execute());
			
			final AcceptBidService acceptBidService = new AcceptBidService(
					CLIENT_OWNER, id);
			assertTrue("Aceitar a licitacao",acceptBidService.execute());
		}

	}

	@Override
	public final void tearDown() throws Exception {
		super.tearDown();
	}

}
