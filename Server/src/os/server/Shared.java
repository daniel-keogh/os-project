package os.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import os.server.players.Player;
import os.server.players.PlayerStatus;
import os.server.players.Position;
import os.server.users.Agent;
import os.server.users.Club;
import os.server.users.User;

public class Shared extends TimerTask {
	private Timer saveListsTimer = new Timer();
	private List<Player> players = new ArrayList<>();
	private List<Agent> agents = new ArrayList<>();
	private List<Club> clubs = new ArrayList<>();

	private static final String CLUB_AGENTS_FILE = "club_agents.txt";
	private static final String PLAYERS_FILE = "players.txt";

	public Shared() throws FileNotFoundException {
		loadPlayersList();
		loadClubAgentsList();
		
		// Schedule to save the Lists to File every 5 mins
		saveListsTimer.schedule(this, 300000, 300000);
	}
	
	@Override
	public synchronized void run() {
		// TODO save Lists
		System.out.println("Inside shared run method");
	}

	private void loadPlayersList() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader(PLAYERS_FILE));

		while (in.hasNext()) {
			Player p = new Player(in.next(), in.nextInt(), in.next(), in.next(), in.next(), in.nextDouble(),
					PlayerStatus.valueOf(in.next()), Position.valueOf(in.next()));

			players.add(p);
		}

		in.close();
	}

	private void loadClubAgentsList() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader(CLUB_AGENTS_FILE));
		String id;
		User u;

		while (in.hasNext()) {
			id = in.next();

			if (id.charAt(0) == 'A') {
				u = new Agent(in.next(), id, in.next());
				agents.add((Agent) u);
			} else if (id.charAt(0) == 'C') {
				u = new Club(in.next(), id, in.next(), in.nextDouble());
				clubs.add((Club) u);
			}
		}

		in.close();
	}

	public synchronized boolean validateLogin(String name, String id) {
		char userType = Character.toUpperCase(id.charAt(0));
		name = name.replaceAll(" ", "_");

		if (userType == 'A') {
			Agent a = new Agent();
			a.setId(id).setName(name);

			return agents.contains(a);
		} else if (userType == 'C') {
			Club c = new Club();
			c.setId(id).setName(name);

			return clubs.contains(c);
		}

		return false;
	}

	public synchronized boolean register(User user) {		
		if (user instanceof Agent) {
			if (!agents.contains(user)) {
				agents.add((Agent) user);
				return true;
			}
		} else if (user instanceof Club) {
			if (!clubs.contains(user)) {
				clubs.add((Club) user);
				return true;
			}
		}

		return false;
	}

	public synchronized void addPlayer(Player player) {
		Integer rand = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
		
		player.setPlayerId("P" + rand);
		players.add(player);
	}

	public synchronized void addAgent(Agent agent) {
		if (agent.getId().charAt(0) != 'A') {
			agent.setId("A" + agent.getId());
		}
		agents.add(agent);
	}
}
