package os.client.menus;

import java.util.Scanner;

import static os.client.ClientApp.receiveMessage;
import static os.client.ClientApp.sendMessage;

public class ClubMenu extends Menu {
	private String incomingMsg;
	private String outgoingMsg;
	
	private static final Scanner console = new Scanner(System.in);
	
	@Override
	public void show() {
		char option;
		boolean cont = true;
		
		listOptions();
	
		do {
			System.out.print("Select an option listed above: ");
			option = console.next().charAt(0);

			if (!Character.toString(option).matches("[a-eqA-EQ]")) {
				System.out.println("[Error] Please enter one of the options provided.");
			}

			cont = handleOption(option);	
		} while (cont);
	}
	
	private void listOptions() {
		System.out.println();
		System.out.println("|==================================================|");
		System.out.println("|                    Club  Menu                    |");
		System.out.println("|==================================================|");
		System.out.println("| A. Search Players by Position                    |");
		System.out.println("| B. Search for \"For Sale\" Players in Club         |");
		System.out.println("| C. Suspend/Resume Sale                           |");
		System.out.println("| D. Purchase Player                               |");
		System.out.println("| E. Display All Players                           |");
		System.out.println("| Q. Logout                                        |");
		System.out.println("|==================================================|");
	}
	
	private boolean handleOption(char option) {
		switch (Character.toUpperCase(option)) {
			case 'A':
				sendMessage("A");
				searchAllByPosition();
				break;
			case 'B':
				sendMessage("B");
				searchAllForSale();
				break;
			case 'C':
				sendMessage("C");
				suspendResumeSale();
				break;
			case 'D':
				sendMessage("D");
				purchasePlayer();
				break;
			case 'E':
				sendMessage("E");
				displayAllPlayers();
				break;
			case 'Q':
				sendMessage("Q");
				return false;
			default:
				break;
		}

		return true;
	}

	private void searchAllByPosition() {
		// Send player position
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Show list of players
		incomingMsg = (String)receiveMessage();

		for (String line : formatList(incomingMsg)) {
			System.out.println(line);
		}
		
		listOptions();
	}
	
	private void searchAllForSale() {
		// Show list of players
		incomingMsg = (String)receiveMessage();

		for (String line : formatList(incomingMsg)) {
			System.out.println(line);
		}
		
		listOptions();
	}

	private void suspendResumeSale() {
		// Send player Id
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Send new player status
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Get result (success/failure)
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		
		listOptions();
	}

	private void purchasePlayer() {
		// Send player Id
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Get result (success/failure)
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		
		listOptions();
	}
	
	private void displayAllPlayers() {
		incomingMsg = (String)receiveMessage();

		for (String line : formatList(incomingMsg)) {
			System.out.println(line);
		}
		
		listOptions();
	}
}
