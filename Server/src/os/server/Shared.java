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
	
	/**
	 * Schedules a TimerTask to carry out a backup of the player and club_agents text files every couple of minutes.
	 * 
	 * @see TimerTask
	 */
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

	/**
	 * Reads the players.txt file and adds each entry to the players ArrayList.
	 * 
	 * @throws FileNotFoundException
	 */
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

	/**
	 * Reads the club_agents.txt file and adds each club/agent to their corresponding list.
	 * 
	 * @throws FileNotFoundException
	 */
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

	/**
	 * Check the given user's credentials and try to log them in.
	 * 
	 * @param user - The user to log-in
	 * 
	 * @throws IllegalArgumentException If the user's ID and/or name is incorrect
	 */
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

	/**
	 * Registers a new User.
	 * Agent's IDs must begin with a capital 'A', and Club's IDs must begin with a capital 'C'.
	 * If not, the appropriate letter will be appended to the ID given.
	 * 
	 * @param user - The user to register
	 * 
	 * @throws IllegalArgumentException If the User's ID is already registered
	 */
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
	
	/**
	 * Gets a copy of a Player with the given ID.
	 * 
	 * @param id - The ID to search for
	 * @return The Player with the given ID
	 */
	public synchronized Player getPlayerFromID(String id) {
		Player p = new Player();
		p.setPlayerId(id);
		
		if (players.contains(p)) {
			p = new Player(players.get(players.indexOf(p)));
			return p;
		}
		
		return null;
	}
	
	/**
	 * Gets a copy of a Club with the given ID.
	 * 
	 * @param id - The ID to search for
	 * @return The Club with the given ID
	 */
	public synchronized Club getClubFromID(String id) {
		Club c = new Club();
		c.setId(id);
		
		if (clubs.contains(c)) {
			c = new Club(clubs.get(clubs.indexOf(c)));
			return c;
		}
		
		return null;
	}
	
	/**
	 * Gets a copy of a Agent with the given ID.
	 * 
	 * @param id - The ID to search for
	 * @return The Agent with the given ID
	 */
	public synchronized Agent getAgentFromID(String id) {
		Agent a = new Agent();
		a.setId(id);
		
		if (agents.contains(a)) {
			a = new Agent(agents.get(agents.indexOf(a)));
			return a;
		}
		
		return null;
	}

	/**
	 * Adds a new player to the players List. The playerId is auto-generated.
	 * 
	 * @param p - The player to add
	 */
	public synchronized void addPlayer(Player p) {
		// Randomly generate player ID
		Integer rand = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
		
		p.setPlayerId("P" + rand).setName(p.getName().replaceAll(" ", "_"));
		players.add(p);
	}

	/**
	 * Adds a new agent to the agents List. A capital 'A' is appended to the ID if not present already.
	 * 
	 * @param agent - The agent to add.
	 */
	public synchronized void addAgent(Agent agent) {
		if (agent.getId().charAt(0) != 'A') {
			agent.setId("A" + agent.getId());
		}
		agents.add(agent);
	}
	
	/**
	 * Updates a player's valuation.
	 * 
	 * @param p - The player whose valuation is to be updated
	 * @param valuation - The new valuation for <code>p</code>
	 */
	public synchronized void updatePlayerValuation(Player p, double valuation) {
		if (players.contains(p)) {
			players.get(players.indexOf(p)).setValuation(valuation);
		}
	}

	/**
	 * Updates a player's status.
	 * 
	 * @param p - The player whose status is to be updated
	 * @param ps - The new status for <code>p</code>
	 */
	public synchronized void updatePlayerStatus(Player p, PlayerStatus ps) {
		if (players.contains(p)) {
			players.get(players.indexOf(p)).setStatus(ps);
		}
	}
	
	/**
	 * Returns a List of all players with the given {@link Position}.
	 * 
	 * @param pos - The Position to search for
	 * @return The List of players who play in that position.
	 */
	public synchronized List<Player> searchAllByPosition(Position pos) {
		List<Player> temp = new ArrayList<>();
		
		players.forEach(p -> {
			if (p.getPosition().equals(pos)) {
				temp.add(p);
			}
		});
		
		return temp;
	}
	
	/**
	 * Returns a list of all players who have a {@link PlayerStatus} of <code>FOR_SALE</code>.
	 * 
	 * @param c - The club to search for
	 * @return The List of players for sale
	 */
	public synchronized List<Player> searchAllForSale(Club c) {
		List<Player> temp = new ArrayList<>();
		
		players.forEach(p -> {
			if (p.getStatus() == PlayerStatus.FOR_SALE && p.getClubId().equalsIgnoreCase(c.getId())) {
				temp.add(p);
			}
		});
		
		return temp;
	}

	/**
	 * Suspends or resumes the sale of a {@link Player} by updating their {@link PlayerStatus} 
	 * 
	 * @param p - The player to update
	 * @param ps - The new status for that player
	 * 
	 * @throws IllegalArgumentException if an attempt is made to change PlayerStatus to <code>SOLD</code> 
	 */
	public synchronized void suspendResumeSale(Player p, PlayerStatus ps) {
		if (ps == PlayerStatus.SOLD) {
			throw new IllegalArgumentException();
		}
		
		players.get(players.indexOf(p)).setStatus(ps);
	}

	/**
	 * Transfers a {@link Player} from one {@link Club} to another.
	 * 
	 * @param buyer - The buying Club
	 * @param p - The Player who is being bought/sold
	 * 
	 * @throws InsufficientFundsException if the buying Club does not have enough funds to complete the transaction
	 */
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
