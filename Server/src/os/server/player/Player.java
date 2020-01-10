package os.server.player;

public class Player {
	private String playerId;
	private String clubId;
	private String agentId;
	private String name;
	private int age;
	private double valuation;
	private Position position;
	private PlayerStatus status;
	
	public Player() { }

	public Player(Player p) {
		playerId = p.playerId;
		clubId = p.clubId;
		agentId = p.agentId;
		name = p.name;
		age = p.age;
		valuation = p.valuation;
		position = p.position;
		status = p.status;
	}
	
	public Player(String playerId, String clubId, String agentId, String name, int age, double valuation, Position position, PlayerStatus status) {
		this.playerId = playerId;
		this.clubId = clubId;
		this.agentId = agentId;
		this.name = name;
		this.age = age;
		this.valuation = valuation;
		this.position = position;
		this.status = status;
	}

	public String getPlayerId() {
		return playerId;
	}

	public Player setPlayerId(String playerId) {
		this.playerId = playerId;
		
		return this;
	}

	public String getClubId() {
		return clubId;
	}

	public Player setClubId(String clubId) {
		this.clubId = clubId;
		
		return this;
	}

	public String getAgentId() {
		return agentId;
	}

	public Player setAgentId(String agentId) {
		this.agentId = agentId;
		
		return this;
	}

	public String getName() {
		return name;
	}

	public Player setName(String name) {
		this.name = name;
		
		return this;
	}

	public int getAge() {
		return age;
	}

	public Player setAge(int age) {
		this.age = age;
		
		return this;
	}

	public double getValuation() {
		return valuation;
	}

	public Player setValuation(double valuation) {
		this.valuation = valuation;
		
		return this;
	}

	public Position getPosition() {
		return position;
	}

	public Player setPosition(Position position) {
		this.position = position;
		
		return this;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public Player setStatus(PlayerStatus status) {
		this.status = status;
		
		return this;
	}

	@Override
	public String toString() {
		return String.format("%-12s   %-10s  %-10s  %-25s  %3d  %12.2f  %-10s  %s", playerId, clubId, agentId, name, age, valuation, position, status);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerId == null) {
			if (other.playerId != null)
				return false;
		} else if (!playerId.equals(other.playerId))
			return false;
		return true;
	}
}
