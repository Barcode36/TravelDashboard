package main.net.atos.uk.TravelDashboard.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * This class is the abstract class of database connection. All the classes in the database
 * packages inherited from this class. All the methods trying to connect to the database will
 * use the connectDB() and closeDB() methods.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public abstract class DatabaseConnector {
	protected Connection connection = null;
	protected PreparedStatement pstmt = null;
	protected Statement stmt = null;
	protected String sql = null;
	
	/**
     * Connect to the database. String dburl uses the format: jdbc:mysql://IP:Port/DatabaseName
     * The userName and passWord is for database side connection (Server side setting), and has 
     * nothing related with the register user from this software.
     * 
     */
	protected void connectDB() {
		String dburl = "jdbc:mysql://160.153.128.36:3306/Dashboard_Atos";

		String userName = "niallll";
		String passWord = "asdfghjkl";

		try {
			//register JDBC driver
			Class.forName ("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection (dburl, userName, passWord);
			//System.out.println ("Database connection established");
		}
		catch (Exception e) {
			System.out.println(e);
		} 
	}
	
	/**
     * Close the database connection.
     * 
     */
	protected void closeDB() {
		if (connection != null) {
			try {
				connection.close();
				//System.out.println("Database connection terminated");
			} 
			catch (Exception e) { 
				System.out.println(e);
			}
		}
	}
}
