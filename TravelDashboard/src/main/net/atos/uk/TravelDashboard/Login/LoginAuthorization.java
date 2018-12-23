package main.net.atos.uk.TravelDashboard.Login;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import main.net.atos.uk.TravelDashboard.Database.LoginConnector;

/**
 * This class is the authorization class of login process. Essentially it checks if
 * input username and password are matched in the database. The password uses SHA-256
 * to encode, therefore the input password is translated first.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class LoginAuthorization {
	LoginConnector loginConnector = new LoginConnector();
	
	/**
     * To check if input username and password are matched in the database.
     * 
     * @param inputUsername The user input username
     * @param inputPassword The user input password
     * 
     * @return if input is correct or not
     */
	public boolean userInfoCorrect(String inputUsername, String inputPassword) {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(inputPassword.getBytes("UTF-8"));
			byte[] inputEncodePassword = md.digest();
			
			if (loginConnector.isLoginValid(inputUsername, inputEncodePassword)) {	
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
