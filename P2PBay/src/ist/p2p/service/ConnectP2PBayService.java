package ist.p2p.service;

import java.io.IOException;

import net.tomp2p.peers.Number160;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectP2PBayService.
 */
public class ConnectP2PBayService extends P2PBayService {

	/** The master ip. */
	private String masterIp = null;

	/** The master port. */
	private int masterPort;

	/** The my port. */
	private int myPort;

	/** The my id. */
	private Number160 myId;

	/**
	 * Instantiates a new connect p2 p bay service.
	 *
	 * @param ip
	 *            the ip
	 * @param port
	 *            the port
	 */
	public ConnectP2PBayService(String ip, int port) {
		this.masterIp = ip;
		this.masterPort = port;
		this.myPort = 1025 + RANDOM.nextInt(60000);
		this.myId = new Number160(RANDOM);
	}

	/**
	 * Instantiates a new connect p2 p bay service.
	 */
	public ConnectP2PBayService(int port) {
		this.masterIp = "127.0.0.1";
		this.masterPort = port;
		this.myPort = port;
		this.myId = Number160.ONE;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ist.p2p.service.P2PBayService#execute()
	 */
	@Override
	public boolean execute() {
		try {
			connect(masterIp, masterPort, myPort, myId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
