package os.server.players;

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

	public Player(String playerId, String clubId, String agentId, String name, int age, double valuation,
			Position position, PlayerStatus status) {
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

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getClubId() {
		return clubId;
	}

	public void setClubId(String clubId) {
		this.clubId = clubId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getValuation() {
		return valuation;
	}

	public void setValuation(double valuation) {
		this.valuation = valuation;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %s %d %.2f %s %s", playerId, clubId, agentId, name, age, valuation, position, status);
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
