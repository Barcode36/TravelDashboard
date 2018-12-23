package main.net.atos.uk.TravelDashboard.Dashboard.Main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;
import main.net.atos.uk.TravelDashboard.Dashboard.Report.Recorder;

/**
 * This class is used to generate analysis data and charts for the main Dashboard:
 * the labels showing total cost, who cost the most, in which location cost the most, etc.,
 * the line chart of total/last year,
 * the bar chart of element/location/member.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class GeneralInfoCalculator {
	
	/**
     * Contains all the uploaded data belongs to the current user.
     */
	private ArrayList<Receipt> userClaims;
	/**
     * Contains the time filter, "" for all and "xxxx" (for example "2016") for the last year
     */
	private String filter;
	
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
	
	/**
     * Total cost
     */
	private double totalInGeneral;

	/**
     * The cost most location's amount
     */
	private double mostAmountInLocation;
	/**
     * The cost most location
     */
	private String mostNameInLocation;
	/**
     * The cost most element's amount
     */
	private double mostAmountInCostElement;
	/**
     * The cost most element
     */
	private String mostNameInCostElement;
	/**
     * The cost most member's amount
     */
	private double mostAmountInTeamMember;
	/**
     * The cost most member
     */
	private String mostNameInTeamMember;
	
    private CategoryAxis xAxisTimeBrf;
    private NumberAxis yAxisTimeBrf;
    private LineChart<String,Number> timeLineChartBrf;	
    private CategoryAxis xAxisLocBrf;
	private NumberAxis yAxisLocBrf;
    private BarChart<String,Number> locationBarChartBrf;
	private CategoryAxis xAxisEleBrf;
    private NumberAxis yAxisEleBrf;
	private BarChart<String,Number> costElementBarChartBrf;
	private CategoryAxis xAxisMemBrf;
    private NumberAxis yAxisMemBrf;
	private BarChart<String,Number> teamMemberBarChartBrf;
	
	DecimalFormat df = new DecimalFormat("#.##");
	
	
	/**
     * The constructor will do all the calculation:
     * several maps used to calculate value of amounts,
     * line charts and bar charts.
     * 
     * @param userClaims all the data that belongs to the current user
     * @param filter time filter
     */
	public GeneralInfoCalculator(ArrayList<Receipt> userClaims, String filter) {
		this.userClaims = userClaims;
		this.filter = filter;
		
		timeMap = new TreeMap<String, Double>();
		locationMap = new HashMap<String, Double>();
		costElementMap = new HashMap<String, Double>();
		teamMemberMap = new HashMap<String, Double>();
		
		xAxisTimeBrf = new CategoryAxis();
		yAxisTimeBrf = new NumberAxis();
	    timeLineChartBrf = new LineChart<String,Number>(xAxisTimeBrf, yAxisTimeBrf);
		
		xAxisEleBrf = new CategoryAxis();
	    yAxisEleBrf = new NumberAxis();
		costElementBarChartBrf = new BarChart<>(xAxisEleBrf, yAxisEleBrf);
		xAxisLocBrf = new CategoryAxis();
		yAxisLocBrf = new NumberAxis();
	    locationBarChartBrf = new BarChart<>(xAxisLocBrf, yAxisLocBrf);
	    xAxisMemBrf = new CategoryAxis();
	    yAxisMemBrf = new NumberAxis();
	    teamMemberBarChartBrf = new BarChart<>(xAxisMemBrf, yAxisMemBrf);
	  
		setTotalValues();
				
		setBriefLineChart(timeLineChartBrf, "Time Summary", timeMap);
		
		setBriefBarChartAndLabels(locationBarChartBrf, "Location Summary", getMostThreeValues(locationMap));
		setBriefBarChartAndLabels(costElementBarChartBrf, "Cost Element Summary", getMostThreeValues(costElementMap));
		setBriefBarChartAndLabels(teamMemberBarChartBrf, "Team Member Summary", getMostThreeValues(teamMemberMap));
	}
	
	/**
     * Used to set the maps. Redundant data is removed here.
     */
	private void setTotalValues() {
		for (Receipt r : userClaims) {
			totalInGeneral += r.getAmountInPound();
			if (r.getExpenseDate().contains(filter)) {
				// calculate total values for selected time interval
				if (!r.getLocation().equals("")) {
					addToMap(locationMap, r.getLocation().toLowerCase(), r.getAmountInPound());
				}
				if (!r.getCostElement().equals("")) {
					addToMap(costElementMap, r.getCostElement().toLowerCase(), r.getAmountInPound());
				}
				if (r.getEmployeeID() != 0) {
					addToMap(teamMemberMap, String.valueOf(r.getEmployeeID()), r.getAmountInPound());
				}
				if (!r.getExpenseDate().equals("")) {
					if (filter.length() == 0) {
						addToMap(timeMap, r.getExpenseDate().substring(6), r.getAmountInPound());
					}
					else {
						addToMap(timeMap, r.getExpenseDate().substring(3, 5), r.getAmountInPound());	
					}
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
	
	/**
     * Used to get the most three cost item of element/location/employee, for chart use.
     * 
     * @param hm processing map
     * @return recorder of three items that cost most
     */
	private Recorder[] getMostThreeValues(HashMap<String, Double> hm) {
		Recorder[] tenValuesRecorder = new Recorder[3];
		for (int i = 0; i < 3; i++) {
			tenValuesRecorder[i] = new Recorder("", 0);	
		}	
		
		for (Map.Entry<String, Double> entry : hm.entrySet()) {
		    String key  = entry.getKey(); 
			double value = entry.getValue();
			
			int tempIndex = 0;
			double tempValue = tenValuesRecorder[0].getTotal();
			
			for (int i = 1; i < 3; i++) {
				if (tenValuesRecorder[i].getTotal() < tempValue) {
					tempValue = tenValuesRecorder[i].getTotal();
					tempIndex = i;
				}
			}
			if (value > tempValue) {
				tenValuesRecorder[tempIndex].setPlace(key);
				tenValuesRecorder[tempIndex].setTotal(value);
			}
		}
		
		return tenValuesRecorder;
	}
	
	/**
     * Used to set line chart.
     * 
     * @param lc the line chart
     * @param chart title
     * @param the map that is used
     */
	private void setBriefLineChart(LineChart<String,Number> lc, String title, Map<String, Double> hm) {
		lc.setTitle(title);
		lc.setPrefHeight(200);
		lc.setPrefWidth(500);
        
        Double maxTemp = null;
        Double minTemp = null;
        
        XYChart.Series<String, Number> series = new Series<String, Number>();
        
        for (Map.Entry<String, Double> entry : hm.entrySet()) {
            series.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
            
            if (maxTemp == null) {maxTemp = entry.getValue();}
            else {if (entry.getValue() > maxTemp) maxTemp = entry.getValue();}

            if (minTemp == null) {minTemp = entry.getValue();}
            else {if (entry.getValue() < minTemp) minTemp = entry.getValue();}
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
	private void setBriefBarChartAndLabels(BarChart<String,Number> bc, String title, Recorder[] rc) {
        bc.setTitle(title);
        bc.setPrefHeight(200);
        bc.setPrefWidth(500);
        bc.setBarGap(-20);
        bc.setCategoryGap(100);

        for (int i = 0; i < 3; i++) {
        	XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();	
        	series.getData().add(new XYChart.Data<String,Number>(pruneString(rc[i].getPlace(), 20), rc[i].getTotal()));
        	bc.getData().add(series);
        }
        
        Recorder max = new Recorder(rc[0].getPlace(), rc[0].getTotal());
        for (int i = 1; i < 3; i++) {
        	if (rc[i].getTotal() > max.getTotal()) {
        		max = rc[i];
        	}
        }
        
        if (bc == locationBarChartBrf) {
        	mostAmountInLocation = max.getTotal();
        	mostNameInLocation = max.getPlace();
        }
        else if (bc == costElementBarChartBrf) {
        	mostAmountInCostElement = max.getTotal();
        	mostNameInCostElement = max.getPlace();
        }
        else {
        	mostAmountInTeamMember = max.getTotal();
        	mostNameInTeamMember = max.getPlace();
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

	public double getTotalInGeneral() {
		return Double.valueOf(df.format(totalInGeneral));
	}
	
	public LineChart<String, Number> getTimeLineChartBrf() {
		return timeLineChartBrf;
	}
	
	public BarChart<String, Number> getLocationBarChartBrf() {
		return locationBarChartBrf;
	}
	
	public BarChart<String, Number> getCostElementBarChartBrf() {
		return costElementBarChartBrf;
	}
	
	public BarChart<String, Number> getTeamMemberBarChartBrf() {
		return teamMemberBarChartBrf;
	}

	public double getMostAmountInLocation() {
		return Double.valueOf(df.format(mostAmountInLocation));
	}
	
	public String getMostNameInLocation() {
		return mostNameInLocation;
	}
	
	public double getMostAmountInCostElement() {
		return Double.valueOf(df.format(mostAmountInCostElement));
	}
	
	public String getMostNameInCostElement() {
		return mostNameInCostElement;
	}
	
	public double getMostAmountInTeamMember() {
		return Double.valueOf(df.format(mostAmountInTeamMember));
	}
	
	public String getMostNameInTeamMember() {
		return mostNameInTeamMember;
	}
}