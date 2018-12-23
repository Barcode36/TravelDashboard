package main.net.atos.uk.TravelDashboard.Dashboard.Report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import main.net.atos.uk.TravelDashboard.ClaimItem.ReceiptForAnalysis;

/**
 * This class is used to generate analysis data and charts for the report page:
 * the labels and charts of five part: general information, time, location, cost element and member.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class ReportDataCalculator {
	
	/**
     * Contains all the uploaded data belongs to the current user.
     */
	private ArrayList<ReceiptForAnalysis> userClaims;
	/**
     * Contains the time filter, "" for all and "xxxx" (for example "2016") for the last year
     */
	private String timeFilter;
	
	/**
     * Used to find quarter index by month. 
     */
	private final int[] QUARTERS = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
	
	/**
	 * Used for time table 
	 */
	private ObservableList<Recorder> timeListForTableUse = FXCollections.observableArrayList();
	/**
	 * Used for location table 
	 */
	private ObservableList<Recorder> locationListForTableUse = FXCollections.observableArrayList();
	/**
	 * Used for cost element table 
	 */
	private ObservableList<Recorder> costElementListForTableUse = FXCollections.observableArrayList();
	/**
	 * Used for member table 
	 */
	private ObservableList<Recorder> teamMemberListForTableUse = FXCollections.observableArrayList();
	
	/**
     * Map used to calculate the total cost of total/last year
     */
	private TreeMap<String, Double> timeMap;
	/**
     * Map used to calculate the cost in location of total/last year
     */
	private HashMap<String, Double> locationMap;
	/**
     * Map used to calculate the cost in cost element of total/last year
     */
	private HashMap<String, Double> costElementMap;
	/**
     * Map used to calculate the cost in employee of total/last year
     */
	private HashMap<String, Double> teamMemberMap;
	
	private double totalInTime;
	private double aveInTime;
	private double maxInTime;
	private double minInTime;

	private double totalInLocation;
	private String amountInLocation1;
	private String amountInLocation2;
	private String amountInLocation3;
	private String placeInLocation1;
	private String placeInLocation2;
	private String placeInLocation3;
	private double totalInCostElement;
	private String amountInCostElement1;
	private String amountInCostElement2;
	private String amountInCostElement3;
	private String placeInCostElement1;
	private String placeInCostElement2;
	private String placeInCostElement3;
	private double totalInTeamMember;
	private String amountInTeamMember1;
	private String amountInTeamMember2;
	private String amountInTeamMember3;
	private String nameInTeamMember1;
	private String nameInTeamMember2;
	private String nameInTeamMember3;
	
    private CategoryAxis xAxisTime;
    private NumberAxis yAxisTime;
    private LineChart<String,Number> timeLineChart;
	private CategoryAxis xAxisLoc;
	private NumberAxis yAxisLoc;
    private BarChart<String,Number> locationBarChart;
	private CategoryAxis xAxisEle;
    private NumberAxis yAxisEle;
	private BarChart<String,Number> costElementBarChart;
	private CategoryAxis xAxisMem;
    private NumberAxis yAxisMem;
	private BarChart<String,Number> teamMemberBarChart;
	
	DecimalFormat df = new DecimalFormat("#.##");
	
	
	/**
     * The constructor will do all the calculation:
     * several maps used to calculate value of amounts,
     * line charts and bar charts.
     * 
     * @param userClaims all the data that belongs to the current user
     * @param filter time filter
     */
	public ReportDataCalculator(ArrayList<ReceiptForAnalysis> userClaims, String timeFilter) {
		this.timeFilter = timeFilter;
		this.userClaims = dealWithTimeFilter(userClaims);
		
		timeMap = new TreeMap<String, Double>();
		locationMap = new HashMap<String, Double>();
		costElementMap = new HashMap<String, Double>();
		teamMemberMap = new HashMap<String, Double>();
		
		xAxisTime = new CategoryAxis();
	    yAxisTime = new NumberAxis();
	    timeLineChart = new LineChart<String,Number>(xAxisTime, yAxisTime);
		xAxisLoc = new CategoryAxis();
		yAxisLoc = new NumberAxis();
	    locationBarChart = new BarChart<>(xAxisLoc, yAxisLoc);
		xAxisEle = new CategoryAxis();
	    yAxisEle = new NumberAxis();
		costElementBarChart = new BarChart<>(xAxisEle, yAxisEle);
		xAxisMem = new CategoryAxis();
		yAxisMem = new NumberAxis();
		teamMemberBarChart = new BarChart<>(xAxisMem, yAxisMem);
		
		setTotalValues();
		
		setListForTableUse(timeMap, timeListForTableUse);
		setListForTableUse(locationMap, locationListForTableUse);
		setListForTableUse(costElementMap, costElementListForTableUse);
		setListForTableUse(teamMemberMap, teamMemberListForTableUse);
		
		setLineChartAndLabels(timeLineChart, "Time Summary", timeMap);
		
		setBarChartAndLabels(locationBarChart, "Location Summary", getMostEightValues(locationMap));
		setBarChartAndLabels(costElementBarChart, "Cost Element Summary", getMostEightValues(costElementMap));
		setBarChartAndLabels(teamMemberBarChart, "Team Member Summary", getMostEightValues(teamMemberMap));
	}
	
	/**
     * Used to find the correct part of data, according to the time filter.
     * 
     * @param userClaims contains all the uploaded data belongs to the current user
     * @return ArrayList of selected time interval
     */
	private ArrayList<ReceiptForAnalysis> dealWithTimeFilter(ArrayList<ReceiptForAnalysis> userClaims) {
		if (timeFilter.equals("")) return userClaims;
		
		else if (timeFilter.length() == 4) {
			Iterator<ReceiptForAnalysis> iter = userClaims.iterator();

			while (iter.hasNext()) {
				ReceiptForAnalysis r = iter.next();

			    if (r.getDate().equals("") ||
			    		!r.getDate().substring(6).equals(timeFilter)) {
			    	iter.remove();
				}
			}
			return userClaims;
		}
		
		else {
			if (timeFilter.substring(5, 6).equals("Q")) {
				Iterator<ReceiptForAnalysis> iter = userClaims.iterator();

				while (iter.hasNext()) {
					ReceiptForAnalysis r = iter.next();
				    
				    if (r.getDate().equals("") || !r.getDate().substring(6).equals(timeFilter.substring(0, 4)) ||
							QUARTERS[Integer.valueOf(r.getDate().substring(3, 5))-1] != Integer.valueOf(timeFilter.substring(6))) {
				    	iter.remove();
					}
				}
			}
			else if (timeFilter.substring(5, 6).equals("M")) {
				Iterator<ReceiptForAnalysis> iter = userClaims.iterator();

				while (iter.hasNext()) {
					ReceiptForAnalysis r = iter.next();

					if (r.getDate().equals("") || !r.getDate().substring(6).equals(timeFilter.substring(0, 4)) ||
							!r.getDate().substring(3, 5).equals(timeFilter.substring(6))) {
						iter.remove();
					}
				}
					
			}
			else if (timeFilter.substring(5, 6).equals("W")) {
				Iterator<ReceiptForAnalysis> iter = userClaims.iterator();

				while (iter.hasNext()) {
					ReceiptForAnalysis r = iter.next();

				    if (r.getDate().equals("") || !r.getDate().substring(6).equals(timeFilter.substring(0, 4)) ||
							r.getWeekNumber() != Integer.valueOf(timeFilter.substring(6))) {
				    	iter.remove();
					}
				}
					
			}
		}
		
		return userClaims;
	}
	
	/**
     * Used to set the maps. Redundant data is removed here.
     */
	private void setTotalValues() {
		for (ReceiptForAnalysis r : userClaims) {
			
			// calculate total values for selected time interval
			if (!r.getLocation().equals("")) {
				totalInLocation += r.getAmountInPound();
				addToMap(locationMap, r.getLocation().toLowerCase(), r.getAmountInPound());
			}
			if (!r.getCostElement().equals("")) {
				totalInCostElement += r.getAmountInPound();
				addToMap(costElementMap, r.getCostElement().toLowerCase(), r.getAmountInPound());
			}
			if (r.getEmployeeID() != 0) {
				totalInTeamMember += r.getAmountInPound();
				addToMap(teamMemberMap, String.valueOf(r.getEmployeeID()), r.getAmountInPound());
			}
			if (!r.getDate().equals("")) {
				if (timeFilter.length() == 0) {
					addToMap(timeMap, r.getDate().substring(6), r.getAmountInPound());
				}
				else if (timeFilter.length() == 4 || timeFilter.substring(5, 6).equals("Q")) {
					addToMap(timeMap, r.getDate().substring(3), r.getAmountInPound());	
				}
				else {
					addToMap(timeMap, r.getDate(), r.getAmountInPound());
				}
			}
		}
	}
	
	/**
     * Used to add data into map.
     * 
     * @param hm processing map
     * @param str processing item name
     * @param amount processing amount
     */
	private void addToMap(Map<String, Double> hm, String str, double amount) {
		Double total = hm.get(str.substring(0, 1).toUpperCase() + str.substring(1));
		if (total != null) total += amount;
		else total = amount;
		
		hm.put(str.substring(0, 1).toUpperCase() + str.substring(1), total);
	}
	
	
	private void setListForTableUse(Map<String, Double> hm, ObservableList<Recorder> list) {
		for (Map.Entry<String, Double> entry : hm.entrySet()) {
			Recorder r = new Recorder(entry.getKey(), Double.valueOf(df.format(entry.getValue())));
			list.add(r);
		}
	}
	
	/**
     * Used to get the most eight cost item of element/location/employee, for chart use.
     * 
     * @param hm processing map
     * @return recorder of eight items that cost most
     */
	private Recorder[] getMostEightValues(HashMap<String, Double> hm) {
		Recorder[] valuesRecorder = new Recorder[8];
		for (int i = 0; i < 8; i++) {
			valuesRecorder[i] = new Recorder("", 0);	
		}	
		
		for (Map.Entry<String, Double> entry : hm.entrySet()) {
		    String key  = entry.getKey(); 
			double value = entry.getValue();
			
			int tempIndex = 0;
			double tempValue = valuesRecorder[0].getTotal();
			
			for (int i = 1; i < 8; i++) {
				if (valuesRecorder[i].getTotal() < tempValue) {
					tempValue = valuesRecorder[i].getTotal();
					tempIndex = i;
				}
			}
			if (value > tempValue) {
				valuesRecorder[tempIndex].setPlace(key);
				valuesRecorder[tempIndex].setTotal(value);
			}
		}
		
		return valuesRecorder;
	}
	
	/**
     * Used to set line chart and the corresponding labels.
     * 
     * @param lc the line chart
     * @param chart title
     * @param the map that is used
     */
	private void setLineChartAndLabels(LineChart<String,Number> lc, String title, Map<String, Double> hm) {
		lc.setTitle(title);
        lc.setPrefHeight(180);
        lc.setPrefWidth(980);
        
        Double maxTemp = null;
        Double minTemp = null;
        
        XYChart.Series<String, Number> series = new Series<String, Number>();
        
        for (Map.Entry<String, Double> entry : hm.entrySet()) {
            series.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
            totalInTime += entry.getValue();
            if (maxTemp == null) {maxTemp = entry.getValue();}
            else {if (entry.getValue() > maxTemp) maxTemp = entry.getValue();}

            if (minTemp == null) {minTemp = entry.getValue();}
            else {if (entry.getValue() < minTemp) minTemp = entry.getValue();}
        }
        if (!hm.isEmpty()) {
        	aveInTime = totalInTime / hm.size();	
        }
        
        if (maxTemp != null && minTemp != null) {
            maxInTime = maxTemp;
            minInTime = minTemp;
        }

        lc.getData().add(series);
	}
	
	/**
     * Used to set bar chart and the corresponding labels.
     * 
     * @param bc the bar chart
     * @param title chart title
     * @param rc the array of Recorder that records the most cost items name and amount
     */
	private void setBarChartAndLabels(BarChart<String,Number> bc, String title, Recorder[] rc) {
        bc.setTitle(title);
        bc.setPrefHeight(180);
        bc.setPrefWidth(980);
        
        double colNum = 0;
        
        for (int i = 0; i < 8; i++) {
        	if (rc[i].getTotal() != 0) {
            	XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();	
            	series.getData().add(new XYChart.Data<String,Number>(pruneString(rc[i].getPlace(), 14), rc[i].getTotal()));
            	bc.getData().add(series);
            	colNum += 1;
        	}
        }
        
        bc.setBarGap(((8-colNum)/8*80 + 20) * -1);
        bc.setCategoryGap((8-colNum)/8*80 + 80);

        // To find most three values for labels setting
        Recorder[] rs3 = new Recorder[3];
		for (int i = 0; i < 3; i++) {
			rs3[i] = new Recorder("", 0);
		}

		for (int i = 0; i < 8; i++) {
			String key  = rc[i].getPlace(); 
			double value = rc[i].getTotal();
			
			int tempIndex = 0;
			double tempValue = rs3[0].getTotal();
			
			for (int j = 1; j < 3; j++) {
				if (rs3[j].getTotal() < tempValue) {
					tempValue = rs3[j].getTotal();
					tempIndex = j;
				}
			}
			if (value > tempValue) {
				rs3[tempIndex].setPlace(key);
				rs3[tempIndex].setTotal(value);
			}
		}
		if (bc == locationBarChart) {
			amountInLocation1 = String.valueOf(Double.valueOf(df.format(rs3[0].getTotal())));
			amountInLocation2 = String.valueOf(Double.valueOf(df.format(rs3[1].getTotal())));
			amountInLocation3 = String.valueOf(Double.valueOf(df.format(rs3[2].getTotal())));
			placeInLocation1 = pruneString(rs3[0].getPlace(), 10);
			placeInLocation2 = pruneString(rs3[1].getPlace(), 10);
			placeInLocation3 = pruneString(rs3[2].getPlace(), 10);
		}
		else if (bc == costElementBarChart){
			amountInCostElement1 = String.valueOf(Double.valueOf(df.format(rs3[0].getTotal())));
			amountInCostElement2 = String.valueOf(Double.valueOf(df.format(rs3[1].getTotal())));
			amountInCostElement3 = String.valueOf(Double.valueOf(df.format(rs3[2].getTotal())));
			placeInCostElement1 = pruneString(rs3[0].getPlace(), 10);
			placeInCostElement2 = pruneString(rs3[1].getPlace(), 10);
			placeInCostElement3 = pruneString(rs3[2].getPlace(), 10);
		}
		else {
			amountInTeamMember1 = String.valueOf(Double.valueOf(df.format(rs3[0].getTotal())));
			amountInTeamMember2 = String.valueOf(Double.valueOf(df.format(rs3[1].getTotal())));
			amountInTeamMember3 = String.valueOf(Double.valueOf(df.format(rs3[2].getTotal())));
			nameInTeamMember1 = pruneString(rs3[0].getPlace(), 10);
			nameInTeamMember2 = pruneString(rs3[1].getPlace(), 10);
			nameInTeamMember3 = pruneString(rs3[2].getPlace(), 10);
		}
	}
	
	/**
     * Used to prune the item name in case the name is too long that the Dashboard cannot display is properly.
     * 
     * @param str the processing string
     * @param num the max lenght of string, for example pruneString("hello", 3) returns "hel..."
     * @return string after prune
     */
	private String pruneString(String str, int num) {
		if (str.length() > num) {
			return str.substring(0, num) + "...";
		}
		else {
			return str;
		}
	}
	
	public double getTotalInTime() {
		return Double.valueOf(df.format(totalInTime));
	}
	
	public double getAveInTime() {
		return Double.valueOf(df.format(aveInTime));
	}
	public double getMaxInTime() {
		return Double.valueOf(df.format(maxInTime));
	}
	public double getMinInTime() {
		return Double.valueOf(df.format(minInTime));
	}

	public double getTotalInLocation() {
		return Double.valueOf(df.format(totalInLocation));
	}
	
	public double getTotalInCostElement() {
		return Double.valueOf(df.format(totalInCostElement));
	}
	
	public double getTotalInTeamMember() {
		return Double.valueOf(df.format(totalInTeamMember));
	}
	
	public ObservableList<Recorder> getTimeListForTableUse() {
		return timeListForTableUse;
	}
	
	public ObservableList<Recorder> getLocationListForTableUse() {
		return locationListForTableUse;
	}
	
	public ObservableList<Recorder> getCostElementListForTableUse() {
		return costElementListForTableUse;
	}
	
	public ObservableList<Recorder> getTeamMemberListForTableUse() {
		return teamMemberListForTableUse;
	}
	
	public LineChart<String, Number> getTimeLineChart() {
		return timeLineChart;
	}
	
	public BarChart<String, Number> getLocationBarChart() {
		return locationBarChart;
	}
	
	public BarChart<String, Number> getCostElementBarChart() {
		return costElementBarChart;
	}
	
	public BarChart<String, Number> getTeamMemberBarChart() {
		return teamMemberBarChart;
	}

	public String getAmountInLocation1() {
		return amountInLocation1;
	}
	public String getAmountInLocation2() {
		return amountInLocation2;
	}
	public String getAmountInLocation3() {
		return amountInLocation3;
	}
	public String getPlaceInLocation1() {
		return placeInLocation1;
	}
	public String getPlaceInLocation2() {
		return placeInLocation2;
	}
	public String getPlaceInLocation3() {
		return placeInLocation3;
	}
	public String getAmountInCostElement1() {
		return amountInCostElement1;
	}
	public String getAmountInCostElement2() {
		return amountInCostElement2;
	}
	public String getAmountInCostElement3() {
		return amountInCostElement3;
	}
	public String getPlaceInCostElement1() {
		return placeInCostElement1;
	}
	public String getPlaceInCostElement2() {
		return placeInCostElement2;
	}
	public String getPlaceInCostElement3() {
		return placeInCostElement3;
	}

	public String getAmountInTeamMember1() {
		return amountInTeamMember1;
	}
	public String getAmountInTeamMember2() {
		return amountInTeamMember2;
	}
	public String getAmountInTeamMember3() {
		return amountInTeamMember3;
	}
	public String getNameInTeamMember1() {
		return nameInTeamMember1;
	}
	public String getNameInTeamMember2() {
		return nameInTeamMember2;
	}
	public String getNameInTeamMember3() {
		return nameInTeamMember3;
	}
}
