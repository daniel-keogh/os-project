package os.client.menus;

public class InvalidOptionException extends Exception {
	public InvalidOptionException() {
		super("Please enter one of the options provided.");
	}
}
