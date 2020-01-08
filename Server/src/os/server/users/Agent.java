package os.server.users;

public class Agent extends User {
	public Agent() { }
	
	public Agent(Agent a) {
		super(a);
	}
	
	public Agent(String id, String name, String email) {
		super(id, name, email);
	}
	
	@Override
	public String toString() {
		return "A "+ super.toString();
	}
}
