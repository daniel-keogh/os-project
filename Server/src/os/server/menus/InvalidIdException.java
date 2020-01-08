package os.server.menus;

public class InvalidIdException extends RuntimeException {
	private static final long serialVersionUID = 5837173913693231717L;

	public InvalidIdException() { 
		super("The given ID does not exist or is otherwise invalid.");
	}
	
	public InvalidIdException(String message) {
		super(message);
	}
	
	public InvalidIdException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidIdException(Throwable cause) {
		super(cause);
	}
}
