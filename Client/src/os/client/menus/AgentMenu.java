package os.client.menus;

import java.util.Scanner;

import static os.client.ClientApp.receiveMessage;
import static os.client.ClientApp.sendMessage;

public class AgentMenu extends Menu {
	private String incomingMsg;
	private String outgoingMsg;
	
	private static final Scanner console = new Scanner(System.in);

	@Override
	public void show() {
		char option;
		boolean cont;

		listOptions();

		do {
			System.out.print("Select an option listed above: ");
			option = console.next().charAt(0);

			if (!Character.toString(option).matches("[a-dqA-DQ]")) {
				System.out.println("[Error] Please enter one of the options provided.");
			}

			cont = handleOption(option);
		} while (cont);
	}
	
	@Override
	protected void listOptions() {
		System.out.println();
		System.out.println("====================================================");
		System.out.println("|                    Agent Menu                    |");
		System.out.println("====================================================");
		System.out.println("| A. Add Player                                    |");
		System.out.println("| B. Update Player Valuation                       |");
		System.out.println("| C. Update Player Status                          |");
		System.out.println("| D. Display All Players                           |");
		System.out.println("| Q. Logout                                        |");
		System.out.println("====================================================");
	}

	@Override
	protected boolean handleOption(char option) {
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
			case 'D':
				sendMessage("D");
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

	private void addPlayer() {
		// Send player name
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		outgoingMsg += console.nextLine();
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
		
		// Send player position
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		outgoingMsg = console.next();
		sendMessage(outgoingMsg);
		
		// Get result (success/failure)
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		
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
		
		// Get result (success/failure)
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		
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
		
		// Get result (success/failure)
		incomingMsg = (String)receiveMessage();
		System.out.println(incomingMsg);
		
		listOptions();
	}
	
	private void displayAllPlayers() {
		incomingMsg = (String)receiveMessage();

		System.out.println(getPlayerTableHeading());
		for (String line : formatList(incomingMsg)) {
			System.out.println(line);
		}
		
		listOptions();
	}
}
