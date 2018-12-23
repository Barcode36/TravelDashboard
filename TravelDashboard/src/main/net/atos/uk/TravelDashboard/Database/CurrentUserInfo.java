package main.net.atos.uk.TravelDashboard.Database;

/**
 * This class contains the information of current user. The information is used in:
 * main Dashoboard to display the user information,
 * database package methods to read or write from or to the database.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class CurrentUserInfo {
	private static String username = null;
	private static int userID = 0;
	private static String userEmail = null;

	public static int getUserID() {
		return userID;
	}

	public static void setUserID(int userID) {
		CurrentUserInfo.userID = userID;
	}

	public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
    	CurrentUserInfo.username = username;
    }

	public static String getUserEmail() {
		return userEmail;
	}

	public static void setUserEmail(String userEmail) {
		CurrentUserInfo.userEmail = userEmail;
	}

}
