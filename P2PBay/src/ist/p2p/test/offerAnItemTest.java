package ist.p2p.test;

import ist.p2p.service.OfferAnItemForSaleService;

public class offerAnItemTest extends p2pServiceTestCase {

	/** The Constant CLIENT_USERNAME. */
	private static final String CLIENT_OWNER = "admin";

	private static final String TITLE = "Stuff title";

	private static final String DESCRIPTION = "Stuff Description";

	public offerAnItemTest() {
		super();
	}

	public offerAnItemTest(final String msg) {
		super(msg);
	}

	@Override
	public final void setUp() throws Exception {
		super.setUp();
	}

	public final void testOfferAnItem() {

		serviceMaster.execute();
		{
		final OfferAnItemForSaleService offerAnItemForSaleService = new OfferAnItemForSaleService(
				CLIENT_OWNER, TITLE, DESCRIPTION);
		assertTrue("Adicao do item",offerAnItemForSaleService.execute());
		}
		
	}

	@Override
	public final void tearDown() throws Exception {
		super.tearDown();
	}

}
