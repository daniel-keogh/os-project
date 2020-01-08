package os.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	private static final int PORT = 10000;
	private static final int BACKLOG = 10;
	
	public static void main(String[] args) {
		int clientId = 0;

		try (ServerSocket listener = new ServerSocket(PORT, BACKLOG)) {	 
			Shared sharedObj = new Shared();
			
			while (true) {
				System.out.println(String.format("Main thread listening on port %d for incoming new connections...", PORT));
				
				Socket newConnection = listener.accept();
				
				System.out.println("New connection received and spanning a thread...");
				
				Runnable ch = new ConnectHandler(newConnection, clientId, sharedObj);
				Thread t = new Thread(ch);
				t.start();
				
				clientId++;
			}
		} catch (IOException e) {
			System.out.println("[Error] Socket not opened.");
			e.printStackTrace();
		}
	}
}
