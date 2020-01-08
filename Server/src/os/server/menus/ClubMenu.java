package os.server.menus;

import java.util.List;

import os.server.ConnectHandler;
import os.server.InsufficientFundsException;
import os.server.Shared;
import os.server.player.Player;
import os.server.player.PlayerStatus;
import os.server.player.Position;
import os.server.users.Club;

public class ClubMenu extends Menu {
	private ConnectHandler ch;
	private Shared sharedObj;
	private Club usersClub;
	
	public ClubMenu(ConnectHandler ch) {
		this.ch = ch;
		sharedObj = ch.getSharedObject();
		usersClub = (Club) ch.getCurrentUser();
	}
	
	@Override
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
		List<Player> temp;
		
		ch.sendMessage("Search all by position...\n$ Enter position (GOALKEEPER, DEFENDER, MIDFIELDED, ATTACKER): ");
		
		try {
			Position pos = Position.valueOf(ch.receiveMessage().toUpperCase());
			
			temp = sharedObj.searchAllByPosition(pos);
			
			if (temp.size() > 0) {
				ch.sendMessage(temp.toString());
				return;
			}
		} catch (IllegalArgumentException ignored) { }
		
		ch.sendMessage("No players found for that position");
	}

	private void searchAllForSale() {			
		List<Player> temp = sharedObj.searchAllForSale(usersClub);
		
		ch.sendMessage((temp.size() == 0 ? "No players found..." : temp.toString()));
	}

	private void suspendResumeSale() {
		ch.sendMessage("Suspend / Resume sale...\n$ Enter the player ID: ");
		String id = ch.receiveMessage();
		
		try {			
			ch.sendMessage("$ Enter the new player status (FOR_SALE, SALE_SUSPENDED): ");
			PlayerStatus ps = PlayerStatus.valueOf(ch.receiveMessage().toUpperCase());
			
			Player p = sharedObj.getPlayerFromID(id);
			
			if (p == null) {
				throw new UnknownIdException("The entered Player ID does not exist.");
			}

			// Prevent the user from modifying other clubs players
			if (!usersClub.getId().equalsIgnoreCase(p.getClubId())) {
				throw new UnknownIdException(p.getName() +" does not play for your club");
			}
			
			// Update status
			sharedObj.suspendResumeSale(p, ps);
			
			ch.sendMessage("Updated player status successfully");
		} catch (IllegalArgumentException e) { 
			ch.sendMessage("[Error] Invalid status entered");
		} catch (UnknownIdException e) {
			ch.sendMessage("[Error] "+ e.getMessage());
		}
	}

	private void purchasePlayer() {
		ch.sendMessage("Purchase player...\n$ Enter player ID: ");
		String id = ch.receiveMessage();
		
		try {
			Player p = sharedObj.getPlayerFromID(id);
			
			if (p == null) {
				throw new UnknownIdException("The entered Player ID does not exist.");
			}

			// Prevent clubs from purchasing their own players.
			if (p.getClubId().equalsIgnoreCase(usersClub.getId())) {
				throw new UnknownIdException("You already own "+ p.getName());
			}
			
			sharedObj.purchasePlayer(usersClub, p);
			
			ch.sendMessage(String.format("%s has been purchased by %s.\nYour remaining budget is: %.2f", p.getName(), usersClub.getName(), usersClub.getFunds()));
		} catch (InsufficientFundsException | UnknownIdException e) {
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
