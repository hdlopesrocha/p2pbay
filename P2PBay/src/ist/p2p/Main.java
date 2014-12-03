package ist.p2p;


import ist.p2p.dto.BidDto;
import ist.p2p.dto.ItemDto;
import ist.p2p.dto.PurchaseDto;
import ist.p2p.service.AcceptBidService;
import ist.p2p.service.AuthenticateUserService;
import ist.p2p.service.BidAnItemService;
import ist.p2p.service.ConnectP2PBayService;
import ist.p2p.service.GetItemByIdService;
import ist.p2p.service.Gossip;
import ist.p2p.service.OfferAnItemForSaleService;
import ist.p2p.service.RegisterUserService;
import ist.p2p.service.SearchAnItemService;
import ist.p2p.service.ViewUserHistoryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * The Class P2PBay.
 */
public class Main {

	/**
	 * Load users file.
	 *
	 * @param filename
	 *            the filename
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void loadUsersFile(String filename) throws IOException {
		final File file = new File(filename);
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";

		while ((line = reader.readLine()) != null) {
			final String[] splits = line.split(":");
			final RegisterUserService service = new RegisterUserService(
					splits[0], splits[1]);
			service.execute();
		}
		reader.close();
	}

	// -i ip:port of known peer (bootstrap if not exists)
	// -u loads users file

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		//ResourceLeakDetector.setLevel(Level.DISABLED);
		ConnectP2PBayService service;
		final String ip = Utils.getArgValue("-i", args);
		final String port = Utils.getArgValue("-b", args);
		
		if (ip != null) {
			final String[] splits = ip.split(":");
			service = new ConnectP2PBayService(splits[0],Integer.valueOf(splits[1]));
			service.execute();
		} else if(port!=null) {
			service = new ConnectP2PBayService(Integer.valueOf(port));
			service.execute();
		}
		
		final String usersFileName = Utils.getArgValue("-u", args);
		if (usersFileName != null) {
			loadUsersFile(usersFileName);
		}
		Gossip.start();


		final Scanner scanner = new Scanner(System.in);
		while (true) {
			commandLine(scanner);
		}
		// scanner.close();
	}

	/**
	 * Command line.
	 *
	 * @param scanner
	 *            the scanner
	 */
	public static void commandLine(Scanner scanner) {

		String username = "";
		while (true) {

			System.out.print("Username: ");
			username = scanner.nextLine();

			System.out.print("Password: ");
			final String password = scanner.nextLine();

			final AuthenticateUserService service = new AuthenticateUserService(
					username, password);
			if (service.execute() == true) {
				break;
			}
			System.err.println("Authentication failed!");
		}
		System.out.println("Authentication Succeded!");

		while (true) {
			System.out.println("************************************");
			System.out.println("1) offer an item for sale");
			System.out.println("2) accept a bid");
			System.out.println("3) search for an item to buy");
			System.out.println("4) bid on an item");
			System.out.println("5) view the details of an item");
			System.out.println("6) view purchase and bidding history");
			System.out.println("7) management");
			System.out.println("0) logout");
			System.out.print("option: ");

			try {
				final int option = Integer.valueOf(scanner.nextLine());
				if (option == 1) {
					offerAnItemMenu(username, scanner);
				} else if (option == 2) {
					acceptABidMenu(username, scanner);
				} else if (option == 3) {
					searchAnItemMenu(username, scanner);
				} else if (option == 4) {
					bidOnAnItemMenu(username, scanner);
				} else if (option == 5) {
					viewItemDetailsMenu(username, scanner);
				} else if (option == 6) {
					viewHistoryMenu(username, scanner);
				}  else if (option == 7) {
					viewManagement(username, scanner);
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

	private static void viewManagement(String username, Scanner scanner) {
		System.out.println("Nodes count = "+ Gossip.getNodeCount());
		System.out.println("Users count = "+ Gossip.getRegisteredUsers());
		System.out.println("Items on sale count = "+ Gossip.getItemsOnSale());
	}

	/**
	 * Bid on an item menu.
	 *
	 * @param username
	 *            the username
	 * @param scanner
	 *            the scanner
	 */
	private static void bidOnAnItemMenu(String username, Scanner scanner) {
		System.out.print("id: ");
		final String id = scanner.nextLine();
		System.out.print("offer: ");
		try {
			final Float offer = Float.valueOf(scanner.nextLine());
			final BidAnItemService service = new BidAnItemService(username, id,
					offer);
			if (service.execute()) {
				System.out.println("Bid succeded!");
			} else {
				System.out.println("Invalid bid!");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid offer!");
		}

	}

	/**
	 * View history menu.
	 *
	 * @param username
	 *            the username
	 * @param scanner
	 *            the scanner
	 */
	private static void viewHistoryMenu(String username, Scanner scanner) {
		final ViewUserHistoryService userBidsService = new ViewUserHistoryService(
				username);
		if (userBidsService.execute()) {

			System.out.println("############## BIDS HISTORY ##############");
			for (BidDto bid : userBidsService.getBids()) {
				if (bid != userBidsService.getBids().get(0)) {
					System.out.println("\t--------------------------------");
				}
				GetItemByIdService getService = new GetItemByIdService(
						bid.getItem());
				getService.execute();
				System.out.println("\tid: " + bid.getItem());
				System.out.println("\ttitle: "
						+ getService.getItem().getTitle());
				System.out.println("\toffer: " + bid.getOffer());

			}
			System.out
					.println("############## PURCHASES HISTORY ##############");
			for (PurchaseDto item : userBidsService.getPurchases()) {
				System.out.println("\tid: " + item.getId());
				System.out.println("\ttitle: " + item.getTitle());
				System.out.println("\toffer: " + item.getOffer());
			}
		}
	}

	/**
	 * View item details menu.
	 *
	 * @param username
	 *            the username
	 * @param scanner
	 *            the scanner
	 */
	private static void viewItemDetailsMenu(String username, Scanner scanner) {
		System.out.print("id: ");
		final String id = scanner.nextLine();
		final GetItemByIdService service = new GetItemByIdService(id);
		if (service.execute()) {
			final ItemDto item = service.getItem();
			System.out.println("owner: " + item.getOwner());
			System.out.println("title: " + item.getTitle());
			System.out.println("description: " + item.getDescription());
			System.out.println("bids: ");
			for (BidDto bid : item.getBids()) {
				System.out.println("\t" + bid.toString());
			}

		} else {
			System.out.println("Item not found!");
		}
	}

	/**
	 * Accept a bid menu.
	 *
	 * @param username
	 *            the username
	 * @param scanner
	 *            the scanner
	 */
	private static void acceptABidMenu(String username, Scanner scanner) {
		System.out.print("id: ");
		final String id = scanner.nextLine();
		final AcceptBidService service = new AcceptBidService(username, id);
		if (service.execute()) {
			System.out.println("Bid accepted with success!");
		} else {
			System.out.println("Bid not accepted!");
		}
	}

	/**
	 * Offer an item menu.
	 *
	 * @param username
	 *            the username
	 * @param scanner
	 *            the scanner
	 */
	public static void offerAnItemMenu(String username, Scanner scanner) {

		System.out.print("title: ");
		final String title = scanner.nextLine();
		System.out.print("description: ");
		final String description = scanner.nextLine();

		final OfferAnItemForSaleService service = new OfferAnItemForSaleService(
				username, title, description);
		if (service.execute()) {
			System.out.println("Offered item with success!");
		} else {
			System.out.println("Offer error!");
		}
	}

	/**
	 * Search an item menu.
	 *
	 * @param username
	 *            the username
	 * @param scanner
	 *            the scanner
	 */
	public static void searchAnItemMenu(String username, Scanner scanner) {

		System.out.print("search: ");
		final String search = scanner.nextLine();
		final SearchAnItemService service = new SearchAnItemService(search);
		if (service.execute()) {
			for (ItemDto item : service.getItems()) {
				System.out.println(item.getId() + " | " + item.getTitle());
			}
		}
	}

}
