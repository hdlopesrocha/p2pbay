package ist.p2p.test;

import ist.p2p.service.AuthenticateUserService;
import ist.p2p.service.RegisterUserService;

public class AuthenticationTest extends p2pServiceTestCase {

	/** The Constant CLIENT_USERNAME. */
	private static final String CLIENT_USERNAME = "admin";

	/** The Constant CLIENT_PASSWORD. */
	private static final String CLIENT_PASSWORD = "admin";

	/** The Constant CLIENT_USERNAME. */
	private static final String CLIENT_USERNAME_FALSE = "user1";

	/** The Constant CLIENT_PASSWORD. */
	private static final String CLIENT_PASSWORD_FALSE = "1111";
	
	private static boolean initialize = true;

	public AuthenticationTest() {
		super();
	}

	public AuthenticationTest(final String msg) {
		super(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
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
	    	 final RegisterUserService registerUserService = new RegisterUserService(
	    			 CLIENT_USERNAME, CLIENT_PASSWORD);
	    	 assertTrue("Registo de Utilizador", registerUserService.execute());
	     }
	     
	     servicePeer.execute();
	 }

	public final void testRemoteLogin() {
		
		{
			AuthenticateUserService authenticateUserService = new AuthenticateUserService(
					CLIENT_USERNAME, CLIENT_PASSWORD);

			assertTrue("Autenticacao com sucesso",
					authenticateUserService.execute());
		}
	}

	public final void testRemoteLoginFalse() {
		
		{
			AuthenticateUserService authenticateUserService = new AuthenticateUserService(
					CLIENT_USERNAME_FALSE, CLIENT_PASSWORD_FALSE);

			assertFalse("Autenticacao sem sucesso",
					authenticateUserService.execute());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public final void tearDown() throws Exception {
		super.tearDown();
	}

}