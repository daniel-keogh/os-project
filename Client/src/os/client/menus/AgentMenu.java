package os.client.menus;

import java.util.Scanner;
import static os.client.ClientApp.receiveMessage;
import static os.client.ClientApp.sendMessage;

public class AgentMenu implements Menu {

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

				if (!Character.toString(option).matches("[a-cqA-cQ]")) {
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
		System.out.println("|                    Agent Menu                    |");
		System.out.println("|==================================================|");
		System.out.println("| A. Add Player                                    |");
		System.out.println("| B. Update Player Valuation                       |");
		System.out.println("| C. Update Player Status                          |");
		System.out.println("| Q. Logout                                        |");
		System.out.println("|==================================================|");
	}

	private boolean handleOption(char option) {
		switch (Character.toUpperCase(option)) {
			case 'A':
				sendMessage("A");
				addPlayer();
				break;
			case 'B':
				sendMessage("B");
				updatePlayerValuation();
				break;
			case 'C':
				sendMessage("C");
				updatePlayerStatus();
				break;
			case 'Q':
				sendMessage("Q");
				return false;
			default:
				break;
		}

		return true;
	}

	private void addPlayer() {
		// Send player name
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Send player age
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Send club id
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Send player valuation
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Send player status
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Send player status
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		listOptions();
	}

	private void updatePlayerValuation() {
		// Send player id
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Send new player valuation
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		listOptions();
	}

	private void updatePlayerStatus() {
		// Send player id
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);

		// Send new status
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		listOptions();
	}

	@Override
	public void quit() {

	}
}
