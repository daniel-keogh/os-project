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
	private String message;
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
	
	public Object receiveMessage() {
		try {
			return in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
				message = (String) receiveMessage();
			} while (!message.equalsIgnoreCase("L") && !message.equalsIgnoreCase("R"));

			do {
				sendMessage("$ Are you an agent (A) or a club (C)?");
				if (((String) receiveMessage()).equalsIgnoreCase("A")) {
					currentUser = new Agent();
				} else {
					currentUser = new Club();
				}

				sendMessage("$ Enter name:");
				currentUser.setName((String) receiveMessage());

				sendMessage("$ Enter ID:");
				currentUser.setId((String) receiveMessage());

				if (message.equalsIgnoreCase("R")) {
					sendMessage("$ Enter email address:");
					currentUser.setEmail((String) receiveMessage());
					
					if (currentUser instanceof Club) {
						// Get funds
						sendMessage("$ Enter funds: ");
						((Club) currentUser).setFunds(Double.parseDouble((String)receiveMessage()));
					}
					
					// Register the new user & send result
					boolean successfulReg = sharedObj.register(currentUser);
					sendMessage(successfulReg);
					
					if (successfulReg) {
						break;
					}
				} else {
					boolean isValidLogin = sharedObj.validateLogin(currentUser.getName(), currentUser.getId());
					sendMessage(isValidLogin);
					
					if (isValidLogin) {
						break;
					}
				}
			} while (true);
			
			// Pass the current instance of ConnectHandler to the appropriate Menu
			if (currentUser instanceof Agent) {
				new AgentMenu(this).show();
			} else {
				new ClubMenu(this).show();
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
}
