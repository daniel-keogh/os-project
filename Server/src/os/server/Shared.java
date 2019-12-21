package os.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import os.server.players.Player;
import os.server.players.PlayerStatus;
import os.server.players.Position;
import os.server.users.Agent;
import os.server.users.Club;
import os.server.users.User;

public class Shared {
	private List<Player> players = new ArrayList<>();
	private List<Agent> agents = new ArrayList<>();
	private List<Club> clubs = new ArrayList<>();
	
	private static final String CLUB_AGENTS_FILE = "club_agents.txt";
	private static final String PLAYERS_FILE = "players.txt";
	
	public Shared() throws FileNotFoundException {
		loadPlayersList();
		loadClubAgentsList();
	}
	
	private void loadPlayersList() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader(PLAYERS_FILE));
		
		while(in.hasNext()) {
			Player p = new Player(
				in.next().replaceAll("_", " "), 
				in.nextInt(), 
				in.next(), 
				in.next(), 
				in.next(), 
				in.nextDouble(), 
				PlayerStatus.valueOf(in.next()),
				Position.valueOf(in.next())
			);
			
			players.add(p);
		}
		
		in.close();
	}
	
	private void loadClubAgentsList() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader(CLUB_AGENTS_FILE));
		String id;
		User u;
		
		while(in.hasNext()) {
			id = in.next();
			
			System.out.println(id);
			
			if (id.charAt(0) == 'A') {
				u = new Agent(in.next().replaceAll("_", " "), id, in.next());
				agents.add((Agent) u);
			} else if (id.charAt(0) == 'C') {
				u = new Club(in.next().replaceAll("_", " "), id, in.next(), in.nextDouble());
				clubs.add((Club) u);
			}
		}
		
		in.close();
	}

	public synchronized boolean validateLogin(String name, String id) {			
		if (id.charAt(0) == 'A') {
			Agent a = new Agent();
			a.setId(id).setName(name);
			
			return agents.contains(a);
		} else {
			Club c = new Club();
			c.setId(id).setName(name);
			
			return clubs.contains(c);
		}
	}
	
	public void register(User user) throws FileNotFoundException {
//		try (PrintWriter pw = new PrintWriter(LOGIN_FILE)) {
//			pw.println(user.getName().replaceAll(" ", "_") +" "+ user.getId());
//		} catch (FileNotFoundException e) {
//			throw e;
//		} 
	}
	
	public synchronized void addPlayer() {
		
	}
	
	public synchronized void addAgent() {
		
	}
}
