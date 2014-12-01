package seprs.unstructured;

import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SearchMessage implements Serializable, Message {
	
	String word;
	int ID;
	ArrayList<String> peers = new ArrayList<String>();

	public SearchMessage( String src) {
		word = src;
	}
	
	public SearchMessage(String src, int IDtemp) {
		word = src;
		this.ID = IDtemp;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public ArrayList<String> getpeers() {
		return this.peers;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void addPeer(String peerSend) {
		peers.add(peerSend);
	}
	
	@Override
	public MessageType getMSGType() {
		return MessageType.SEARCH;
	}

}