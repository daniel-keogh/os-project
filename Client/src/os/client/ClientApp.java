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
	
	//private static final String IP_ADDRESS = "51.140.245.236";
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
			
			UserType userType;
			UserAction userAction = outgoingMsg.toUpperCase().charAt(0) == 'R' ? UserAction.REGISTRATION : UserAction.LOGIN;
			Boolean success = false;
			
			do {
				// Send user type (Agent or Club)
				incomingMsg = (String)receiveMessage();
				System.out.println(incomingMsg);
				outgoingMsg = console.next();
				sendMessage(outgoingMsg);
				
				userType = outgoingMsg.toUpperCase().charAt(0) == 'A' ? UserType.AGENT : UserType.CLUB;
				
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
				if (userAction == UserAction.REGISTRATION) {
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
					
					if (userType == UserType.CLUB) {
						do {
							// Send funds
							incomingMsg = (String)receiveMessage();
							System.out.println(incomingMsg);
							outgoingMsg = console.next();
							sendMessage(outgoingMsg);
							
							// Get error message if there is one
							if (!((Boolean) receiveMessage())) {
								System.out.println((String) receiveMessage());
							} else {
								break;
							}
						} while (true);
					}
				}
				
				success = (Boolean) receiveMessage();
				
				// Get appropriate error message if there is one
				if (!success) {
					System.out.println("[Error] "+ receiveMessage());
				} else {
					System.out.println(userAction + " successful.");
				}
			} while (!success);
			
			// Display the menu
			Menu menu;
			if (userType == UserType.AGENT) {
				menu = new AgentMenu();
			} else {
				menu = new ClubMenu();
			}
			
			menu.show();
			
		} catch (IOException e) {
			System.out.println("[Error] Failed to connect to the server.");
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
	
	private enum UserType { AGENT, CLUB }
	
	private enum UserAction {
		LOGIN, REGISTRATION;
		
		@Override
		public String toString() {
			return super.toString().substring(0, 1) + super.toString().substring(1).toLowerCase();
		}
	}
}
