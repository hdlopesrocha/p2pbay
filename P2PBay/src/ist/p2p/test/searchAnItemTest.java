package ist.p2p.test;

import ist.p2p.dto.ItemDto;
import ist.p2p.service.OfferAnItemForSaleService;
import ist.p2p.service.SearchAnItemService;

public class searchAnItemTest extends p2pServiceTestCase{

	/** The Constant CLIENT_USERNAME. */
	private static final String CLIENT_OWNER = "admin";

	private static final String TITLE = "Stuff title";
	
	private static final String TITLE_SEARCH = "Stuff";
	
	private static final String TITLE_SEARCH_EMPTY = "other";
	
	private static final String TITLE_SEARCH_AND = "(and Stuff title)";
	
	private static final String TITLE_SEARCH_AND_EMPTY = "(and Stuff other)";
	
	private static final String TITLE_SEARCH_OR = "(or Stuff other)";
	
	private static final String TITLE_SEARCH_NOT = "(not Stuff)";
	
	private static final String TITLE_SEARCH_OR_NOT = "(or (not other) stuff)";
	
	private static final String TITLE_SEARCH_AND_NOT = "(and (not other) stuff)";

	private static final String DESCRIPTION = "Stuff Description";

	private static boolean initialize = true;

	public searchAnItemTest() {
		super();
	}

	public searchAnItemTest(final String msg) {
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

	public final void testSearch() {
		
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertFalse("Nao encontrou o item",id.equals(""));
			
		}
		
	}
	
	public final void testSearchEmpty() {
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH_EMPTY);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertTrue("Encontrou o item",id.equals(""));
			
		}
		
	}
	
	public final void testSearchAnd() {
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH_AND);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertFalse("Nao encontrou o item",id.equals(""));
			
		}
		
	}
	
	public final void testSearchAndEmpty() {
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH_AND_EMPTY);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertTrue("Nao encontrou o item",id.equals(""));
			
		}
		
	}
	
	public final void testSearchOr() {
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH_OR);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertFalse("Nao encontrou o item",id.equals(""));
			
		}
		
	}
	
	public final void testSearchNot() {
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH_NOT);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertTrue("Nao encontrou o item",id.equals(""));
			
		}
		
	}
	
	public final void testSearchOrNot() {
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH_OR_NOT);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertTrue("Nao encontrou o item",id.equals(""));
			
		}
		
	}
	
	public final void testSearchAndNot() {
		String id = "";
		
		{
			final SearchAnItemService searchAnItemService1 = new SearchAnItemService(TITLE_SEARCH_AND_NOT);
			boolean search = searchAnItemService1.execute();
			assertTrue("Procura do item",search);
			if (search) {
				for (ItemDto item : searchAnItemService1.getItems()) {
					if (item.getTitle().equals(TITLE)) {
						id = item.getId();
					}
				}
			}
			assertFalse("Nao encontrou o item",id.equals(""));
			
		}
		
	}

	@Override
	public final void tearDown() throws Exception {
		super.tearDown();
	}

}
