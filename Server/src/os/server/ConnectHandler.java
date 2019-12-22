package os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

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

					// Register the new user
					sharedObj.register(currentUser);
					
					sendMessage(Boolean.TRUE);
					break;
				} else {
					Boolean valid = sharedObj.validateLogin(currentUser.getName(), currentUser.getEmail());
					sendMessage(valid);
					
					if (valid) {
						break;
					}
				}
			} while (true);

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
