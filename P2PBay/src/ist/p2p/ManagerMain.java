package ist.p2p;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

import java.util.Scanner;

import ist.p2p.service.P2PBayService;
import ist.p2p.service.ConnectP2PBayService;

public class ManagerMain {
	

	public static void main(String[] args){
		ResourceLeakDetector.setLevel(Level.DISABLED);
		ConnectP2PBayService service;
		final String ip = Utils.getArgValue("-i", args);
		if (ip != null) {
			final String[] splits = ip.split(":");
			service = new ConnectP2PBayService(splits[0],
					Integer.valueOf(splits[1]));
		} else {
			service = new ConnectP2PBayService();
		}
		service.execute();

		final Scanner scanner = new Scanner(System.in);

		commandLine(scanner);
		
	}


	public static void commandLine(Scanner scanner){

		while (true) {
			System.out.println("************************************");
			System.out.println("1) Start Gossip");
			System.out.println("2) Get Number of active Nodes");
			System.out.println("3) Get Number of User Accounts");
			System.out.println("4) Get Number of Stored Files");
			System.out.println("0) logout");
			System.out.print("option: ");

			try {
				final int option = Integer.valueOf(scanner.nextLine());
				if (option == 1) {
					startGossip();
				} else if (option == 2) {
					numNodes();
				} else if (option == 3) {
					numUsers();
				} else if (option == 4) {
					numFiles();
				} else if (option == 0) {
					break;
				} else {
					System.out.println("Option not found!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid option!");
			}

		}

		System.out.println("See you next time!");

	}
	public static void startGossip(){
		P2PBayService.getGossip().spreadGossip();
		System.out.println("Starting Infection...");
	}
	public static void numNodes(){
		System.out.println(P2PBayService.getGossip().calcNodes());
	}
	public static void numUsers(){
		System.out.println(P2PBayService.getGossip().calcUsers());
	}
	public static void numFiles(){
		System.out.println(P2PBayService.getGossip().calcAuctions());
	}


}