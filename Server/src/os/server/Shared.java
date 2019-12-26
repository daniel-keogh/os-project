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

public class Shared {
	private List<Player> players = new ArrayList<>();
	private List<Agent> agents = new ArrayList<>();
	private List<Club> clubs = new ArrayList<>();
	private Timer saveListsTimer = new Timer();

	private static final long BACKUP_FREQ = 120000;
	private static final String CLUB_AGENTS_FILE = "club_agents.txt";
	private static final String PLAYERS_FILE = "players.txt";

	public Shared() throws FileNotFoundException {
		loadPlayersList();
		loadClubAgentsList();
		scheduleBackup();
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
	
	private synchronized void scheduleBackup() {		
		saveListsTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Backing up files...");
				
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
		}, BACKUP_FREQ, BACKUP_FREQ);
	}

	private synchronized void loadPlayersList() throws FileNotFoundException {
		Scanner inFile = new Scanner(new FileReader(PLAYERS_FILE));

		Player p;
		while (inFile.hasNext()) {
			p = new Player();
			
			p.setPlayerId(inFile.next())
				.setClubId(inFile.next())
				.setAgentId(inFile.next())
				.setName(inFile.next())
				.setAge(inFile.nextInt())
				.setValuation(inFile.nextDouble())
				.setPosition(Position.valueOf(inFile.next()))
				.setStatus(PlayerStatus.valueOf(inFile.next()));

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

	public synchronized void login(User user) {
		user.setId(user.getId().toUpperCase());
		user.setName(user.getName().replaceAll(" ", "_"));
		
		List<? extends User> temp = (user instanceof Agent) ? agents : clubs;
		
		// Login is based on both the User's ID and their name
		boolean isValidLogin = temp.contains(user) && 
				temp.get(temp.indexOf(user)).getName().equalsIgnoreCase(user.getName());
	
		if (!isValidLogin) {
			throw new IllegalArgumentException("User ID or name is incorrect.");
		}
	}

	public synchronized void register(User user) {
		if (agents.contains(user) || clubs.contains(user)) {
			throw new IllegalArgumentException("A user with ID "+ user.getId() +" already exists.");
		}
		
		user.setName(user.getName().replaceAll(" ", "_"));
		
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
	
	public synchronized Player getPlayerFromID(String id) {
		Player p = new Player();
		p.setPlayerId(id);
		
		if (players.contains(p)) {
			p = new Player(players.get(players.indexOf(p)));
			return p;
		}
		
		return null;
	}
	
	public synchronized Club getClubFromID(String id) {
		Club c = new Club();
		c.setId(id);
		
		if (clubs.contains(c)) {
			c = new Club(clubs.get(clubs.indexOf(c)));
			return c;
		}
		
		return null;
	}
	
	public synchronized Agent getAgentFromID(String id) {
		Agent a = new Agent();
		a.setId(id);
		
		if (agents.contains(a)) {
			a = new Agent(agents.get(agents.indexOf(a)));
			return a;
		}
		
		return null;
	}

	public synchronized void addPlayer(Player p) {
		// Randomly generate player ID
		Integer rand = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
		
		p.setPlayerId("P" + rand).setName(p.getName().replaceAll(" ", "_"));
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
	
	public synchronized List<Player> searchAllByPosition(Position pos) {
		List<Player> temp = new ArrayList<>();
		
		players.forEach(p -> {
			if (p.getPosition().equals(pos)) {
				temp.add(p);
			}
		});
		
		return temp;
	}
	
	public synchronized List<Player> searchAllForSale(Club c) {
		List<Player> temp = new ArrayList<>();
		
		players.forEach(p -> {
			if (p.getStatus() == PlayerStatus.FOR_SALE && p.getClubId().equalsIgnoreCase(c.getId())) {
				temp.add(p);
			}
		});
		
		return temp;
	}

	public synchronized void suspendResumeSale(Player p, PlayerStatus ps) {
		if (ps == PlayerStatus.SOLD) {
			throw new IllegalArgumentException();
		}
		
		players.get(players.indexOf(p)).setStatus(ps);
	}

	public synchronized void purchasePlayer(Club buyer, Player p) throws InsufficientFundsException {
		if (buyer.getFunds() < p.getValuation()) {
			throw new InsufficientFundsException(buyer, p);
		}
		
		// Update selling clubs funds
		Club seller = new Club();
		seller.setId(p.getClubId());	
		seller = clubs.get(clubs.indexOf(seller));
		seller.setFunds(seller.getFunds() + p.getValuation());
		
		// Update purchasing clubs funds
		buyer.setFunds(buyer.getFunds() - p.getValuation());
		clubs.set(clubs.indexOf(buyer), buyer);
		
		// Update the player's status and club ID
		p.setStatus(PlayerStatus.SOLD);
		p.setClubId(buyer.getId());
		players.set(players.indexOf(p), p);
	}
}
