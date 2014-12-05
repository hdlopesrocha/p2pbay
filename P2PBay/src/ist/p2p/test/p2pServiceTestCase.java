package ist.p2p.test;

import ist.p2p.service.ConnectP2PBayService;

import java.util.Random;

import junit.framework.TestCase;

public abstract class p2pServiceTestCase extends TestCase {
	
	private final Random RANDOM_MASTER = new Random();
	private final String ip = "127.0.0.1";
	
	protected ConnectP2PBayService serviceMaster = null;
	protected ConnectP2PBayService servicePeer = null;
	private final int portMaster = 1025 + RANDOM_MASTER.nextInt(2000);
	
	{
		serviceMaster = new ConnectP2PBayService(portMaster);
		servicePeer = new ConnectP2PBayService(ip,portMaster);
	}
	
	public p2pServiceTestCase() {
		super();
	}
	
	public p2pServiceTestCase(final String msg) {
		super(msg);
	}
	
}
