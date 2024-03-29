package os.server;

import os.server.player.Player;
import os.server.users.Club;

/**
 * Thrown to indicate that a Club doesn't have enough funds to complete the purchase of a Player.
 */
public class InsufficientFundsException extends Exception {
	private static final long serialVersionUID = 7005406086109096584L;

	public InsufficientFundsException() { }
	
	public InsufficientFundsException(String message) {
		super(message);
	}
	
	public InsufficientFundsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InsufficientFundsException(Throwable cause) {
		super(cause);
	}
	
	public InsufficientFundsException(Club c, Player p) {
		super(String.format("%s does not have enough funds to complete the transaction.\nNeed: %.2f\nHave: %.2f", 
				c.getName(), p.getValuation(), c.getFunds()));
	}
}
