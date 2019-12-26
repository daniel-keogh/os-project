package os.server.users;

public class Agent extends User {
	public Agent() { }
	
	public Agent(String name, String id, String email) {
		super(name, id, email);
	}
	
	public Agent(Agent a) {
		super(a);
	}
}
