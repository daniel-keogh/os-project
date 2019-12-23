package os.server;

import os.server.players.Player;
import os.server.users.Club;

public class InsufficientFundsException extends Exception {
	public InsufficientFundsException() { }
	
	public InsufficientFundsException(Club c, Player p) {
		super(String.format("%s does not have enough funds to complete the transaction.\nNeed: %.2f\nHave: %.2f", 
				c.getName(), p.getValuation(), c.getFunds()));
	}
}
