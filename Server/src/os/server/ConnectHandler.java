package os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import os.server.players.Player;
import os.server.players.PlayerStatus;
import os.server.players.Position;
import os.server.users.Agent;
import os.server.users.Club;
import os.server.users.User;

public class ConnectHandler implements Runnable {
	private Shared sharedObj;
	private Socket individualConn;
	private int socketId;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;
	private User currentUser;

	public ConnectHandler(Socket individualConn, int socketId, Shared sharedObj) {
		this.sharedObj = sharedObj;
		this.individualConn = individualConn;
		this.socketId = socketId;
	}

	public void sendMessage(Object msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			out = new ObjectOutputStream(individualConn.getOutputStream());
			out.flush();

			in = new ObjectInputStream(individualConn.getInputStream());

			System.out.println(String.format("Connection %d from IP address: %s", socketId, individualConn.getInetAddress()));

			// Login
			do {
				sendMessage("$ Do you want to Register (R) or Login (L):");
				message = (String) in.readObject();
			} while (!message.equalsIgnoreCase("L") && !message.equalsIgnoreCase("R"));

			do {
				sendMessage("$ Are you an agent (A) or a club (C)?");
				if (((String) in.readObject()).equalsIgnoreCase("A")) {
					currentUser = new Agent();
				} else {
					currentUser = new Club();
				}

				sendMessage("$ Enter name:");
				currentUser.setName((String) in.readObject());

				sendMessage("$ Enter ID:");
				currentUser.setId((String) in.readObject());

				if (message.equalsIgnoreCase("R")) {
					sendMessage("$ Enter email address:");
					currentUser.setEmail((String) in.readObject());
					
					if (currentUser instanceof Club) {
						// Get funds
						sendMessage("$ Enter funds: ");
						((Club) currentUser).setFunds(Double.parseDouble((String)in.readObject()));
					}
					
					// Register the new user & send result
					boolean successfulReg = sharedObj.register(currentUser);
					sendMessage(successfulReg);
					
					if (successfulReg) {
						break;
					}
				} else {
					boolean valid = sharedObj.validateLogin(currentUser.getName(), currentUser.getId());
					sendMessage(valid);
					
					if (valid) {
						break;
					}
				}
			} while (true);
			
			if (currentUser instanceof Agent) {
				showAgentMenu();
			} else {
				
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				individualConn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showAgentMenu() throws ClassNotFoundException, IOException {
		String option;
		
		do {
			option = (String)in.readObject();
			
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

	private void addPlayer() throws ClassNotFoundException, IOException {
		Player p = new Player();
		p.setAgentId(currentUser.getId());
		
		sendMessage("Add Player...\n$ Enter player Name: ");
		p.setName((String)in.readObject());
		
		sendMessage("$ Enter player age: ");
		p.setAge(Integer.parseInt((String)in.readObject()));
		
		sendMessage("$ Enter club ID: ");
		p.setClubId((String)in.readObject());
		
		sendMessage("$ Enter player valuation: ");
		p.setValuation(Double.parseDouble((String)in.readObject()));
		
		sendMessage("$ Enter player status (FOR_SALE, SOLD, SALE_SUSPENDED): ");
		p.setStatus(PlayerStatus.valueOf((String)in.readObject()));
		
		sendMessage("$ Enter player position (GOALKEEPER, DEFENDER, MIDFIELDER, ATTACKER): ");
		p.setPosition(Position.valueOf((String)in.readObject()));
		
		sharedObj.addPlayer(p);
	}
	
	private void updatePlayerValuation() throws ClassNotFoundException, IOException {
		Player p = new Player();
		
		sendMessage("Update Player Valuation...\n$ Enter player ID: ");
		p.setPlayerId((String)in.readObject());
		
		sendMessage("$ Enter new player valuation: ");
		p.setValuation(Double.parseDouble((String)in.readObject()));
		
		sharedObj.updatePlayerValuation(p);
	}
	
	private void updatePlayerStatus() throws ClassNotFoundException, IOException {
		Player p = new Player();
		
		sendMessage("Update Player Status...\n$ Enter player ID: ");
		p.setPlayerId((String)in.readObject());
		
		sendMessage("$ Enter new player status (FOR_SALE, SOLD, SALE_SUSPENDED): ");
		p.setStatus(PlayerStatus.valueOf((String)in.readObject()));
		
		sharedObj.updatePlayerStatus(p);
	}
}
