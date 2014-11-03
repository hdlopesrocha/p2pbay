package ist.p2p;

import ist.p2p.dto.ItemDto;
import ist.p2p.dto.HistoryDto;
import ist.p2p.service.AuthenticateUserService;
import ist.p2p.service.BidAnItemService;
import ist.p2p.service.ConnectP2PBayService;
import ist.p2p.service.GetItemByIdService;
import ist.p2p.service.OfferAnItemForSaleService;
import ist.p2p.service.RegisterUserService;
import ist.p2p.service.SearchAnItemService;
import ist.p2p.service.ViewUserHistoryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
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

		final String usersFileName = Utils.getArgValue("-u", args);
		if (usersFileName != null) {
			loadUsersFile(usersFileName);
		}
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
		System.out.println("***********************");
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
			System.out.println("\t1) offer an item for sale");
			System.out.println("\t2) accept a bid");
			System.out.println("\t3) search for an item to buy");
			System.out.println("\t4) bid on an item");
			System.out.println("\t5) view the details of an item");
			System.out.println("\t6) view purchase and bidding history");
			System.out.println("\t0) logout");
			System.out.print("\toption: ");

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
		HistoryDto user = userBidsService.execute();

		System.out.println("--- BIDS HISTORY ---");
		for (String str : user.getBids()) {
			System.out.println(str);
		}
		System.out.println("--- PURCHASES HISTORY ---");
		for (String str : user.getPurchases()) {
			System.out.println(str);
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
		final ItemDto result = service.execute();
		if (result != null) {
			System.out.println(result);
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
		// TODO Auto-generated method stub

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
		final ItemDto newItem = new ItemDto(username, title, description);

		final OfferAnItemForSaleService service = new OfferAnItemForSaleService(
				newItem);
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

		final List<ItemDto> items = service.execute();
		for (ItemDto item : items) {
			System.out.println(item.toString());
		}
	}

}
