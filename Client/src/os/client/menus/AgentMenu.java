package os.client.menus;

import java.util.Scanner;

public class AgentMenu implements Menu {

	private static final Scanner console = new Scanner(System.in);
	
	@Override
	public void showMenu() {
		char option;
		boolean cont = true;

		System.out.println("|==================================================|");
		System.out.println("|                    Agent Menu                    |");
		System.out.println("|==================================================|");
		System.out.println("| A. Add Player                                    |");
		System.out.println("| B. Update Player Valuation                       |");
		System.out.println("| C. Update Player Status                          |");
		System.out.println("| Q. Logout                                        |");
		System.out.println("|==================================================|");

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

	private boolean handleOption(char option) {
		switch (Character.toUpperCase(option)) {
		case 'A':
			addPlayer();
			break;
		case 'B':
			updatePlayerValuation();
			break;
		case 'C':
			updatePlayerStatus();
			break;
		case 'Q':
			quit();
		default:
			break;
		}

		return true;
	}

	private void addPlayer() {
		// TODO Auto-generated method stub

	}

	private void updatePlayerValuation() {
		// TODO Auto-generated method stub

	}

	private void updatePlayerStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void quit() {

	}
}
