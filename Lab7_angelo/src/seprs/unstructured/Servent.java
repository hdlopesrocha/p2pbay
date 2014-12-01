package seprs.unstructured;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ClientSocketThread extends Thread {
	Servent sv;
	Socket s;

	public ClientSocketThread(Servent sv1, Socket s1) {
		this.sv = sv1;
		this.s = s1;
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

public class Servent {
	public static final int BUFF_SIZE = 100;

	private ServerSocket serverSocket;
	List<Socket> peers = new ArrayList<Socket>();
	int id;
	Document doc = null;
	int value = 1;
	int weight = 0;

	public Servent(int id) {
		this.id = id;
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(null);
			System.out.println("Listening on port "
					+ serverSocket.getLocalPort());
		} catch (IOException e) {
			System.out.println("Could not open socket");
			System.exit(-1);
		}
	}

	public void connect(String ip, int port) throws UnknownHostException,
			IOException, ClassNotFoundException {
		Socket sock = new Socket(ip, port);
		synchronized (this) {
			peers.add(sock);
		}
		new ObjectOutputStream(sock.getOutputStream())
				.writeObject(new Message( MessageType.HELLO,id));
		new ClientSocketThread(this, sock).start();
	}

	void acceptConnections() throws IOException {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						Socket sock;
						sock = serverSocket.accept();
						synchronized (Servent.this) {
							peers.add(sock);
						}
						new ClientSocketThread(Servent.this, sock).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	synchronized void handleMsg(Message msg, Socket s) throws IOException {

		switch (msg.getMessageType()) {
		case HELLO:
			System.out.printf("Received hello from %d\n", msg.getContent());
			new ObjectOutputStream(s.getOutputStream())
					.writeObject(new Message( MessageType.HELLO_REPLY,id));
			break;
		case HELLO_REPLY:
			System.out.printf("Received hello reply from %d\n",
					msg.getContent());
			break;
		case SEARCH:
			System.out.println("###########  Entrou no SEARCH  ###########");
<<<<<<< HEAD
			SearchMessage hmsearch = (SearchMessage) msg;
			if (this.doc == null) {
				System.out.println("Documento Vazio!!\n");
				this.searchWord(hmsearch);
			} else if (this.doc.searchWord(hmsearch.getWord())) {
				new ObjectOutputStream(s.getOutputStream())
				.writeObject(new SearchMessageReply("Peer "+this.id+" have the word("+hmsearch.getWord()+") in your text\n"));
			} else {
				System.out.println("Documento " + hmsearch.getWord() + " não encontrado!!\n");
				this.searchWord(hmsearch);
			}
			break;
		case SEARCH_REPLY:
			System.out.println("###########  Entrou no SEARCH_REPLY  ###########");
			SearchMessageReply hmsgreply = (SearchMessageReply) msg;
			System.out.printf("Received msg %s\n",
					hmsgreply.getSrcId());
=======
			if (this.doc.searchWord((String)msg.getContent())) {
				System.out.printf("Peer %d have the word(%s) in your text\n", this.id, msg.getContent());
//				new ObjectOutputStream(s.getOutputStream())
//						.writeObject(new HelloMessageReply(id));
			}				
>>>>>>> 43d8a9fee4d00655ac4f358c48ee45efe8f9fa83
			break;
		}
	}
	
<<<<<<< HEAD
	synchronized void searchWord(SearchMessage msg) throws IOException {
		for (Socket soc : this.peers) {
			ArrayList<String> peersSend = msg.getpeers();
			String ipPort = soc.getInetAddress()+":"+soc.getPort();
			
			for (String str : peersSend) {
				System.out.println("Lista de Peers que já foi enviado:");
				System.out.println("Peer: " + str);
			}
			System.out.println("\n");
			
			if (!peersSend.contains(ipPort)) {
				System.out.println("-----> searchWord enviado para " + ipPort);
				msg.addPeer(ipPort);
				new ObjectOutputStream(soc.getOutputStream())
				.writeObject(msg);
			}
			
		}
=======
	synchronized void searchWord(String word) throws IOException {
		for (Socket soc : this.peers)
			new ObjectOutputStream(soc.getOutputStream())
			.writeObject(new Message(MessageType.SEARCH, word));
>>>>>>> 43d8a9fee4d00655ac4f358c48ee45efe8f9fa83
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
			UnknownHostException, IOException, ClassNotFoundException {
		if (args.length >= 1) {			
			System.out.println("#############  ID=" + args[0] + "  #############");
			Servent sr = new Servent(Integer.parseInt(args[0]));
			
			if (Integer.parseInt(args[0])==1) {
				sr.doc = new Document("Teste lixado");
			} else if (Integer.parseInt(args[0])==2) {
				sr.doc = new Document("Cenas fixes");
			}
			
			if (args.length >= 2 && Integer.parseInt(args[1]) == 1)
				sr.weight = 1;
			
			for (int x = 2; x < args.length; x += 2) {
				System.out.println("for="+ x+"\n");
				sr.connect(args[x], Integer.parseInt(args[x + 1]));
			}
			
			if (Integer.parseInt(args[0]) != 1) {
				System.out.println("Procura lixado:");
				sr.searchWord(new SearchMessage("lixado"));
				
				System.out.println("Procura Teste:");
				sr.searchWord(new SearchMessage("teste"));
				
				System.out.println("Procura lixado:");
				sr.searchWord(new SearchMessage("cenas"));
				
				System.out.println("Procura Teste:");
				sr.searchWord(new SearchMessage("fixes"));
			}
			
			sr.acceptConnections();
			
			

			// create a different thread for sending messages?
		} else
			System.out
					.println("Usage: id [<ip> <port>]*\nexample: 4 192.168.1.2 1234");

	}

}
