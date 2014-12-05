package ist.p2p.test;

import ist.p2p.dto.ItemDto;
import ist.p2p.service.OfferAnItemForSaleService;
import ist.p2p.service.SearchAnItemService;

public class offerAnItemTest extends p2pServiceTestCase {

	/** The Constant CLIENT_USERNAME. */
	private static final String CLIENT_OWNER = "admin";

	private static final String TITLE = "Stuff title";

	private static final String DESCRIPTION = "Stuff Description";
	
	private static final String TITLE_SEARCH = "(and Stuff title)";

	private static boolean initialize = true;

	public offerAnItemTest() {
		super();
	}

	public offerAnItemTest(final String msg) {
		super(msg);
	}

	@Override
	public final void setUp() throws Exception {
		init();
		super.setUp();
	}

	public void init() {
		if (!initialize)
			return;
		initialize = false;

		serviceMaster.execute();
		{
			final OfferAnItemForSaleService offerAnItemForSaleService = new OfferAnItemForSaleService(
					CLIENT_OWNER, TITLE, DESCRIPTION);
			assertTrue("Oferta de um item", offerAnItemForSaleService.execute());
		}
		servicePeer.execute();
	}

	public final void testOfferAnItem() {

		String id = "";

		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(
					TITLE_SEARCH);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item", search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertFalse("Nao encontrou o item", id.equals(""));

		}

	}

	@Override
	public final void tearDown() throws Exception {
		super.tearDown();
	}

}
