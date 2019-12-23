package os.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
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
	private List<Player> players = new ArrayList<>();
	private List<Agent> agents = new ArrayList<>();
	private List<Club> clubs = new ArrayList<>();
	private Timer saveListsTimer = new Timer();

	private static final String CLUB_AGENTS_FILE = "club_agents.txt";
	private static final String PLAYERS_FILE = "players.txt";

	public Shared() throws FileNotFoundException {
		loadPlayersList();
		loadClubAgentsList();
		
		// Schedule to save the Lists to File every 2 mins
		saveListsTimer.schedule(this, 120000, 120000);
	}
	
	@Override
	public synchronized void run() {
		System.out.println("Inside shared run method");
		
		try (PrintWriter outFile = new PrintWriter(PLAYERS_FILE)) {
			players.forEach(player -> outFile.println(player));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try (PrintWriter outFile = new PrintWriter(CLUB_AGENTS_FILE)) {
			agents.forEach(agent -> outFile.println(agent));
			clubs.forEach(club -> outFile.println(club));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private synchronized void loadPlayersList() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader(PLAYERS_FILE));

		while (in.hasNext()) {
			Player p = new Player(in.next(), in.next(), in.next(), in.next(), in.nextInt(), in.nextDouble(),
					Position.valueOf(in.next()), PlayerStatus.valueOf(in.next()));

			players.add(p);
		}

		in.close();
	}

	private synchronized void loadClubAgentsList() throws FileNotFoundException {
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
			a.setId(id.toUpperCase()).setName(name);

			return agents.contains(a);
		} else if (userType == 'C') {
			Club c = new Club();
			c.setId(id.toUpperCase()).setName(name);

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

	public synchronized void addPlayer(Player p) {
		Integer rand = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
		
		p.setPlayerId("P" + rand);
		players.add(p);
	}

	public synchronized void addAgent(Agent agent) {
		if (agent.getId().charAt(0) != 'A') {
			agent.setId("A" + agent.getId());
		}
		agents.add(agent);
	}
	
	public synchronized void updatePlayerValuation(Player p) {
		if (players.contains(p)) {
			players.get(players.indexOf(p)).setValuation(p.getValuation());
		}
	}

	public synchronized void updatePlayerStatus(Player p) {
		if (players.contains(p)) {
			players.get(players.indexOf(p)).setStatus(p.getStatus());
		}
	}
	
	public synchronized List<Player> searchAllByPosition(Club c, Position pos) {
		List<Player> temp = new ArrayList<>();
		
		players.forEach(p -> {
			if (p.getPosition().equals(pos) && p.getClubId().equalsIgnoreCase(c.getId())) {
				temp.add(p);
			}
		});
		
		return temp;
	}
	
	private synchronized List<Player> searchAllForSale(Club c) {
		List<Player> temp = new ArrayList<>();
		
		players.forEach(p -> {
			if (p.getStatus().equals(PlayerStatus.FOR_SALE) && p.getClubId().equalsIgnoreCase(c.getId())) {
				temp.add(p);
			}
		});
		
		return temp;
	}

	private synchronized void SuspendResumeSale(Player p, PlayerStatus ps) {
		players.get(players.indexOf(p)).setStatus(ps);
	}

	private synchronized void purchasePlayer(Club c, Player p) throws InsufficientFundsException {
		if (c.getFunds() < p.getValuation()) {
			throw new InsufficientFundsException(c, p);
		}
		
		c.setFunds(c.getFunds() - p.getValuation());
		p.setStatus(PlayerStatus.SOLD);
		p.setClubId(c.getId());
	}
}
