package os.client.menus;

public abstract class Menu {
	public abstract void show();

	public String[] formatList(String list) {
		return list.replace("[", "").replace("]", "").split(", ");
	}
	
	public String getPlayerTableHeading() {
		return String.format("%-10s  %-10s  %-10s  %-25s  %3s  %12s  %-10s  %s\n", 
				"PID", "Club ID", "Agent ID", "Name", "Age", "Valuation", "Position", "Status");
	}
}
