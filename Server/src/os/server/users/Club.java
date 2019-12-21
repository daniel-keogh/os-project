package os.server.users;

public class Club extends User {
	private double funds;
	
	public Club(String name, String id, String email, double funds) {
		super(name, id, email);
		this.funds = funds;
	}
	
	public double getFunds() {
		return funds;
	}
	public void setFunds(double funds) {
		this.funds = funds;
	}
}
