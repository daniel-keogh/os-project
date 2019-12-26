package os.server.menus;

import java.util.ArrayList;
import java.util.List;

import os.server.ConnectHandler;
import os.server.InsufficientFundsException;
import os.server.Shared;
import os.server.players.Player;
import os.server.players.PlayerStatus;
import os.server.players.Position;
import os.server.users.Club;

public class ClubMenu {
	private ConnectHandler ch;
	private Shared sharedObj;
	private Club usersClub;
	
	public ClubMenu(ConnectHandler ch) {
		this.ch = ch;
		sharedObj = ch.getSharedObject();
		usersClub = sharedObj.getClubFromID(ch.getCurrentUser().getId());
	}
	
	public void show() {
		String option;
		
		do {
			option = ch.receiveMessage();
			
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
				case "E":
					displayAllPlayers();
					break;
				default:
					break;
			}
		} while (option.charAt(0) != 'Q');
	}

	private void searchAllByPosition() {
		List<Player> temp = new ArrayList<>();
		
		ch.sendMessage("Search all by position...\n$ Enter position (GOALKEEPER, DEFENDER, MIDFIELDED, ATTACKER): ");
		
		try {
			Position pos = Position.valueOf(ch.receiveMessage().toUpperCase());
			
			temp = sharedObj.searchAllByPosition(pos);
			
			if (temp.size() > 0) {
				ch.sendMessage(temp.toString());
				return;
			}
		} catch (IllegalArgumentException e) { }
		
		ch.sendMessage("No players found for that position");
	}

	private void searchAllForSale() {			
		List<Player> temp = sharedObj.searchAllForSale(usersClub);
		
		ch.sendMessage("Search all for sale...\n"+ (temp.size() == 0 ? "No players found" : temp.toString()));
	}

	private void suspendResumeSale() {
		Player p = new Player();
		
		ch.sendMessage("Suspend / Resume sale...\n$ Enter the player ID: ");
		p.setPlayerId(ch.receiveMessage());
		
		try {			
			ch.sendMessage("$ Enter the new player status (FOR_SALE, SALE_SUSPENDED): ");
			PlayerStatus ps = PlayerStatus.valueOf(ch.receiveMessage().toUpperCase());
			
			if (!sharedObj.getPlayers().contains(p)) {
				throw new InvalidIdException("The entered Player ID does not exist");
			} else {
				// Prevent the user from modifying other clubs players
				for (Player player : sharedObj.getPlayers()) {
					if (p.equals(player)) {
						if (!usersClub.getId().equalsIgnoreCase(player.getClubId())) {
							throw new InvalidIdException("That player does not belong to your club");
						}
					}
				}
			}
			
			sharedObj.suspendResumeSale(p, ps);
			
			ch.sendMessage("Updated player status");
		} catch (IllegalArgumentException e) { 
			ch.sendMessage("[Error] Invalid status entered");
		} catch (InvalidIdException e) {
			ch.sendMessage("[Error] "+ e.getMessage());
		}
	}

	private void purchasePlayer() {
		ch.sendMessage("Purchase player...\n$ Enter player ID: ");
		String id = ch.receiveMessage();
		
		try {
			Player p = sharedObj.getPlayerFromID(id);
			
			if (p == null) {
				throw new InvalidIdException();
			}

			// Prevent clubs from purchasing their own players.
			if (p.getClubId().equalsIgnoreCase(usersClub.getId())) {
				throw new InvalidIdException("You already own that player");
			}
			
			sharedObj.purchasePlayer(usersClub, p);
			
			ch.sendMessage(p.getName() +" has been purchased by "+ usersClub.getName());
		} catch (InsufficientFundsException | InvalidIdException e) {
			ch.sendMessage("[Error] "+ e.getMessage());
		}
	}
	
	private void displayAllPlayers() {
		List<Player> temp = sharedObj.getPlayers();
		
		if (temp.size() == 0) {
			ch.sendMessage("No players found");
		} else {
			ch.sendMessage(temp.toString());	
		}
	}
}
