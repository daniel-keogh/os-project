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
	
	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}
	
	public List<Agent> getAgents() {
		return new ArrayList<Agent>(agents);
	}

	public List<Club> getClubs() {
		return new ArrayList<Club>(clubs);
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
		Scanner inFile = new Scanner(new FileReader(PLAYERS_FILE));

		Player p;
		while (inFile.hasNext()) {
			p = new Player(inFile.next(), inFile.next(), inFile.next(), inFile.next(), inFile.nextInt(), inFile.nextDouble(),
					Position.valueOf(inFile.next()), PlayerStatus.valueOf(inFile.next()));

			players.add(p);
		}

		inFile.close();
	}

	private synchronized void loadClubAgentsList() throws FileNotFoundException {
		Scanner inFile = new Scanner(new FileReader(CLUB_AGENTS_FILE));
		String id;
		User u;

		while (inFile.hasNext()) {
			id = inFile.next();

			if (id.charAt(0) == 'A') {
				u = new Agent(inFile.next(), id, inFile.next());
				agents.add((Agent) u);
			} else if (id.charAt(0) == 'C') {
				u = new Club(inFile.next(), id, inFile.next(), inFile.nextDouble());
				clubs.add((Club) u);
			}
		}

		inFile.close();
	}

	public synchronized void login(User user) throws FailedLoginException {
		user.setId(user.getId().toUpperCase());
		user.setName(user.getName().replaceAll(" ", "_"));
		
		List<? extends User> temp = (user instanceof Agent) ? agents : clubs;
		
		// Login is based on both the User's ID and their name
		boolean isValidLogin = temp.contains(user) && temp.get(temp.indexOf(user)).getName().equalsIgnoreCase(user.getName());
	
		if (!isValidLogin) {
			throw new FailedLoginException("User ID or name is incorrect.");
		}
	}

	public synchronized void register(User user) throws FailedRegistrationException {
		
		if (!agents.contains(user) && !clubs.contains(user)) {
			throw new FailedRegistrationException("A user with ID "+ user.getId() +" already exists.");
		}
		
		if (user instanceof Agent) {
			if (user.getId().charAt(0) != 'A') {
				user.setId("A"+ user.getId());
			}
			
			agents.add((Agent) user);
		} else {
			if (user.getId().charAt(0) != 'C') {
				user.setId("C"+ user.getId());
			}
			
			clubs.add((Club) user);
		}
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
	
	public synchronized List<Player> searchAllForSale(Club c) {
		List<Player> temp = new ArrayList<>();
		
		players.forEach(p -> {
			if (p.getStatus().equals(PlayerStatus.FOR_SALE) && p.getClubId().equalsIgnoreCase(c.getId())) {
				temp.add(p);
			}
		});
		
		return temp;
	}

	public synchronized void suspendResumeSale(Player p, PlayerStatus ps) {
		players.get(players.indexOf(p)).setStatus(ps);
	}

	public synchronized void purchasePlayer(Club c, Player p) throws InsufficientFundsException {
		if (c.getFunds() < p.getValuation()) {
			throw new InsufficientFundsException(c, p);
		}
		
		// Update selling clubs funds
		Club seller = new Club();
		seller.setId(p.getClubId());
		seller = clubs.get(clubs.indexOf(seller));
		seller.setFunds(seller.getFunds() + p.getValuation());
		
		c.setFunds(c.getFunds() - p.getValuation());
		p.setStatus(PlayerStatus.SOLD);
		p.setClubId(c.getId());
	}
}
