package seprs.unstructured;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
	Document doc = new Document("Teste lixado");

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
		while (true) {
			Socket sock = serverSocket.accept();
			synchronized (this) {
				peers.add(sock);
			}
			new ClientSocketThread(this, sock).start();

		}
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
			if (this.doc.searchWord((String)msg.getContent())) {
				System.out.printf("Peer %d have the word(%s) in your text\n", this.id, msg.getContent());
//				new ObjectOutputStream(s.getOutputStream())
//						.writeObject(new HelloMessageReply(id));
			}				
			break;
		}
	}
	
	synchronized void searchWord(String word) throws IOException {
		for (Socket soc : this.peers)
			new ObjectOutputStream(soc.getOutputStream())
			.writeObject(new Message(MessageType.SEARCH, word));
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
			Servent sr = new Servent(Integer.parseInt(args[0]));
			for (int x = 1; x < args.length; x += 2)
				sr.connect(args[x], Integer.parseInt(args[x + 1]));
			
			System.out.println("Procura lixado:");
			sr.searchWord("lixado");
			
			System.out.println("Procura Teste:");
			sr.searchWord("teste");

			System.out.println("Procura lixado:\n");
			sr.acceptConnections();
			
			

			// create a different thread for sending messages?
		} else
			System.out
					.println("Usage: id [<ip> <port>]*\nexample: 4 192.168.1.2 1234");

	}

}
