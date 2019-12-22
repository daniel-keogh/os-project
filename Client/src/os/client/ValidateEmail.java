package os.client;

public class ValidateEmail {
	// Source: https://www.tutorialspoint.com/validate-email-address-in-java
	public static boolean isValid(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}
}
