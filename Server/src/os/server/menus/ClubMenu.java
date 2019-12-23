package os.server.menus;

import os.server.ConnectHandler;

public class ClubMenu {
	private ConnectHandler ch;
	
	public ClubMenu(ConnectHandler ch) {
		this.ch = ch;
	}
	
	public void show() {
		String option;
		
		do {
			option = (String)ch.receiveMessage();
			
			switch (option) {
				case "A":
					searchAllByPosition();
					break;
				case "B":
					searchAllForSale();
					break;
				case "C":
					SuspendResumeSale();
					break;
				case "D":
					purchasePlayer();
					break;
				default:
					break;
			}
		} while (option.charAt(0) != 'Q');
	}

	private void searchAllByPosition() {
		// TODO Auto-generated method stub
		
	}

	private void searchAllForSale() {
		// TODO Auto-generated method stub
		
	}

	private void SuspendResumeSale() {
		// TODO Auto-generated method stub
		
	}

	private void purchasePlayer() {
		// TODO Auto-generated method stub
		
	}
}
