package ist.p2p;

import ist.p2p.dto.ItemDto;
import ist.p2p.service.AuthenticateUserService;
import ist.p2p.service.ConnectP2PBayService;
import ist.p2p.service.OfferAnItemForSaleService;
import ist.p2p.service.RegisterUserService;
import ist.p2p.service.SearchAnItemService;

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
		File file = new File(filename);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";

		while ((line = reader.readLine()) != null) {
			String[] splits = line.split(":");
			RegisterUserService service = new RegisterUserService(splits[0], splits[1]);
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
		String ip = Utils.getArgValue("-i", args);
		if(ip!=null){
			String [] splits = ip.split(":");
			service = new ConnectP2PBayService(splits[0], Integer.valueOf(splits[1]));
		}
		else {
			service = new ConnectP2PBayService();
		}
		service.execute();

		String usersFileName = Utils.getArgValue("-u", args);
		if (usersFileName != null) {
			loadUsersFile(usersFileName);
		}
		commandLine();
	}

	/**
	 * Command line.
	 */
	public static void commandLine() {
		System.out.println("***********************");
		Scanner scanner = new Scanner(System.in);
		String username = "";
		while (true) {

			System.out.print("Username: ");
			username = scanner.nextLine();

			System.out.print("Password: ");
			String password = scanner.nextLine();

			AuthenticateUserService service = new AuthenticateUserService(
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
			System.out.println("\t0) quit");
			System.out.print("\toption: ");
			int option = Integer.valueOf(scanner.nextLine());
			if (option == 1) {
				offerAnItemMenu(username, scanner);
			} else if (option == 2) {

			} else if (option == 3) {
				searchAnItemMenu(username, scanner);
			} else if (option == 4) {

			} else if (option == 5) {

			} else if (option == 6) {

			} else if (option == 0) {
				break;
			} else {
				System.out.println("Option not found!");
			}
		}

		System.out.println("See you next time!");
		scanner.close();
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
		String title = scanner.nextLine();
		System.out.print("description: ");
		String description = scanner.nextLine();
		ItemDto newItem = new ItemDto(username, title, description);

		OfferAnItemForSaleService service = new OfferAnItemForSaleService(
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
		String search = scanner.nextLine();
		SearchAnItemService service = new SearchAnItemService(search);

		List<ItemDto> items = service.execute();
		for (ItemDto item : items) {
			System.out.println(item.toJSON().toString());
		}
	}

}
