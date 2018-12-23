package main.net.atos.uk.TravelDashboard.ClaimItem;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is used to hold each of the claim (one row data). The only difference is this class
 * only contains one date, compared with the Receipt class. It is used when the user choose one 
 * date type (claim date or trip end date) to get summary report.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class ReceiptForAnalysis {
	
	private SimpleStringProperty wbsNumber;
	private SimpleDoubleProperty amountInPound;
	private SimpleStringProperty costElement;
	private SimpleStringProperty location;
	private SimpleStringProperty date;
	private SimpleIntegerProperty employeeID;
	protected int weekNumber;
	
	public ReceiptForAnalysis(String wbsNumber, double amountInPound, String costElement, String location, 
			String date, int employeeID, int weekNumber) {
		this.wbsNumber = new SimpleStringProperty(wbsNumber);
		this.amountInPound = new SimpleDoubleProperty(amountInPound);
		this.costElement = new SimpleStringProperty(costElement);
		this.location = new SimpleStringProperty(location);
		this.date = new SimpleStringProperty(date);
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
	public String getDate() {
		return date.get();
	}
	public int getEmployeeID() {
		return employeeID.get();
	}
	
    public int getWeekNumber() {
    	return this.weekNumber;
    }
}
