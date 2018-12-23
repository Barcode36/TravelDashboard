package main.net.atos.uk.TravelDashboard.Dashboard.Report;

import java.util.ArrayList;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import main.net.atos.uk.TravelDashboard.ClaimItem.ReceiptForAnalysis;


/**
 * This class is used to find all date options of several ComboBoxs in:
 * main Dashboard and report page. According to the options list, the user
 * can see all the possibility he can choose, in:
 * main Dashboard to choose budget ComboBox,
 * report page to choose the WBS number,
 * report page to choose employee ID when "Person" is selected.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class DateSelector {
	
	/**
     * Contains all given data, analysis based on it.
     */
	private ArrayList<ReceiptForAnalysis> userClaims;
	/**
     * The comparator to make sure the list is in a correct (sequential) order: 
     */
	Comparator<String> myComparator;
	/**
     * The list is used to find all year options (2015, 2016...) in:
     * main Dashboard for the Year button to find the last year data (the first element of the list),
     * report page, Year option.
     */
	private ObservableList<String> comboBoxOptions1 = FXCollections.observableArrayList();
	/**
     * Used to make sure the list is in a correct (sequential) order: 
     */
	private SortedList<String> comboBoxOptionsList1;
	/**
     * The list is used to find all quarter/month/week options (2014Q3, 2016M10, 2016W40...) in report page.
     */
	private ObservableList<String> comboBoxOptions2 = FXCollections.observableArrayList();
	/**
     * Used to make sure the list is in a correct (sequential) order: 
     */
	private SortedList<String> comboBoxOptionsList2;
	
	/**
     * Used to find quarter index by month. 
     */
	private final int[] QUARTERS = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
	
	/**
     * The constructor receives the data and initialises the comparator.
     *  
     * @param userClaims contains all given data, analysis based on it
     */
	public DateSelector(ArrayList<ReceiptForAnalysis> userClaims) {
		this.userClaims = userClaims;
		myComparator = new Comparator<String>() {
	        @Override
	        public int compare(String s2, String s1) {
	            if (s1.length() == 7) {
	            	return (s1.substring(3) + s1.substring(0, 3)).compareTo(s2.substring(3) + s2.substring(0, 3));
	            }
	            else {
	            	return s1.compareTo(s2);
	            }
	        }
	    };
	}
	
	/**
     * The list is used to find all year options (2015, 2016...) within all given data.
     */
	public ObservableList<String> findComboBoxOptionsByYear() {
		comboBoxOptions1.clear();
		
		for (ReceiptForAnalysis r : userClaims) {
			if (!r.getDate().equals("")) {
				if (!comboBoxOptions1.contains(r.getDate().substring(6))) {
					comboBoxOptions1.add(r.getDate().substring(6));
				}
			}
		}
	    
		comboBoxOptionsList1 = comboBoxOptions1.sorted();
	    comboBoxOptionsList1.setComparator(myComparator);
		return comboBoxOptionsList1;
	}
	
	/**
     * The list is used to find all month options (2015M3, 2016M11...) within a given year.
     * 
     * @param year the given year
     * @return ObservableList of given year by month
     */
	public ObservableList<String> findComboBoxOptionsByMonthWithinYear(String year) {
		comboBoxOptions2.clear();
		
		for (ReceiptForAnalysis r : userClaims) {
			if (!r.getDate().equals("") && r.getDate().substring(6).equals(year)) {
				if (!comboBoxOptions2.contains(year + " M" + r.getDate().substring(3, 5))) {
					comboBoxOptions2.add(year + " M" + r.getDate().substring(3, 5));
				}
			}
		}
		
		comboBoxOptionsList2 = comboBoxOptions2.sorted();
	    comboBoxOptionsList2.setComparator(myComparator);
		return comboBoxOptionsList2;
	}
	
	/**
     * The list is used to find all quarter options (2015Q3, 2016Q4...) within a given year.
     * 
     * @param year the given year
     * @return ObservableList of given year by quarter
     */
	public ObservableList<String> findComboBoxOptionsByQuarterWithinYear(String year) {
		comboBoxOptions2.clear();
		
		for (ReceiptForAnalysis r : userClaims) {
			if (!r.getDate().equals("") && r.getDate().substring(6).equals(year)) {
				if (!comboBoxOptions2.contains(year + " Q" + QUARTERS[Integer.valueOf(r.getDate().substring(3, 5))-1])) {
					comboBoxOptions2.add(year + " Q" + QUARTERS[Integer.valueOf(r.getDate().substring(3, 5))-1]);
				}
			}
		}
		
		comboBoxOptionsList2 = comboBoxOptions2.sorted();
	    comboBoxOptionsList2.setComparator(myComparator);
		return comboBoxOptionsList2;
	}
	
	/**
     * The list is used to find all week options (2015W3, 2016W24...) within a given year.
     * Notice that the week information is from the Excel File, it is not analysed by the system.
     * so based on that, only ExpenseDate will have week number. If the user choose TripEndDate, the
     * week number is simply set to 0.
     * 
     * @param year the given year
     * @return ObservableList of given year by week
     */
	public ObservableList<String> findComboBoxOptionsByWeekWithinYear(String year) {
		comboBoxOptions2.clear();
		
		for (ReceiptForAnalysis r : userClaims) {
			if (!r.getDate().equals("") && r.getDate().substring(6).equals(year)) {
				if (r.getWeekNumber() != 0 && !comboBoxOptions2.contains(year + " W" + r.getWeekNumber())) {
					comboBoxOptions2.add(year + " W" + r.getWeekNumber());
				}
			}
		}
		
		comboBoxOptionsList2 = comboBoxOptions2.sorted();
	    comboBoxOptionsList2.setComparator(myComparator);
		return comboBoxOptionsList2;
	}
}
