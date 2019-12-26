package os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import os.server.menus.AgentMenu;
import os.server.menus.ClubMenu;
import os.server.users.Agent;
import os.server.users.Club;
import os.server.users.User;

public class ConnectHandler implements Runnable {
	private Shared sharedObj;
	private Socket individualConn;
	private int socketId;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private User currentUser;

	public ConnectHandler(Socket individualConn, int socketId, Shared sharedObj) {
		this.sharedObj = sharedObj;
		this.individualConn = individualConn;
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
			out = new ObjectOutputStream(individualConn.getOutputStream());
			out.flush();

			in = new ObjectInputStream(individualConn.getInputStream());

			System.out.println(String.format("Connection %d from IP address: %s", socketId, individualConn.getInetAddress()));

			registerOrLogin();
			
			// One logged-in/registered, pass the current instance of ConnectHandler to the appropriate Menu.
			if (currentUser instanceof Agent) {
				new AgentMenu(this).show();
			} else {
				new ClubMenu(this).show();
			}
		} catch (IOException e) {
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
								sendMessage("[Error] Invalid number format. Try again.");
							}
						} while (!isValidFunds);
					}
					
					sharedObj.register(currentUser);	
				} else {
					sharedObj.login(currentUser);
				}
				
				success = true;
				sendMessage(success);
			} catch (FailedLoginException | FailedRegistrationException e) {
				sendMessage(success);
				sendMessage(e.getMessage());
			}
		} while (!success);
	}
}
