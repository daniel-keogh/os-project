package os.server.menus;

public class InvalidIdException extends RuntimeException {
	public InvalidIdException() { 
		super("The given ID does not exist or is otherwise invalid.");
	}

	public InvalidIdException(String message) {
		super(message);
	}
}
