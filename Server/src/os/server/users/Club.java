package os.server.users;

public class Club extends User {
	private double funds;
	
	public Club() { }
	
	public Club(String name, String id, String email, double funds) {
		super(name, id, email);
		this.funds = funds;
	}
	
	public double getFunds() {
		return funds;
	}
	public Club setFunds(double funds) {
		this.funds = funds;
		
		return this;
	}
}
