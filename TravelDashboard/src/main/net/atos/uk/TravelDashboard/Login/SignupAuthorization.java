package main.net.atos.uk.TravelDashboard.Login;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import main.net.atos.uk.TravelDashboard.Database.LoginConnector;

/**
 * This class is the authorization class of sign up process. All of the rules
 * are in 'userInputVaild' method, and new rule can be easily added.
 * The password uses SHA-256 to encode, therefore the input password is translated first.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class SignupAuthorization {
	LoginConnector loginConnector = new LoginConnector();

	/**
     * To check if input is valid for sign up a new account. The rules are:
     * default register code is "Atos17",
     * username is available, that is not found in the database,
     * username only contains A-Z, a-z, 0-9,
     * two times input password matched,
     * password must be longer than 6,
     * password only contains A-Z, a-z, 0-9,
     * email address contains an '@'.
     * 
     * @param inputUsername The user input username
     * @param inputPassword The user's first input password
     * @param inputPasswordAgain The user's second input password
     * @param emailAddress The user input email address
     * @param registerCode The user input register code
     * 
     * @return Authorization class that contains a boolean value and the information in details 
     */
	public Authorization userInputValid(String inputUsername, String inputPassword, String inputPasswordAgain, 
			String emailAddress, String registerCode) {
		Authorization az = new Authorization();
		
		if (!registerCode.equals("Atos17")) {
			az.setValid(false);
			az.setInformation("Register code incorrect!");
			return az;
		}
		
		else if (!loginConnector.isUsernameAvailable(inputUsername)) {
			az.setValid(false);
			az.setInformation("Username not available!");
			return az;
		}
		
		else if (!inputUsername.matches("[A-Za-z0-9]*")) {
			az.setValid(false);
			az.setInformation("Username contains invalid character!");
			return az;
		}
		
		else if (!inputPassword.equals(inputPasswordAgain)) {
			az.setValid(false);
			az.setInformation("Password not matched!");
			return az;
		}
		
		else if (inputPassword.length() < 6) {
			az.setValid(false);
			az.setInformation("Password too short!");
			return az;
		}
		
		else if (!inputPassword.matches("[A-Za-z0-9]*")) {
			az.setValid(false);
			az.setInformation("Password contains invalid character!");
			return az;
		}
		
		else if (!emailAddress.contains("@")) {
			az.setValid(false);
			az.setInformation("Email address invalid!");
			return az;
		}
		
		else {
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("SHA-256");
				md.update(inputPassword.getBytes("UTF-8"));
				byte[] encodePassword = md.digest();
				
				loginConnector.toSignUp(inputUsername, encodePassword, emailAddress);
				System.out.println("OK!");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		
		az.setValid(true);
		az.setInformation("Account created successfully!");
		return az;
	}
}
