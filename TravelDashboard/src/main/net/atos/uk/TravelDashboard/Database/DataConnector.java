package main.net.atos.uk.TravelDashboard.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;
import main.net.atos.uk.TravelDashboard.ClaimItem.ExcelFile;

/**
 * This class contains all the methods to extract and upload data. The methods are used in:
 * main Dashboard to generate general summary of all uploaded data,
 * report page to generate specific report,
 * upload page to upload data to the database.
 * 
 * @author  Niall Garratt
 * @since   2017-04-8
 * @version 1.0
*/

public class DataConnector extends DatabaseConnector {
	
	/**
     * The method is used in main Dashboard for user to get the 6 months budget they set.
     * 
     * @param budgetDate the specified budget date, in the form of "xxxx-Hx" (for example "2017-H1")
     * @return user budget
     */
	public double toGetUserBudget(String budgetDate) {
		double budget = 0;
		connectDB();
		
		try {
		    stmt = connection.createStatement();
		    sql = "SELECT * FROM `Budget` WHERE `budgetDate` = '" + budgetDate 
		    		+ "' and `budgetUserID` = " + CurrentUserInfo.getUserID();

		    ResultSet rs = stmt.executeQuery(sql);

		    //Retrieve by column name
		    if (rs.next()) {
		    	budget = rs.getDouble("budgetAmount");	
		    }
		    stmt.close();
		}
	    catch (Exception e) {
			System.out.println(e);
		} finally {
			closeDB();	
		}
		return budget;
	}
	
	/**
     * The method is used in main Dashboard for user to set up 6 months budget. If the budget
     * is set before, it updated. Otherwise it is set. This is done by MySql 'REPLACE'.
     * 
     * @param budgetDate the specified budget date, in the form of "xxxx-Hx" (for example "2017-H1")
     * @param budgetAmount the total amount of the specified time budget
     * 
     * @return upload successful or not
     */
	public boolean toUploadUserBudget(String budgetDate, double budgetAmount) {
		boolean ifSuccess = true; 
		connectDB();
		
		try {
			sql = "REPLACE INTO Budget(budgetUserID, budgetDate, budgetAmount) VALUES (?,?,?)";
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, CurrentUserInfo.getUserID());
			pstmt.setString(2, budgetDate);
			pstmt.setDouble(3, budgetAmount);
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
     * The method is used when data analysis is needed. It is used in:
     * the main Dashboard to process general analysis,
     * the report page to process analysis in details.
     * 
     * Essentially it returns all the claims that current user have ever uploaded, as an ArrayList.
     * All the filter action (filter by specific employee ID, specific WBS number or specific time amount) 
     * is done based on this ArrayList. The method uses CurrentUserInfo class to obtain current user ID.
     * 
     * @return the ArrayList that contains all the data
     */
	public ArrayList<Receipt> toGetUserClaims() {
		ArrayList<Receipt> al = new ArrayList<>();
		connectDB();
		
		try {
		    stmt = connection.createStatement();
		    sql = "SELECT * FROM `Claim` WHERE claimExcelFileID in "
		    		+ "(SELECT excelFileID FROM `ExcelFile` WHERE `excelUserID` = "
		    		+ CurrentUserInfo.getUserID() + ")";
		    
		    ResultSet rs = stmt.executeQuery(sql);
			
		    //Extract data from result set
		    while(rs.next()) {
		       //Retrieve by column name
		       String wbsIndex = rs.getString("claimWbsNumber");
		       double amount = rs.getDouble("claimAmountInPound");
		       String element = rs.getString("claimCostElement");
		       String location = rs.getString("claimLocation");
		       String startDate = rs.getString("claimExpenseDate");
		       String endDate = rs.getString("claimTripEndDate");
		       int employeeID = rs.getInt("claimEmployeeID");
		       int weekNumber = rs.getInt("claimWeekNumber");
		       Receipt r = new Receipt(wbsIndex, amount, element, 
		    		   location, startDate, endDate, employeeID, weekNumber);
		       al.add(r);
		    }
		    stmt.close();
		}
	    catch (Exception e) {
			System.out.println(e);
		} finally {
			closeDB();	
		}
		return al;
	}
	
	/**
     * The method is used when the user decided to remove claims by Excel file they updated.
     * It is used in upload page.
     * 
     * @param ef the excel file contains its information
     * @return delete successful or not
     */
	public boolean toDeleteExcelInfo(ExcelFile ef) {
		boolean ifSuccess = true; 
		connectDB();
		
		try {
			sql = "DELETE FROM ExcelFile WHERE `excelFileID` = " + ef.getExcelFileId();
			pstmt = connection.prepareStatement(sql);
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
     * The method is used to store the Excel information. It stores the current user ID,
     * along with the Excel file name and the upload time. In the database side, the primary key
     * is increased automatically (by 1).
     * 
     * @param ef the excel file contains its information
     * @return upload successful or not
     */
	public boolean toUploadExcelInfo(ExcelFile ef) {
		boolean ifSuccess = true; 
		connectDB();
		
		try {
			sql = "INSERT INTO ExcelFile(excelUserID, excelFileName, excelUploadTime) VALUES (?,?,?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, CurrentUserInfo.getUserID());
			pstmt.setString(2, ef.getExcelFileName());
			pstmt.setString(3, ef.getExcelFileUploadTime());
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
     * The method is used to get the biggest number of Excel file ID (the ID in database side, as primary
     * key). The process is: (when the user click confirm after they saw the table of upload claims in upload
     * page) toUploadExcelInfo() method insert the Excel file record to the database, the Excel file ID in
     * the database side is handled automatically (increase 1). At the moment, the actual claims (the data)
     * has not yet been uploaded. To do this, the Excel file ID is needed. The ID that has been increased is
     * what we need. It must be the biggest number of Excel file ID belongs to the current user.
     * 
     * @return the latest file id
     */
	public int getLatestFileID() {
		int id = 0;
		connectDB();
		
		try {
		    stmt = connection.createStatement();
		    sql = "SELECT excelFileID FROM `ExcelFile` WHERE excelUserID = " + CurrentUserInfo.getUserID();
		    ResultSet rs = stmt.executeQuery(sql);
			
		    //Extract data from result set
		    while(rs.next()) {
		       //Retrieve by column name
		       int excelFileID = rs.getInt("excelFileID");
		       if (excelFileID > id) id = excelFileID;
		    }
		    stmt.close();
		}
	    catch (Exception e) {
			System.out.println(e);
		} finally {
			closeDB();	
		}
		return id;
	}
	
	/**
     * As explained before, the method is used to upload the actual claims (the data) to the database.
     * 
     * @param fileID the excel file ID (database side, primary key) that the data belongs to
     * @param the to-be-uploaded data
     * 
     * @return upload successful or not
     */
	public boolean toUploadClaims(int fileID, ObservableList<Receipt> claims) {
		boolean ifSuccess = true; 
		connectDB();
		
		String wbsNumber;
		double amountInPound;
		String costElement;
		String location;
		String expenseDate;
		String tripEndDate;
		int employeeID;
		int weekNumber;
		
		int i = 0;
		
		try {
			for (Receipt ap : claims) {
				wbsNumber = ap.getWbsNumber();
				amountInPound = ap.getAmountInPound();
				costElement = ap.getCostElement();
				location = ap.getLocation();
				expenseDate = ap.getExpenseDate();
				tripEndDate = ap.getTripEndDate();
				employeeID = ap.getEmployeeID();
				weekNumber = ap.getWeekNumber();
				
				sql = "INSERT INTO Claim(claimExcelFileID, claimWbsNumber, claimAmountInPound, "
						+ "claimCostElement, claimLocation, claimExpenseDate, claimTripEndDate, "
						+ "claimEmployeeID, claimWeekNumber) VALUES (?,?,?,?,?,?,?,?,?)";
				pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, fileID);
				pstmt.setString(2, wbsNumber);
				pstmt.setDouble(3, amountInPound);
				pstmt.setString(4, costElement);
				pstmt.setString(5, location);
				pstmt.setString(6, expenseDate);
				pstmt.setString(7, tripEndDate);
				pstmt.setInt(8, employeeID);
				pstmt.setInt(9, weekNumber);
				
				pstmt.execute();
				pstmt.close();
				
				i++;
				System.out.println(i + " / " + claims.size());
			}
		}
	    catch (Exception e) {
	    	ifSuccess = false;
			System.out.println(e);
			System.out.println("Error here");
		} finally {
			closeDB();
		}
		return ifSuccess; 
	}
	
	/**
     * The method is used to get the Excel information. It is used in:
     * main Dashboard to display the last four records of upload,
     * upload page to display all of the records of upload.
     * 
     * @return a sorted list of Excel files by upload time
     */
	public SortedList<ExcelFile> toGetExcelFileList() {
		ObservableList<ExcelFile> excelFileList = FXCollections.observableArrayList();
		
		connectDB();
		//execute a query
		try {
		    stmt = connection.createStatement();
		    sql = "SELECT * FROM  `ExcelFile` WHERE excelUserID = " + CurrentUserInfo.getUserID();
		    
		    ResultSet rs = stmt.executeQuery(sql);
			
		    //Extract data from result set
		    while(rs.next()) {
		    	//Retrieve by column name
		    	int excelFileID = rs.getInt("excelFileID");
		    	String excelFileName = rs.getString("excelFileName");
		    	String excelUploadTime = rs.getString("excelUploadTime");
		       
		    	ExcelFile ef = new ExcelFile(excelFileID, excelFileName, excelUploadTime);
		    	excelFileList.add(ef);
		    }
		    stmt.close();
		}
	    catch (Exception e) {
			System.out.println(e);
		} finally {
			closeDB();	
		}
		SortedList<ExcelFile> sortedExcelFileList = excelFileList.sorted();
		sortedExcelFileList.setComparator(new Comparator<ExcelFile>() {
		   @Override
		   public int compare(ExcelFile arg0, ExcelFile arg1) {
		      return arg1.getExcelFileId() - arg0.getExcelFileId();
		   }
		});
		return sortedExcelFileList;
	}	
}
