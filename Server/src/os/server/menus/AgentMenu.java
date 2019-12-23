package os.server.menus;

import java.io.IOException;

import os.server.ConnectHandler;
import os.server.players.Player;
import os.server.players.PlayerStatus;
import os.server.players.Position;

public class AgentMenu {
	private ConnectHandler ch;
	
	public AgentMenu(ConnectHandler ch) {
		this.ch = ch;
	}

	public void show() throws ClassNotFoundException, IOException {
		String option;
		
		do {
			option = (String)ch.receiveMessage();
			
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
				default:
					break;
			}
		} while (option.charAt(0) != 'Q');
	}

	private void addPlayer() {
		Player p = new Player();
		p.setAgentId(ch.getCurrentUser().getId());
		
		ch.sendMessage("Add Player...\n$ Enter player Name: ");
		p.setName((String)ch.receiveMessage());
		
		ch.sendMessage("$ Enter player age: ");
		p.setAge(Integer.parseInt((String)ch.receiveMessage()));
		
		ch.sendMessage("$ Enter club ID: ");
		p.setClubId((String)ch.receiveMessage());
		
		ch.sendMessage("$ Enter player valuation: ");
		p.setValuation(Double.parseDouble((String)ch.receiveMessage()));
		
		ch.sendMessage("$ Enter player status (FOR_SALE, SOLD, SALE_SUSPENDED): ");
		p.setStatus(PlayerStatus.valueOf((String)ch.receiveMessage()));
		
		ch.sendMessage("$ Enter player position (GOALKEEPER, DEFENDER, MIDFIELDER, ATTACKER): ");
		p.setPosition(Position.valueOf((String)ch.receiveMessage()));
		
		ch.getSharedObject().addPlayer(p);
	}
	
	private void updatePlayerValuation() {
		Player p = new Player();
		
		ch.sendMessage("Update Player Valuation...\n$ Enter player ID: ");
		p.setPlayerId((String)ch.receiveMessage());
		
		ch.sendMessage("$ Enter new player valuation: ");
		p.setValuation(Double.parseDouble((String)ch.receiveMessage()));
		
		ch.getSharedObject().updatePlayerValuation(p);
	}
	
	private void updatePlayerStatus() {
		Player p = new Player();
		
		ch.sendMessage("Update Player Status...\n$ Enter player ID: ");
		p.setPlayerId((String)ch.receiveMessage());
		
		ch.sendMessage("$ Enter new player status (FOR_SALE, SOLD, SALE_SUSPENDED): ");
		p.setStatus(PlayerStatus.valueOf((String)ch.receiveMessage()));
		
		ch.getSharedObject().updatePlayerStatus(p);
	}
}
