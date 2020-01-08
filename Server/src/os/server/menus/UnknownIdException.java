package os.server.menus;

/**
 *	Thrown to indicate the user has entered an ID for a User/Agent/Player that does not exist.
 */
public class UnknownIdException extends RuntimeException {
	private static final long serialVersionUID = 5837173913693231717L;

	public UnknownIdException() { 
		super();
	}
	
	public UnknownIdException(String message) {
		super(message);
	}
	
	public UnknownIdException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnknownIdException(Throwable cause) {
		super(cause);
	}
}
