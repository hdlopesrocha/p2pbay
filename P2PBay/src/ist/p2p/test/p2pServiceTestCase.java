package ist.p2p.test;

import ist.p2p.service.ConnectP2PBayService;

import java.util.Random;

import junit.framework.TestCase;

public abstract class p2pServiceTestCase extends TestCase {
	
	final Random RANDOM = new Random();
	
	ConnectP2PBayService service = null;
	final int port = 1025 + RANDOM.nextInt(60000);
	
	{
		service = new ConnectP2PBayService(port);
		service.execute();
	}
	
//	static {
//		
//		/** The Constant RANDOM. */
//		final Random RANDOM = new Random();
//		final int port = 1025 + RANDOM.nextInt(60000);
////		StorageGeneric storage = null;
//		Peer peer = null;
//		final Number160 myPeerId = new Number160(RANDOM);
//		final int myPeerPort = port;
//		final int masterPort = port;
//		final String masterIp = "127.0.0.1";
//		
//		Bindings bindings=new Bindings();
//		PeerMaker pm =new PeerMaker(myPeerId).setPorts(myPeerPort).setBindings(bindings)
//				.setEnableIndirectReplication(true);
////		storage = pm.getStorage();
//		try {
//			peer = pm.makeAndListen();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		peer.getConfiguration().setBehindFirewall(true);
//		
//		InetAddress address = null;
//
//		try {
//			address = InetAddress.getByName(masterIp);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		
//		{
//			final FutureDiscover futureDiscover = peer.discover()
//					.setInetAddress(address).setPorts(masterPort).start();
//			futureDiscover.awaitUninterruptibly();
//
//			if (!futureDiscover.isSuccess()) {
//				System.out
//						.println("Discover with direct connection failed. Reason = "
//								+ futureDiscover.getFailedReason());
//				peer.shutdown();
//			}
//		}		
//    }

	public p2pServiceTestCase() {
		super();
	}
	
	public p2pServiceTestCase(final String msg) {
		super(msg);
	}
	
}
