package os.client.menus;

import static os.client.ClientApp.sendMessage;

import java.util.Scanner;

public class ClubMenu implements Menu {
	
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
				searchByPosition();
				break;
			case 'B':
				sendMessage("B");
				searchForSale();
				break;
			case 'C':
				sendMessage("C");
				SuspendResumeSale();
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

	private void searchByPosition() {
		// TODO Auto-generated method stub
		
	}
	
	private void searchForSale() {
		// TODO Auto-generated method stub
		
	}

	private void SuspendResumeSale() {
		// TODO Auto-generated method stub
		
	}

	private void purchasePlayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit() {

	}
}
