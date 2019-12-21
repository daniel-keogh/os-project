package os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import os.server.users.Agent;
import os.server.users.Club;
import os.server.users.User;

public class ConnectHandler implements Runnable {
	private Socket individualConn;
	private int socketId;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;

	public ConnectHandler(Socket individualConn, int socketId) {
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

			System.out.println(
					String.format("Connection %d from IP address: %s", socketId, individualConn.getInetAddress()));

			// Login
			do {
				sendMessage("$ Do you want to Register (R) or Login (L):");
				message = (String) in.readObject();
			} while (!message.equalsIgnoreCase("L") && !message.equalsIgnoreCase("R"));

			do {
				sendMessage("$ Are you an agent (A) or a club (C)?");
				String userType = (String) in.readObject();

				sendMessage("$ Enter name:");
				String name = (String) in.readObject();

				sendMessage("$ Enter ID:");
				String id = (String) in.readObject();

				sendMessage("$ Enter email address:");
				String email = (String) in.readObject();

				if (message.equalsIgnoreCase("R")) {
					User newUser;

					if (userType.equalsIgnoreCase("A")) {
						newUser = new Agent(name, id, email);
					} else {
						newUser = new Club(name, id, email, 0);
					}

					// TODO Register the new user

					sendMessage(Boolean.TRUE);
					break;
				} else {
					// TODO Login
					sendMessage(Boolean.TRUE);
					break;
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
