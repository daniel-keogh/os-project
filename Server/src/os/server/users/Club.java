package os.server.users;

public class Club extends User {
	private double funds;
	
	public Club() { }
	
	public Club(Club c) { 
		super(c);
		funds = c.funds;
	}
	
	public Club(String id, String name, String email, double funds) {
		super(id, name, email);
		this.funds = funds;
	}
	
	public double getFunds() {
		return funds;
	}
	
	public Club setFunds(double funds) {
		this.funds = funds;
		
		return this;
	}

	@Override
	public String toString() {
		return String.format("C %s %.2f", super.toString(), funds);
	}
}
