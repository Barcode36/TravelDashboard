package main.net.atos.uk.TravelDashboard.Database;

import java.sql.ResultSet;
import java.util.Arrays;

/**
 * This class contains all the methods that are used in the login and sign up process.
 * 
 * @author  Niall Garratt
 * @since   2017-04-8
 * @version 1.0
*/

public class LoginConnector extends DatabaseConnector {

	/**
     * The method is used to upload the sign up information to the database. The information here
     * must be valid because the SignupAuthorization has already done the authorisation.
     * 
     * @param name valid username
     * @param encodePassword valid password in SHA-256 format
     * @param emailAddress valid email address
     * 
     * @return sign up successful or not
     */
	public boolean toSignUp(String name, byte[] encodePassword, String emailAddress) {
		boolean ifSuccess = true; 
		connectDB();
		
		try {
			sql = "INSERT INTO User(userUsername, userPassword, userEmail) VALUES (?,?,?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setBytes(2, encodePassword);
			pstmt.setString(3, emailAddress);
			pstmt.execute();
			pstmt.close();
		}
	    catch (Exception e) {
	    	ifSuccess = false;
			System.out.println(e);
		} finally {
			closeDB();
		}
		return ifSuccess; 
	}
	
	/**
     * The method is used to check if input username is available (not used yet).
     * 
     * @param input input username
     * 
     * @return username available or not
     */
	public boolean isUsernameAvailable(String input) {
		boolean isAvailable = true;
		connectDB();
		//execute a query
		try {

		    stmt = connection.createStatement();
		    sql = "SELECT * FROM  `User`";

		    ResultSet rs = stmt.executeQuery(sql);
			
		    //Extract data from result set
		    while(rs.next()) {
		       //Retrieve by column name
		       String username = rs.getString("userUsername");
		       if (username.equals(input)) {
		    	   isAvailable = false;
		    	   break;
		       }
		    }
		    stmt.close();
		}
	    catch (Exception e) {
			System.out.println(e);
		} finally {
			closeDB();	
		}
		
		return isAvailable;
	}
	
	/**
     * The method is used to check if login process is valid. If it is valid, the CurrentUserInfo
     * is set inside this method. Then a true is returned, so that the user is allowed to see the 
     * main Dashboard.
     * 
     * @param inputUsername input username
     * @param inputEncodePassword input password in SHA-256 format
     * 
     * @return login successful or not
     */
	public boolean isLoginValid(String inputUsername, byte[] inputEncodePassword) {
		boolean isValid = false;
		connectDB();
		//execute a query
		try {

		    stmt = connection.createStatement();
		    sql = "SELECT * FROM  `User`";

		    ResultSet rs = stmt.executeQuery(sql);
			
		    //Extract data from result set
		    while(rs.next()) {
		       //Retrieve by column name
		       String username = rs.getString("userUsername");
		       byte[] encodePassword = rs.getBytes("userPassword");
		       String email = rs.getString("userEmail");
		       if ((username.equals(inputUsername) || email.equals(inputUsername))
		    		   && Arrays.equals(encodePassword, inputEncodePassword)) {
		    	   isValid = true;
		    	   
		    	   int userID = rs.getInt("userID");
		    	   CurrentUserInfo.setUsername(username);
		    	   CurrentUserInfo.setUserID(userID);
		    	   CurrentUserInfo.setUserEmail(email);
		    	   
		    	   break;
		       }
		    }
		    stmt.close();
		}
	    catch (Exception e) {
			System.out.println(e);
		} finally {
			closeDB();	
		}
		
		return isValid;
	}
}
