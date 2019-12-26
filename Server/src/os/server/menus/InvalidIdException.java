package os.server.menus;

public class InvalidIdException extends Exception {
	public InvalidIdException() { 
		super("The given ID does not exist or is otherwise invalid.");
	}

	public InvalidIdException(String message) {
		super(message);
	}
}
