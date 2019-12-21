package os.server.players;

public class Player {
	private String name;
	private int age;
	private int playerId;
	private int clubId;
	private int agentId;
	private double valuation;
	private PlayerStatus status;
	private Position position;
	
	public Player() { }
	
	public Player(String name, int age, int playerId, int clubId, int agentId, double valuation, PlayerStatus status, Position position) {
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

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
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
		return "Player [name=" + name + ", age=" + age + ", playerId=" + playerId + ", clubId=" + clubId + ", agentId="
				+ agentId + ", valuation=" + valuation + ", status=" + status + ", position=" + position + "]";
	}
}
