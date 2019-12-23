package os.server.menus;

import java.util.List;

import os.server.ConnectHandler;
import os.server.InsufficientFundsException;
import os.server.players.Player;
import os.server.players.PlayerStatus;
import os.server.players.Position;
import os.server.users.Club;

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
					suspendResumeSale();
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
		List<Player> temp;
		
		ch.sendMessage("Search all by position...\n$ Enter position (GOALKEEPER, DEFENDER, MIDFIELDED, ATTACKER): ");
		Position pos = Position.valueOf((String) ch.receiveMessage());
		
		temp = ch.getSharedObject().searchAllByPosition((Club) ch.getCurrentUser(), pos);
		
		ch.sendMessage(temp.toString());
	}

	private void searchAllForSale() {			
		List<Player> temp = ch.getSharedObject().searchAllForSale((Club) ch.getCurrentUser());
		
		ch.sendMessage("Search all for sale...\n"+ temp.toString());
	}

	private void suspendResumeSale() {
		ch.sendMessage("Suspend / Resume sale...\n$ Enter the player ID: ");
		Player p = new Player();
		p.setPlayerId((String) ch.receiveMessage());
		
		ch.sendMessage("$ Enter the new player status (FOR_SALE, SOLD, SALE_SUSPENDED): ");
		PlayerStatus ps = PlayerStatus.valueOf((String) ch.receiveMessage());
		
		ch.getSharedObject().suspendResumeSale(p, ps);
	}

	private void purchasePlayer() {
		ch.sendMessage("Purchase player...\n$ Enter player ID: ");
		Player p = new Player();
		p.setPlayerId((String) ch.receiveMessage());
		
		try {
			ch.getSharedObject().purchasePlayer((Club) ch.getCurrentUser(), p);
			ch.sendMessage(p.getName() +" has been purchased by "+ ch.getCurrentUser().getName());
		} catch (InsufficientFundsException e) {
			ch.sendMessage(e.getMessage());
		}
	}
}
