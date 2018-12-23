package main.net.atos.uk.TravelDashboard.Login;

/**
 * This class is the authorization class to give information. It is used in SignupAuthorization.
 * For example if the password is too short, it returns Authorization(false, "Password too short!"),
 * and if the sign up process is valid, it returns Authorization(true, "Account created successfully!").
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class Authorization {
	private boolean valid;
	private String information = "";
	
	public boolean isValid() {
		return valid;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	
}
