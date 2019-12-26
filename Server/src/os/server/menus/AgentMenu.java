package os.server.menus;

import java.util.List;

import os.server.ConnectHandler;
import os.server.Shared;
import os.server.players.Player;
import os.server.players.PlayerStatus;
import os.server.players.Position;
import os.server.users.Club;

public class AgentMenu {
	private ConnectHandler ch;
	private Shared sharedObj;
	
	public AgentMenu(ConnectHandler ch) {
		this.ch = ch;
		sharedObj = ch.getSharedObject();
	}

	public void show() {
		String option;
		
		do {
			option = ch.receiveMessage();
			
			switch (option) {
				case "A":
					addPlayer();
					break;
				case "B":
					updatePlayerValuation();
					break;
				case "C":
					updatePlayerStatus();
					break;
				case "D":
					displayAllPlayers();
					break;
				default:
					break;
			}
		} while (option.charAt(0) != 'Q');
	}

	private void addPlayer() {
		Player p = new Player();
		p.setAgentId(ch.getCurrentUser().getId());
		
		ch.sendMessage("Add Player...\n$ Enter player Name: ");
		String name = ch.receiveMessage();
		
		ch.sendMessage("$ Enter player age: ");
		String age = ch.receiveMessage();
		
		ch.sendMessage("$ Enter club ID: ");
		String clubId = ch.receiveMessage();
		
		ch.sendMessage("$ Enter player valuation: ");
		String valuation = ch.receiveMessage();	
		
		ch.sendMessage("$ Enter player status (FOR_SALE, SOLD, SALE_SUSPENDED): ");
		String status = ch.receiveMessage();
		
		ch.sendMessage("$ Enter player position (GOALKEEPER, DEFENDER, MIDFIELDER, ATTACKER): ");
		String position = ch.receiveMessage();
		
		try {
			Club c = new Club();
			c.setId(clubId);
			
			// Make sure the entered Club ID exists
			if (!sharedObj.getClubs().contains(c)) {
				throw new InvalidIdException();
			}
			
			p.setName(name)
				.setAge(Integer.parseInt(age))
				.setClubId(clubId)
				.setValuation(Double.parseDouble(valuation))
				.setStatus(PlayerStatus.valueOf(status.toUpperCase()))
				.setPosition(Position.valueOf(position.toUpperCase()));
			
			sharedObj.addPlayer(p);
			
			ch.sendMessage("Player added successfully");
		} catch (IllegalArgumentException e) {
			ch.sendMessage("[Error] The server encountered an error trying to add that player.");
		} catch (InvalidIdException e) {
			ch.sendMessage("[Error] "+ e.getMessage());
		}
	}
	
	private void updatePlayerValuation() {
		Player p = new Player();
		
		ch.sendMessage("Update Player Valuation...\n$ Enter player ID: ");
		p.setPlayerId(ch.receiveMessage());
		
		try {
			ch.sendMessage("$ Enter new player valuation: ");
			p.setValuation(Double.parseDouble(ch.receiveMessage()));
			
			// Make sure player ID exists
			if (!sharedObj.getPlayers().contains(p)) {
				throw new InvalidIdException();
			}
			
			if (!sharedObj.getPlayersAgentId(p).equalsIgnoreCase(ch.getCurrentUser().getId())) {
				throw new InvalidIdException("You do not represent this player");
			}
			
			sharedObj.updatePlayerValuation(p);
			
			ch.sendMessage("Valuation updated successfully");
		} catch (NumberFormatException e) {
			ch.sendMessage("[Error] Invalid valuation entered");
		} catch (InvalidIdException e) {
			ch.sendMessage("[Error] "+ e.getMessage());
		}
	}
	
	private void updatePlayerStatus() {
		Player p = new Player();
		
		ch.sendMessage("Update Player Status...\n$ Enter player ID: ");
		p.setPlayerId(ch.receiveMessage());
		
		try {
			ch.sendMessage("$ Enter new player status (FOR_SALE, SOLD, SALE_SUSPENDED): ");
			p.setStatus(PlayerStatus.valueOf(ch.receiveMessage().toUpperCase()));
			
			if (!sharedObj.getPlayers().contains(p)) {
				throw new InvalidIdException();
			}
			
			if (!sharedObj.getPlayersAgentId(p).equalsIgnoreCase(ch.getCurrentUser().getId())) {
				throw new InvalidIdException("You do not represent this player");
			}
			
			sharedObj.updatePlayerStatus(p);
			
			ch.sendMessage("Updated player status");
		} catch (IllegalArgumentException e) {
			ch.sendMessage("[Error] Invalid status entered");
		} catch (InvalidIdException e) {
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
