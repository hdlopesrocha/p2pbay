package ist.p2p.service;

import java.io.IOException;

public class ConnectP2PBayService extends P2PBayService{

	private String ip =null;
	private int port;
	
	public ConnectP2PBayService(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public ConnectP2PBayService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() {
		try {
			connect(ip,port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
