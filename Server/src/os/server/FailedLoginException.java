package os.server;

public class FailedLoginException extends Exception {

	public FailedLoginException() { }
	
	public FailedLoginException(String message) {
		super(message);
	}
}
