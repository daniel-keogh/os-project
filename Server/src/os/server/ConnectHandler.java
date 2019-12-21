package os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
			
			// System.out.println("client>" + msg);
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
}
