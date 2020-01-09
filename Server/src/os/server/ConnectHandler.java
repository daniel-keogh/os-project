package os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import os.server.menus.AgentMenu;
import os.server.menus.ClubMenu;
import os.server.menus.Menu;
import os.server.users.Agent;
import os.server.users.Club;
import os.server.users.User;

public class ConnectHandler implements Runnable {
	private Shared sharedObj;
	private Socket connection;
	private int socketId;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private User currentUser;

	public ConnectHandler(Socket connection, int socketId, Shared sharedObj) {
		this.sharedObj = sharedObj;
		this.connection = connection;
		this.socketId = socketId;
	}
	
	public Shared getSharedObject() {
		return sharedObj;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}

	public void sendMessage(Object msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receiveMessage() {
		try {
			return (String) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();

			in = new ObjectInputStream(connection.getInputStream());

			System.out.println(String.format("Connection %d from IP address: %s", socketId, connection.getInetAddress()));

			registerOrLogin();
			
			// One logged-in/registered, get the rest of the user's info. and
			// then pass the current instance of ConnectHandler to the appropriate Menu.
			Menu menu;
			if (currentUser instanceof Agent) {
				currentUser = sharedObj.getAgentFromID(currentUser.getId());
				menu = new AgentMenu(this);
			} else {
				currentUser = sharedObj.getClubFromID(currentUser.getId());
				menu = new ClubMenu(this);
			}
			
			menu.show();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void registerOrLogin() {
		String incomingMsg;
		boolean success = false;
		
		do {
			sendMessage("$ Do you want to Register (R) or Login (L):");
			incomingMsg = receiveMessage();
		} while (!incomingMsg.equalsIgnoreCase("L") && !incomingMsg.equalsIgnoreCase("R"));
		
		do {
			sendMessage("$ Are you an agent (A) or a club (C)?");
			if (receiveMessage().equalsIgnoreCase("A")) {
				currentUser = new Agent();
			} else {
				currentUser = new Club();
			}

			sendMessage("$ Enter name:");
			currentUser.setName(receiveMessage());

			sendMessage("$ Enter ID:");
			currentUser.setId(receiveMessage());
			
			try {
				// Extra steps needed before registering
				if (incomingMsg.equalsIgnoreCase("R")) {
					sendMessage("$ Enter email address:");
					currentUser.setEmail(receiveMessage());
					
					// More extra steps needed if the user is a club
					if (currentUser instanceof Club) {
						// Get funds
						boolean isValidFunds = false;
						
						do {
							try {
								sendMessage("$ Enter funds: ");
								((Club) currentUser).setFunds(Double.parseDouble(receiveMessage()));
								
								isValidFunds = true;
								sendMessage(isValidFunds);
							} catch (NumberFormatException e) {
								sendMessage(isValidFunds);
								sendMessage("Invalid number format. Try again.");
							}
						} while (!isValidFunds);
					}
					
					sharedObj.register(currentUser);	
				} else {
					sharedObj.login(currentUser);
				}
				
				success = true;
				sendMessage(success);
			} catch (IllegalArgumentException e) {
				sendMessage(success);
				sendMessage(e.getMessage());
			}
		} while (!success);
	}
}
