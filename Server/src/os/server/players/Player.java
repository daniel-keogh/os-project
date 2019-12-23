package os.server.players;

public class Player {
	private String name;
	private int age;
	private String playerId;
	private String clubId;
	private String agentId;
	private double valuation;
	private PlayerStatus status;
	private Position position;
	
	public Player() { }
	
	public Player(String name, int age, String playerId, String clubId, String agentId, double valuation, PlayerStatus status, Position position) {
		this.name = name;
		this.age = age;
		this.playerId = playerId;
		this.clubId = clubId;
		this.agentId = agentId;
		this.valuation = valuation;
		this.status = status;
		this.position = position;
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

	public double getValuation() {
		return valuation;
	}

	public void setValuation(double valuation) {
		this.valuation = valuation;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %s %s %s %s %s", playerId, clubId, agentId, name, age, valuation, position, status);
	}
}
