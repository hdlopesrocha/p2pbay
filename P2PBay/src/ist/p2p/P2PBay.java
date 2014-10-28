package ist.p2p;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class P2PBay {

    static private Peer peer=null;

	
	public static boolean argExists(String arg, String [] args){
		for(int i=0; i < args.length ; ++i){
			if(args[i].equals(arg)){
				return true;
			}
		}
		return false;		
	}
    
    
	public static String getArgValue(String arg, String [] args){
		for(int i=0; i < args.length ; ++i){
			if(args[i].equals(arg) && i+1 < args.length){
				return args[i+1];
			}
		}
		return null;		
	}
	
	
	public static void loadUsersFile(String filename) throws IOException{
		File file = new File(filename);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line="";
		
		while((line=reader.readLine())!=null){
			String [] splits = line.split(":");
			store(splits[0],splits[1]);
		}
		
		
		reader.close();
		
	}
	
    private static String get(String key) throws ClassNotFoundException, IOException {
        FutureDHT futureDHT = peer.get(Number160.createHash(key)).start();
        futureDHT.awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getObject().toString();
        }
        return "not found";
    }
    
    private static void store(String key, String value) throws IOException {
        peer.put(Number160.createHash(key)).setData(new Data(value)).start().awaitUninterruptibly();
    }
	
	
	// -i ip:port of known peer (bootstrap if not exists)
	// -u loads users file
	
	public static void main(String[] args) throws IOException {
		String usersFileName = getArgValue("-u", args);

		Random  random = new Random();
		int myPeerPort = !argExists("-i",args) ? 1024: 1025 + random.nextInt(60000);
		
		Bindings bindings = new Bindings();
		bindings.addInterface("eth0");
		bindings.addInterface("wlan0");
		bindings.addInterface("lo");

        peer = new PeerMaker(new Number160(random)).setPorts(myPeerPort).setBindings(bindings).makeAndListen();
        peer.getConfiguration().setBehindFirewall(true);
        
        if(argExists("-i",args)){
    		String [] knownPeerIpPort = getArgValue("-i", args).split(":");
    		int knownPeerPort = Integer.valueOf(knownPeerIpPort[1]);
    		String knownPeerIp = knownPeerIpPort[0];
        	InetAddress address = Inet4Address.getByName(knownPeerIp);
        	FutureDiscover futureDiscover = peer.discover().setInetAddress(address).setPorts(knownPeerPort).start();
        	futureDiscover.awaitUninterruptibly();
        }
      
		
    	if(usersFileName!=null){
    		loadUsersFile(usersFileName);        		
    	}
		
		System.out.println("***********************");
		
		
		commandLine();
		
	}

	public static void commandLine(){
		Scanner scanner = new Scanner(System.in);
		Boolean authenticated = false;
		
		while(!authenticated){
		
			System.out.print("Username: ");
			String username = scanner.nextLine();
			
			System.out.print("Password: ");
			String password = scanner.nextLine();
			
			String remotePassword;
			try {
				remotePassword = get(username);	
				if(remotePassword !=null && remotePassword.equals(password)){
					authenticated=true;
				}
				else {
					System.err.println("Authentication failed!");
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Authentication Success!");
		scanner.close();
	}
	
	
	
}
