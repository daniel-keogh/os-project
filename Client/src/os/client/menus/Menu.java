package os.client.menus;

public abstract class Menu {
	public abstract void show();

	public String[] formatList(String list) {
		return list.replace("[", "").replace("]", "").split(", ");
	}
}
