package main.net.atos.uk.TravelDashboard.Dashboard.Report;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class defines a type to have a place name and the total amount. It is used when
 * some sum amount need to be calculated. For example each column of charts in the system.
 * 
 * It is used in:
 * main Dashboard to generate charts and labels,
 * report page to generate charts and labels.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class Recorder {
	private SimpleStringProperty place;
	private SimpleDoubleProperty total;
	
	public Recorder(String place, double total) {
		this.place = new SimpleStringProperty(place);
		this.total = new SimpleDoubleProperty(total);
	}
	
	public String getPlace() {
		return place.get();
	}
	public double getTotal() {
		return total.get();
	}
	public void setPlace(String place) {
		this.place.set(place);
	}
	public void setTotal(double total) {
		this.total.set(total);
	}
}
