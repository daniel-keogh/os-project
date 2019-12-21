package os.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
	private Socket connection;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String incomingMsg;
	private String outgoingMsg;

	private static final String IP_ADDRESS = "127.0.0.1";
	private static final int PORT = 10000;
	private static final Scanner console = new Scanner(System.in);

	private void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
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
				incomingMsg = (String)in.readObject();
				System.out.println(incomingMsg);
				outgoingMsg = console.next();
				sendMessage(outgoingMsg);
			} while(!outgoingMsg.equalsIgnoreCase("R") && !outgoingMsg.equalsIgnoreCase("L"));
			
			String userType = new String(outgoingMsg);
			Boolean success = false;
			
			if (outgoingMsg.equalsIgnoreCase("R")) {
				do {
					// Send user type (Agent or Club)
					incomingMsg = (String)in.readObject();
					System.out.println(incomingMsg);
					outgoingMsg = console.next();
					sendMessage(outgoingMsg);
					
					// Send name
					incomingMsg = (String)in.readObject();
					System.out.println(incomingMsg);
					outgoingMsg = console.next();
					outgoingMsg += console.nextLine();
					sendMessage(outgoingMsg);
					
					// Send id
					incomingMsg = (String)in.readObject();
					System.out.println(incomingMsg);
					outgoingMsg = console.next();
					sendMessage(outgoingMsg);
					
					// Send email
					incomingMsg = (String)in.readObject();
					System.out.println(incomingMsg);
					outgoingMsg = console.next();
					sendMessage(outgoingMsg);
					
					if (userType.equals("C")) {
						// Send funds
						incomingMsg = (String)in.readObject();
						System.out.println(incomingMsg);
						outgoingMsg = console.next();
						sendMessage(outgoingMsg);
					}
					
					success = (Boolean) in.readObject();
					System.out.println(String.format("Registration %s\n", success ? "successful." : "unsuccessful. Try again."));
				} while (!success);
			} else {
				do {
					// Send user type (Agent or Club)
					incomingMsg = (String)in.readObject();
					System.out.println(incomingMsg);
					outgoingMsg = console.next();
					sendMessage(outgoingMsg);
					
					// Send name
					incomingMsg = (String)in.readObject();
					System.out.println(incomingMsg);
					outgoingMsg = console.next();
					outgoingMsg += console.nextLine();
					sendMessage(outgoingMsg);
					
					// Send id
					incomingMsg = (String)in.readObject();
					System.out.println(incomingMsg);
					outgoingMsg = console.next();
					sendMessage(outgoingMsg);
					
					success = (Boolean)in.readObject();
					System.out.println(String.format("Login %s\n", success ? "successful." : "unsuccessful. Try again."));
				} while (!success);
			}
			
			out.close();
			in.close();
			connection.close();
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
