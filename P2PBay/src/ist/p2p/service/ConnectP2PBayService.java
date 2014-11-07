package ist.p2p.service;

import java.io.IOException;

import net.tomp2p.peers.Number160;

public class ConnectP2PBayService extends P2PBayService{

	private String masterIp =null;
	private int masterPort;
	private int myPort;
	private Number160 myId;
	
	public ConnectP2PBayService(String ip, int port) {
		this.masterIp = ip;
		this.masterPort = port;
		this.myPort = 1025 + RANDOM.nextInt(60000);
		this.myId = new Number160(RANDOM);
	}

	public ConnectP2PBayService() {
		this.masterIp = "127.0.0.1";
		this.masterPort = 1024;
		this.myPort = 1024;
		this.myId = Number160.ONE;

	}

	@Override
	public boolean execute() {
		try {
			connect( masterIp,masterPort, myPort,myId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
