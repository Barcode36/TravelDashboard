package main.net.atos.uk.TravelDashboard.ClaimItem;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is used to hold each of the claim (one row data) inside the software, essentially:
 * wbsNumber, amount(pound), cost element(type), location, expense data, trip end data, week number
 * for expense data, and employee ID.
 * 
 * It is used in:
 * FileReader to read Excel file content then upload to the database,
 * the whole data analysis process (extract data from the database, then analyse the data in report page).
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class Receipt {
	private SimpleStringProperty wbsNumber;
	private SimpleDoubleProperty amountInPound;
	private SimpleStringProperty costElement;
	private SimpleStringProperty location;
	private SimpleStringProperty expenseDate;
	private SimpleStringProperty tripEndDate;
	private SimpleIntegerProperty employeeID;
	protected int weekNumber;
	
	public Receipt(String wbsNumber, double amountInPound, String costElement, String location, 
			String expenseDate, String tripEndDate, int employeeID, int weekNumber) {
		this.wbsNumber = new SimpleStringProperty(wbsNumber);
		this.amountInPound = new SimpleDoubleProperty(amountInPound);
		this.costElement = new SimpleStringProperty(costElement);
		this.location = new SimpleStringProperty(location);
		this.expenseDate = new SimpleStringProperty(expenseDate);
		this.tripEndDate = new SimpleStringProperty(tripEndDate);
		this.employeeID = new SimpleIntegerProperty(employeeID);
		this.weekNumber = weekNumber;
	}

	public String getWbsNumber() {
		return wbsNumber.get();
	}
	public double getAmountInPound() {
		return amountInPound.get();
	}
	public String getCostElement() {
		return costElement.get();
	}
	public String getLocation() {
		return location.get();
	}
	public String getExpenseDate() {
		return expenseDate.get();
	}
	public String getTripEndDate() {
		return tripEndDate.get();
	}
	public int getEmployeeID() {
		return employeeID.get();
	}
    public int getWeekNumber() {
    	return this.weekNumber;
    }
}