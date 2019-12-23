package os.client.menus;

import static os.client.ClientApp.receiveMessage;
import static os.client.ClientApp.sendMessage;

import java.util.List;
import java.util.Scanner;

public class ClubMenu extends Menu {
	
	private String incomingMsg;
	private String outgoingMsg;
	
	private static final Scanner console = new Scanner(System.in);
	
	@Override
	public void showMenu() {
		char option;
		boolean cont = true;
		
		listOptions();
		
		do {
			try {
				System.out.print("Select an option listed above: ");
				option = console.next().charAt(0);

				if (!Character.toString(option).matches("[a-dqA-DQ]")) {
					throw new InvalidOptionException();
				}

				cont = handleOption(option);	
			} catch (InvalidOptionException e) {
				System.out.println("[Error] "+ e.getMessage());
			}
		} while (cont);
	}
	
	private void listOptions() {
		System.out.println("|==================================================|");
		System.out.println("|                    Club  Menu                    |");
		System.out.println("|==================================================|");
		System.out.println("| A. Search Players by Position                    |");
		System.out.println("| B. Search for For Sale Player in Club            |");
		System.out.println("| C. Suspend/Resume Sale                           |");
		System.out.println("| D. Purchase Player                               |");
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
		System.out.println(incomingMsg);
	}
	
	private void searchAllForSale() {
		// Show list of players
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
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
	}

	private void purchasePlayer() {
		// Send player Id
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Display result
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
	}
}
