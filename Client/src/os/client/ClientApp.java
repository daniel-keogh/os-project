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
	private String message;

	private static final String IP_ADDRESS = "127.0.0.1";
	private static final int PORT = 10000;
	private static final Scanner console = new Scanner(System.in);

	private void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			// System.out.println("client>" + msg);
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
			
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}
	}
}
