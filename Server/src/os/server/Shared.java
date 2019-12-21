package os.server;

import java.util.ArrayList;
import java.util.List;

import os.server.players.Player;
import os.server.users.Agent;
import os.server.users.Club;

public class Shared {
	private List<Player> players;
	private List<Agent> agents;
	private List<Club> clubs;
	
	public Shared() {
		players = new ArrayList<>();
		agents = new ArrayList<>();
		clubs = new ArrayList<>();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Agent> getAgents() {
		return agents;
	}

	public List<Club> getClubs() {
		return clubs;
	}
	
	public synchronized void addPlayer() {
		
	}
	
	public synchronized void addAgent() {
		
	}
}
