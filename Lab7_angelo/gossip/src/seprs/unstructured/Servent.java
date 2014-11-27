package seprs.unstructured;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

class ClientSocketThread extends Thread {
	Servent sv;
	Socket s;
	public ClientSocketThread(Servent sv, Socket s) {
		this.sv = sv;
		this.s = s;
	}

	@Override
	public void run() {
		ObjectInputStream ois;
		try {
			while (true) {
				ois = new ObjectInputStream(s.getInputStream());	
				Message msg = (Message) ois.readObject();
				sv.handleMsg(msg, s);
			}
		} catch (IOException e) {
			e.printStackTrace();
			synchronized( sv) {
				sv.peers.remove( s);
			}
			try {
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}


class ClientListenThread extends Thread {
	Servent sv;
	ServerSocket serverSocket;
	public ClientListenThread(Servent sv, ServerSocket server) {
		this.sv = sv;
		this.serverSocket = server;
	}

	@Override
	public void run() {
		try{
			while (true){
				Socket sock = serverSocket.accept();
				synchronized (this) {
					sv.addPeers(sock);
				}
				new ClientSocketThread(sv, sock).start();			
			}
		}catch(IOException e) {
			System.out.println("Accepting conns fail, bye");
			System.exit(-1);
		}
	}


}
class GossipThread extends Thread {
	Servent sv;
	public GossipThread(Servent sv){
		this.sv = sv;
	}
	@Override
	public void run(){
		while(true){
			try{
				sleep(sv.GOSSIP_INTERVAL_TIME);
				sv.queryGossip();

			}catch(InterruptedException e){
				sv.consoleOut(e.getMessage());
			}
		}
	}
}

public class Servent {
	public static final int BUFF_SIZE = 100;
	public static String document;

	private ServerSocket serverSocket;
	private Map<Integer,Socket> replySrc = new HashMap<Integer,Socket>();
	private List<Integer> queriesDone = new ArrayList<Integer>();

	private float gossipValue = 1;
	private float gossipWeight = 0;

	private int numNodes = 1;

	public static final int GOSSIP_INTERVAL_TIME = 1000;

	List<Socket> peers = new ArrayList<Socket>();
	int id;

	public Servent(int id, int port) {
		this.id = id;
		try {
			if (port > 3000)
				serverSocket = new ServerSocket(port);
			else 
				serverSocket = new ServerSocket();
			System.out.println("Listening on port "
					+ serverSocket.getLocalPort());
		} catch (IOException e) {
			System.out.println("Could not bind socket: "+e.getMessage());
			System.exit(-1);
		}
	}
	public int getId(){
		return this.id;
	}
	public int getNumNodes(){
		synchronized(this){
			return this.numNodes;
		}
	}

	public void addPeers(Socket sock){
		synchronized(this){
			this.peers.add(sock);
		}
	}

	public void connect(String ip, int port) throws UnknownHostException,
			IOException, ClassNotFoundException {
		Socket sock = new Socket(ip, port);
		synchronized (this) {
			peers.add(sock);
		}
		new ClientSocketThread(this, sock).start();
	}

	public void query (String query) throws IOException{
		int queryId = new Random().nextInt(Integer.MAX_VALUE);

		synchronized(this){
			this.numNodes = 1;	//reset counter to another query
		}
		for (Socket sock : peers){
			new ObjectOutputStream(sock.getOutputStream()).
				writeObject(new HelloMessage(query,queryId));
		}
		
	}
	//
	//	Send a query message to one peeer
	//
	public void queryGossip() {
		if (gossipWeight<=0 || peers.size()==0)
			return;


		Random rand = new Random(); 
		int queryId = rand.nextInt();
		int peerPos = rand.nextInt(peers.size()); //hope nobody leaves
		try{
			synchronized (this) {
				new ObjectOutputStream(peers.get(peerPos).getOutputStream()).
					writeObject(new GossipMessage(queryId,getGossipWeight()/2,gossipValue/2));
				setGossipWeight(getGossipWeight()/2);
				setGossipValue(getGossipValue()/2);
		//		System.out.println("less weight"+getGossipWeight());
			}

		}catch(IOException e){	
			consoleOut("Unable to send query message: "+e.getMessage());
			return;
		}

	}

	public synchronized void setGossipWeight(float weight){
		this.gossipWeight = weight;
	}

	public synchronized float getGossipWeight(){
		return this.gossipWeight;
	}

	public synchronized void setGossipValue(float value){
		this.gossipValue = value;
	}

	public synchronized float getGossipValue(){
		return this.gossipValue;
	}
	void acceptConnections() throws IOException {
			new ClientListenThread(this,this.serverSocket).start();

	}

	synchronized void handleMsg(Message msg, Socket s) throws IOException {
		switch (msg.getMSGType()) {
		case QUERY:
			HelloMessage hmsg = (HelloMessage) msg;
			System.out.printf("Received query num: %d\n", hmsg.messageId);
			if (queriesDone.indexOf(hmsg.messageId) != -1){ //already answered to this query
				System.out.println("Already answered this query");
				break;
			}
			//check if has document return id if has or 0 if hasnt
			boolean found = false;
			for (String str: this.document.split("\\s+")){
				if (str.equals(hmsg.query))
					found = true;
			}
			//send response Reply(myId,IdOfMessage)
			new ObjectOutputStream(s.getOutputStream())
					.writeObject(new HelloMessageReply((found)?this.id:0,hmsg.messageId));

			//all replies rcvd with this id belong here
			replySrc.put(new Integer(hmsg.messageId),s);

			//query other sockets

			for (Socket sock: peers){
				if (sock.equals(s))	//query rcvd from this guy
					continue;
				//query all other known sockets
				new ObjectOutputStream(sock.getOutputStream()).writeObject(hmsg);
			}
			queriesDone.add(hmsg.messageId);
			break;

		case QUERY_REPLY:
			HelloMessageReply hmsgr = (HelloMessageReply) msg;
			System.out.printf("Received query reply to %d\n",
					hmsgr.messageId);

			Socket replyTo = replySrc.get(hmsgr.messageId);

			//Check if it is my query
			if (replyTo != null){
				new ObjectOutputStream(replyTo.getOutputStream())
					.writeObject(hmsgr);
			}
			else{
				//my query
				numNodes +=1;
				System.out.println("Received answer("+hmsgr.messageId+"): "+hmsgr.replierId);

			}

			break;

		case GOSSIP:
			GossipMessage gmsg = (GossipMessage) msg;
			//Confirm that the message was received
			new ObjectOutputStream(s.getOutputStream())
						.writeObject(new GossipMessageReply(gmsg));
			synchronized(this){
				setGossipWeight(getGossipWeight()+gmsg.getWeight());
				setGossipValue(getGossipValue()+gmsg.getValue());	//can gossip access be blocked?
				//System.out.println("more weight"+getGossipWeight());
			}
			break;

		case GOSSIP_REPLY:
			GossipMessageReply gmsgr = (GossipMessageReply) msg;

			break;
		}
	}
	/*
	 *	method used for printing message 
	 */
	public synchronized static void consoleOut(String message){
		System.out.println(message);
	}

	/**
	 * Creates a listening socket. Prints out the port. Receives as parameters,
	 * list of IP and port pairs or peers to connect to Usage: id [<ip> <port>]*
	 * 
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws NumberFormatException,
			UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		if (args.length >= 1) {
		
			Servent sr = null;
			File f = new File("routes.txt");
			if (f.exists()){
				BufferedReader fr = new BufferedReader(new FileReader(f));
				String lines = null;
				while ((lines=fr.readLine()) != null){
					String[] line = lines.split("\\|");
					if (Integer.parseInt(line[0]) != Integer.parseInt(args[0])) //not my port
						continue;
					
					sr = new Servent(Integer.parseInt(args[0]),Integer.parseInt(line[1]));
					sr.acceptConnections();

					sr.document = line[2];

					if (line.length <= 3)	//provides no connections
						break;
					int i;
					for (i=3; i < line.length;i++){
						
						String[] ipport = line[i].split(":");
						sr.connect(ipport[0],Integer.parseInt(ipport[1]));
					}
				}
			}else{
				System.out.println("No file given at start.");
				sr = new Servent(Integer.parseInt(args[0]),0);
				sr.acceptConnections();
			}

				new GossipThread(sr).start(); //start gossiping

				BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
				while(true){
					System.out.print("1) connect\n"+
									 "2) query\n"+
					                 "3) number of nodes\n"+
									 "4) (GOSSIP) Give weight 1\n"+
									 "5) (GOSSIP) Number of nodes\n"+
					                 "6) exit\n"+
					                 ">>");

					int cmd = Integer.parseInt(input.readLine());
					switch(cmd){
						case 1:
							System.out.print("connect(ip:port)>>");
							//String ipport = input.readLine();
							String[] cmds =  input.readLine().split(":");
							sr.connect(cmds[0], Integer.parseInt(cmds[1]));
							break;

						case 2:
							System.out.print("query >> ");
							sr.query(input.readLine());
							break;

						case 3:
							int nodes = sr.getNumNodes();
							System.out.println(nodes>1?"There are "+nodes+" nodes": "Do a query first.");
							break;

						case 4:
							sr.setGossipWeight(1);
							break;
						case 5:
							synchronized (sr) {
								System.out.println("weight: "+sr.getGossipWeight());
								System.out.println("value: "+sr.getGossipValue());
								System.out.println("nodes: "+Math.round(sr.getGossipValue()/sr.getGossipWeight()));
							}
							break;
						case 6:
							System.exit(0);
						default:
							System.out.println("Wrong value");
					}
						//Wait awhile for output of threads
						Thread.sleep(100);		//hehe (locks for what?)
				
				} 

		}else
			System.out
					.println("Usage: id [<ip> <port>]*\nexample: 4 192.168.1.2 1234");

		
	}
}
