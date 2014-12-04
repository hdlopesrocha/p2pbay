package ist.p2p.test;

import ist.p2p.service.AuthenticateUserService;
import ist.p2p.service.RegisterUserService;

public class AuthenticationTest extends p2pServiceTestCase{
	
	
	/** The Constant CLIENT_USERNAME. */
    private static final String CLIENT_USERNAME = "admin";
 
    /** The Constant CLIENT_PASSWORD. */
    private static final String CLIENT_PASSWORD = "admin";
    
    public AuthenticationTest() {
        super();
    }
    
    public AuthenticationTest(final String msg) {
        super(msg);
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public final void setUp() throws Exception {
        super.setUp();
    }
    
    public final void testLogin() {
    	
    	final RegisterUserService service = new RegisterUserService(
    			CLIENT_USERNAME, CLIENT_PASSWORD);
		service.execute();
    	
    	AuthenticateUserService authenticateUserService =
                new AuthenticateUserService(CLIENT_USERNAME,CLIENT_PASSWORD);
        try {
        	assertTrue(authenticateUserService.execute());
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }
 
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    public final void tearDown() throws Exception  {
        super.tearDown();
    }
    
    
}