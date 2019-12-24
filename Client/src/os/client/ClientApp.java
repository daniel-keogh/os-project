package os.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import os.client.menus.AgentMenu;
import os.client.menus.ClubMenu;
import os.client.menus.Menu;

public class ClientApp {
	private Socket connection;
	private String incomingMsg;
	private String outgoingMsg;

	private static ObjectOutputStream out;
	private static ObjectInputStream in;
	
	private static final String IP_ADDRESS = "127.0.0.1";
	private static final int PORT = 10000;
	private static final Scanner console = new Scanner(System.in);

	public static void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object receiveMessage() {
		try {
			return in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void launch() {
		try {
			connection = new Socket(IP_ADDRESS, PORT);

			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();

			in = new ObjectInputStream(connection.getInputStream());

			System.out.println("Client Side ready to communicate");
			
			// Register / Login
			do {
				incomingMsg = (String)receiveMessage();
				System.out.println(incomingMsg);
				outgoingMsg = console.next();
				sendMessage(outgoingMsg);
			} while(!outgoingMsg.equalsIgnoreCase("R") && !outgoingMsg.equalsIgnoreCase("L"));
			
			char action = outgoingMsg.toUpperCase().charAt(0);
			Boolean success = false;
			char userType;
			
			do {
				// Send user type (Agent or Club)
				incomingMsg = (String)receiveMessage();
				System.out.println(incomingMsg);
				outgoingMsg = console.next();
				sendMessage(outgoingMsg);
				
				userType = outgoingMsg.toUpperCase().charAt(0);
			
				// Send name
				incomingMsg = (String)receiveMessage();
				System.out.println(incomingMsg);
				outgoingMsg = console.next();
				outgoingMsg += console.nextLine();
				sendMessage(outgoingMsg);
				
				// Send id
				incomingMsg = (String)receiveMessage();
				System.out.println(incomingMsg);
				outgoingMsg = console.next();
				sendMessage(outgoingMsg);	
				
				// Extra steps needed for registration
				if (action == 'R') {
					// Send email
					incomingMsg = (String)receiveMessage();
					System.out.println(incomingMsg);
					
					do {
						outgoingMsg = console.next();
						
						if (!ValidateEmail.isValid(outgoingMsg)) {
							System.out.println("[Error] Invalid Email. Try again.");
						} else {
							break;
						}
					} while (true);
					
					sendMessage(outgoingMsg);
					
					if (userType == 'C') {
						// Send funds
						incomingMsg = (String)receiveMessage();
						System.out.println(incomingMsg);
						outgoingMsg = console.next();
						sendMessage(outgoingMsg);
					}
				}
				
				success = (Boolean) receiveMessage();
				
				// Get appropriate error message if there is one
				if (!success) {
					System.out.println("[Error] "+ (String) receiveMessage());
				} else {
					System.out.println((action == 'R' ? "Registration" : "Login") + " successful.");
				}
				
			} while (!success);
			
			Menu menu;
			if (userType == 'A') {
				menu = new AgentMenu();
			} else {
				menu = new ClubMenu();
			}
			
			menu.show();
			
			out.close();
			in.close();
			connection.close();
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}
	}
}
