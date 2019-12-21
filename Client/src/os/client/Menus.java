package os.client;

public class Menus {
	public static void showAgentMenu() {
		System.out.println("|==================================================|");
		System.out.println("|                    Agent Menu                    |");
		System.out.println("|==================================================|");
		System.out.println("| A. Add Player                                    |");
		System.out.println("| B. Update Player Valuation                       |");
		System.out.println("| C. Update Player Status                          |");
		System.out.println("| Q. Logout                                        |");
		System.out.println("|==================================================|");
	}
	
	public static void showClubMenu() {
		System.out.println("|==================================================|");
		System.out.println("|                    Club  Menu                    |");
		System.out.println("|==================================================|");
		System.out.println("| A. Search Players by Position                    |");
		System.out.println("| B. Search for For Sale Player in Club            |");
		System.out.println("| C. Suspend/Resume Sale                           |");
		System.out.println("| D. Purchase Player                               |");
		System.out.println("| Q. Logout                                        |");
		System.out.println("|==================================================|");
	}
}
