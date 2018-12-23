package main.net.atos.uk.TravelDashboard.Dashboard.Main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;
import main.net.atos.uk.TravelDashboard.Database.DataConnector;

/**
 * This class is used to generate budget chart and corresponding labels for the main Dashboard.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class BudgetCalculator {
	
	/**
     * Contains all the uploaded data belongs to the current user.
     */
	private ArrayList<Receipt> userClaims;
	/**
     * Contains the time filter, "" for all and "xxxx" (for example "2016") for the last year
     */
	private String filter;
	
	/**
     * Map used to calculate the actual cost of the selected six months.
     */
	private HashMap<String, Double> actualCostMap;
	
	private CategoryAxis xAxisBud;
    private NumberAxis yAxisBud;
	private BarChart<String, Number> budgetBarChart;
	
	/**
     * Observable list used in main Dashboard ComboBox to show all possible options of time 
     * (H1 and H2 represent for first six month and last six month of a year).
     */
	private ObservableList<String> actualCostList = FXCollections.observableArrayList();
	
	DecimalFormat df = new DecimalFormat("#.##");
	
	private double budgetAmount = 0;
	
	/**
     * The constructor will do all the calculation:
     * map used to calculate value of actual cost,
     * the budget bar chart.
     * 
     * @param userClaims all the data that belongs to the current user
     * @param filter time filter
     */
	public BudgetCalculator(ArrayList<Receipt> userClaims, String filter) {		
		this.userClaims = userClaims;

		actualCostMap = new HashMap<String, Double>();
		
		xAxisBud = new CategoryAxis();
	    yAxisBud = new NumberAxis();
	    budgetBarChart = new BarChart<>(xAxisBud, yAxisBud); 
	    
	    setActualCostList();
	    DataConnector dc = new DataConnector();
	    
	    if (actualCostList.size() > 0 && filter.equals("")) this.filter = actualCostList.get(0);
		else this.filter = filter;
	    
	    if (actualCostList.size() > 0) {
	    	budgetAmount = dc.toGetUserBudget(this.filter);
	    	setBudgetChart(budgetAmount, setMonthBudgetForChart());
	    }
	}
	
	/**
     * Used to set up observable list.
     */
	private void setActualCostList() {
		for (Receipt r : userClaims) {
			if (!r.getExpenseDate().equals("")) {
				if (Integer.valueOf(r.getExpenseDate().substring(3, 5)) <= 6) {
					addToMap(actualCostMap, r.getExpenseDate().substring(6) + "-H1", r.getAmountInPound());
				}
				else {
					addToMap(actualCostMap, r.getExpenseDate().substring(6) + "-H2", r.getAmountInPound());	
				}
			}
		}
		
		for (Map.Entry<String, Double> r : actualCostMap.entrySet()) {
			actualCostList.add(r.getKey());
		}
		Comparator<String> myComparator = new Comparator<String>() {
	        @Override
	        public int compare(String s1, String s2) {
	        	return s2.compareTo(s1);
	        }
	    };
	    actualCostList = actualCostList.sorted(myComparator); 
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
     * Used to set an array that contains the selected six month's actual cost.
     */
	private double[] setMonthBudgetForChart() {
		double sixMonth[] = {0, 0, 0, 0, 0, 0};
		
		for (Receipt r : userClaims) {
			// claims within the year
			if (!r.getExpenseDate().equals("") && r.getExpenseDate().substring(6).equals(filter.substring(0, 4))) {
				if (filter.substring(5).equals("H1")) {
					if (Integer.valueOf(r.getExpenseDate().substring(3, 5)) <= 6)
							sixMonth[Integer.valueOf(r.getExpenseDate().substring(3, 5))-1] += r.getAmountInPound();
				}
				else {
					if (Integer.valueOf(r.getExpenseDate().substring(3, 5)) > 6)
						sixMonth[Integer.valueOf(r.getExpenseDate().substring(3, 5))-7] += r.getAmountInPound();
				}
			}
		}
		
		return sixMonth;
	}
	
	/**
     * Used to set the budget bar chart.
     * 
     * @param budgetAmount total budget of the selected six months
     * @param sixMonth an array that contains the selected six month's actual cost
     */
	@SuppressWarnings("unchecked")
	private void setBudgetChart(double budgetAmount, double[] sixMonth) {
		final String m1 = "1";
	    final String m2 = "2";
	    final String m3 = "3";
	    final String m4 = "4";
	    final String m5 = "5";
	    final String m6 = "6";
		
		budgetBarChart.setTitle("Budget Comparison");
		xAxisBud.setTickLabelRotation(90);
	 
		budgetBarChart.setPrefHeight(200);
		budgetBarChart.setPrefWidth(500);
		budgetBarChart.setBarGap(0);
		budgetBarChart.setCategoryGap(30);
		
		XYChart.Series<String, Number> series2 = new Series<String, Number>();
		series2.getData().add(new XYChart.Data<String, Number>(m1, budgetAmount / 6));
		series2.getData().add(new XYChart.Data<String, Number>(m2, budgetAmount / 6));
		series2.getData().add(new XYChart.Data<String, Number>(m3, budgetAmount / 6));
		series2.getData().add(new XYChart.Data<String, Number>(m4, budgetAmount / 6));
		series2.getData().add(new XYChart.Data<String, Number>(m5, budgetAmount / 6));
		series2.getData().add(new XYChart.Data<String, Number>(m6, budgetAmount / 6));
		XYChart.Series<String, Number> series1 = new Series<String, Number>();
		series1.getData().add(new XYChart.Data<String, Number>(m1, sixMonth[0]));
		series1.getData().add(new XYChart.Data<String, Number>(m2, sixMonth[1]));
		series1.getData().add(new XYChart.Data<String, Number>(m3, sixMonth[2]));
		series1.getData().add(new XYChart.Data<String, Number>(m4, sixMonth[3]));
		series1.getData().add(new XYChart.Data<String, Number>(m5, sixMonth[4]));
		series1.getData().add(new XYChart.Data<String, Number>(m6, sixMonth[5]));
		
		
	    budgetBarChart.getData().addAll(series2, series1);
	}
	
	public ObservableList<String> getActualCostList() {
		return actualCostList;
	}
	
	public BarChart<String, Number> getBudgetChart() {
		return budgetBarChart;
	}
	
	public double getActualCostAmount() {
		return Double.valueOf(df.format(actualCostMap.get(filter)));	
	}
	
	public double getBudgetAmount() {
		return Double.valueOf(df.format(budgetAmount));
	}
	
}
