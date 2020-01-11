package os.server.menus;

/**
 *	Thrown to indicate the user has entered an ID for a User/Agent/Player that either does not exist, or is somehow invalid.
 */
public class InvalidIdException extends RuntimeException {
	private static final long serialVersionUID = 5837173913693231717L;

	public InvalidIdException() { 
		super();
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
