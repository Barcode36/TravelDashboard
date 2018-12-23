package main.net.atos.uk.TravelDashboard.Dashboard.Main;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.net.atos.uk.TravelDashboard.ClaimItem.ExcelFile;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;
import main.net.atos.uk.TravelDashboard.ClaimItem.ReceiptForAnalysis;
import main.net.atos.uk.TravelDashboard.Dashboard.Report.DateSelector;
import main.net.atos.uk.TravelDashboard.Database.CurrentUserInfo;
import main.net.atos.uk.TravelDashboard.Database.DataConnector;

/**
 * This class is the controller class of the main Dashboard when the user logged in successfully.
 * For the main Dashboard page, the DashboardRoot page contains the menu bar in the left of the screen.
 * In total, there are four buttons:
 * Main - main Dashboard
 * Upload - upload page
 * Report - report page
 * Exit - to log out
 * 
 * The menu bar will always appear except the user log out. This is the reason that Root layout and Main
 * layout are separated.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class MainDashboardController implements Initializable {
	
	/**
     * Dashboard root pane, used to draw white background.
     */
	@FXML private AnchorPane apDashboard;
	
	/**
     * Time summary line chart.
     */
	@FXML private AnchorPane apLineChart;
	/**
     * Total button above the line chart.
     */
	@FXML private Button totalButton;
	/**
     * Year button above the line chart. It always shows the last year of all upload data.
     */
	@FXML private Button yearButton;
	
	/**
     * Anchor pane of optional bar chart providing three options: Element, Location and Member
     */
	@FXML private AnchorPane apChartOptions;
	/**
     * Element button above the optional bar chart.
     */
	@FXML private Button elementButton;
	/**
     * Location button above the optional bar chart.
     */
	@FXML private Button locationButton;
	/**
     * Member button above the optional bar chart.
     */
	@FXML private Button memberButton;
	
	/**
     * Anchor pane of budget bar chart providing one of half year's budget comparison.
     */
	@FXML private AnchorPane apBudget;
	/**
     * ComboBox above the budget bar chart to choose time (for example 2016-H2 is the last six month of 2016).
     */
	@FXML private ComboBox<String> budgetDateOptions;
	/**
     * Actual cost amount used in comparison of budget cost.
     */
	@FXML private Label lbActualCostAmount;
	/**
     * Budget cost amount used in budget cost, default 0.
     */
	@FXML private Label lbBudgetAmount;
	/**
     * Label used to indicate changing budget when cursor hover into it, that is: change the label from "Budget" to
     * "Click it to change".
     */
	@FXML private Label lbBudgetChange;
	/**
     * Text field used to input the new budget amount when the lbBudgetAmount label is clicked, default hidden. After
     * change confirmed, hidden again.
     */
	@FXML private TextField tfChangeBudget;
	/**
     * Button used to confirm the new budget amount, default hidden.
     */
	@FXML private Button ChangeBudgetButton;
	
	/**
     * The label to show the amount of cost of all updated data.
     */
	@FXML private Label lbTotalInGeneral;
	/**
     * The label to show the amount of cost of cost element, whether all or the last, depending on user selection.
     */
	@FXML private Label lbMostAmountInCostElement;
	/**
     * The label to show the name of cost of cost element, whether all or the last, depending on user selection.
     */
	@FXML private Label lbMostNameInCostElement;
	/**
     * The label to show the amount of cost of location, whether all or the last, depending on user selection.
     */
	@FXML private Label lbMostAmountInLocation;
	/**
     * The label to show the name of cost of location, whether all or the last, depending on user selection.
     */
	@FXML private Label lbMostNameInLocation;
	/**
     * The label to show the amount of cost of team member, whether all or the last, depending on user selection.
     */
	@FXML private Label lbMostAmountInTeamMember;
	/**
     * The label to show the name of cost of team member, whether all or the last, depending on user selection.
     */
	@FXML private Label lbMostNameInTeamMember;
	
	/**
     * The anchor pane contains the current user information: user ID, username and email.
     */
	@FXML private AnchorPane apUserInfo;
	
	/**
     * The anchor pane contains the record of last four upload files.
     */
	@FXML private AnchorPane apLastestAvtivities;
	/**
     * The anchor pane contains the record of one of the last four upload files (1/4).
     */
	@FXML private AnchorPane latestUploadAp1;
	/**
     * The anchor pane contains the record of one of the last four upload files (2/4).
     */
	@FXML private AnchorPane latestUploadAp2;
	/**
     * The anchor pane contains the record of one of the last four upload files (3/4).
     */
	@FXML private AnchorPane latestUploadAp3;
	/**
     * The anchor pane contains the record of one of the last four upload files (4/4).
     */
	@FXML private AnchorPane latestUploadAp4;
	
	/**
     * The circle is used to load Atos image.
     */
	@FXML private Circle portrait;
	
	/**
     * Contains the record of one of the last four upload files.
     */
	private ObservableList<ExcelFile> excelFileList;
	
	/**
     * Contains all the uploaded data belongs to the current user.
     */
	private ArrayList<Receipt> userClaims;
	/**
     * Transfer all the uploaded data belongs to the current user to analysis form, essentially only one time type 
     * (ExpenseDate in main Dashboard).
     */
	private ArrayList<ReceiptForAnalysis> userClaimsForAnalysis = new ArrayList<ReceiptForAnalysis>();
	/**
     * A class from Report package, used to find the last year data of all uploaded data.
     */
	private DateSelector ds;
	/**
     * Contains the last year string, for example 2016 is "2016". Pass it into "GeneralInfoCalculator" to filter the
     * specific last year data.
     */
	private String lastYearFilter;
	/**
     * Main Dashboard element chart, display three elements that cost the most in all/last year.
     */
	private BarChart<String, Number> elementChart;
	/**
     * Main Dashboard element chart, display three locations that cost the most in all/last year.
     */
	private BarChart<String, Number> locationChart;
	/**
     * Main Dashboard element chart, display three employees that cost the most in all/last year.
     */
	private BarChart<String, Number> memberChart;
	
	/**
     * The function initialise the following elements:
     * Atos logo,
     * current user information,
     * the last four upload files of current user,
     * extract data from database, all data in the form of ArrayList,
     * initialise all charts in updateCharts,
     * initialise the budget data.
     */
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		Image img = new Image("atos.jpg");
		portrait.setFill(new ImagePattern(img));
		
		setUserInfo();
		setUploadList();
		
		extractData();
		setUpUserClaimsForAnalysis();
		
		ds = new DateSelector(userClaimsForAnalysis);
		
		// if the last year selected
		if (ds.findComboBoxOptionsByYear().size() > 0) {
			lastYearFilter = ds.findComboBoxOptionsByYear().get(0);	
		}
		
		updateCharts("");
		
		budgetDateOptions.getSelectionModel().selectedItemProperty().addListener(event -> {
			updateBudgetChartAndLabels(budgetDateOptions.getSelectionModel().getSelectedItem());
		});
		
		BudgetCalculator budgetCalculator = new BudgetCalculator(userClaims, "");
		budgetDateOptions.setItems(budgetCalculator.getActualCostList());
		budgetDateOptions.getSelectionModel().selectFirst();
		
		lbBudgetAmount.setOnMouseEntered(event -> {
			lbBudgetChange.setText("Click it to change");
		});
		lbBudgetAmount.setOnMouseExited(event -> {
			lbBudgetChange.setText("Budget");
		});
	}
	
	/**
     * Used to update budget chart and relative labels.
     * 
     * @param filter time filter, "" for all and "xxxx" (for example "2016") for the last year
     */
	private void updateBudgetChartAndLabels(String filter) {
		BudgetCalculator budgetCalculator = new BudgetCalculator(userClaims, filter);
		apBudget.getChildren().clear();
		apBudget.getChildren().add(budgetCalculator.getBudgetChart());
		
		lbBudgetAmount.setText(String.valueOf(budgetCalculator.getBudgetAmount()));
		lbActualCostAmount.setText(String.valueOf(budgetCalculator.getActualCostAmount()));
	}
	
	/**
     * Used to change Receipt to Analysis Receipt, essentially only one time type, ExpenseDate here.
     */
	private void setUpUserClaimsForAnalysis() {
		userClaimsForAnalysis.clear();
		
		for (Receipt r : userClaims) {
			ReceiptForAnalysis rfa;
			rfa = new ReceiptForAnalysis(r.getWbsNumber(), r.getAmountInPound(), 
					r.getCostElement(), r.getLocation(), r.getExpenseDate(), r.getEmployeeID(), 0);	
			userClaimsForAnalysis.add(rfa);
		}
	}
	
	/**
     * Used to set the record of the last four upload files.
     */
	private void setUploadList() {
		DataConnector dataConnector = new DataConnector();
		excelFileList = dataConnector.toGetExcelFileList();

		for (int i = 0; i < 4; i++) {
			if (i < excelFileList.size()) {
				Label lbName = new Label(excelFileList.get(i).getExcelFileName());
				Label lbTime = new Label(excelFileList.get(i).getExcelFileUploadTime());
				lbName.setFont(Font.font(null, FontWeight.BOLD, 14));
				
				AnchorPane.setLeftAnchor(lbName, 10.0);
				AnchorPane.setLeftAnchor(lbTime, 10.0);
				AnchorPane.setTopAnchor(lbName, 5.0);
				AnchorPane.setTopAnchor(lbTime, 25.0);
				
				if (i == 0) {
					latestUploadAp1.getChildren().add(lbName);
					latestUploadAp1.getChildren().add(lbTime);
				}
				else if (i == 1) {
					latestUploadAp2.getChildren().add(lbName);
					latestUploadAp2.getChildren().add(lbTime);
				}
				else if (i == 2) {
					latestUploadAp3.getChildren().add(lbName);
					latestUploadAp3.getChildren().add(lbTime);
				}
				else {
					latestUploadAp4.getChildren().add(lbName);
					latestUploadAp4.getChildren().add(lbTime);
				}
			}
		}
	}
	
	/**
     * Used to set the current user information
     */
	private void setUserInfo() {
		Label userID = new Label(String.valueOf(CurrentUserInfo.getUserID()));
		AnchorPane.setLeftAnchor(userID, 90.0);
		AnchorPane.setTopAnchor(userID, 20.0);
		Label userName = new Label(CurrentUserInfo.getUsername());
		AnchorPane.setLeftAnchor(userName, 90.0);
		AnchorPane.setTopAnchor(userName, 50.0);
		Label userEmail = new Label(CurrentUserInfo.getUserEmail());
		AnchorPane.setLeftAnchor(userEmail, 90.0);
		AnchorPane.setTopAnchor(userEmail, 80.0);
		
		apUserInfo.getChildren().addAll(userID, userName, userEmail);
	}
	
	/**
     * Used to update charts, when Total or Year button is selected. 
     * 
     * @param filter time filter, "" for all and "xxxx" (for example "2016") for the last year
     */
	private void updateCharts(String filter) {
		GeneralInfoCalculator calculator = new GeneralInfoCalculator(userClaims, filter);
		apLineChart.getChildren().clear(); 
		apChartOptions.getChildren().clear();
		LineChart<String, Number> lc = calculator.getTimeLineChartBrf();
		
		apLineChart.getChildren().add(lc);
		elementChart = calculator.getCostElementBarChartBrf();
		locationChart = calculator.getLocationBarChartBrf();
		memberChart = calculator.getTeamMemberBarChartBrf();
		apChartOptions.getChildren().add(elementChart);
				
		lbTotalInGeneral.setText(String.valueOf(calculator.getTotalInGeneral()));
		lbMostAmountInLocation.setText(String.valueOf(calculator.getMostAmountInLocation()));
		lbMostNameInLocation.setText(calculator.getMostNameInLocation());
		lbMostAmountInCostElement.setText(String.valueOf(calculator.getMostAmountInCostElement()));
		lbMostNameInCostElement.setText(calculator.getMostNameInCostElement());
		lbMostAmountInTeamMember.setText(String.valueOf(calculator.getMostAmountInTeamMember()));
		lbMostNameInTeamMember.setText(calculator.getMostNameInTeamMember());
	}
	
	/**
     * Used when the Budget label is clicked, TextField and Button appear.
     */
	@FXML void requestSetBudget() {
		tfChangeBudget.setVisible(true);
		ChangeBudgetButton.setVisible(true);
		tfChangeBudget.requestFocus();
	}
	
	/**
     * Used when the confirm Button is clicked, TextField and Button disappear.
     */
	@FXML void confirmSetBudget() {
		try {
            double budget = Double.parseDouble(tfChangeBudget.getText());
            DataConnector dc = new DataConnector();
            dc.toUploadUserBudget(budgetDateOptions.getSelectionModel().getSelectedItem(), budget);
        } catch (NumberFormatException e) {
            System.out.println("Input Not Valid!");
        }
			
		tfChangeBudget.setVisible(false);
		ChangeBudgetButton.setVisible(false);
		
		updateBudgetChartAndLabels(budgetDateOptions.getSelectionModel().getSelectedItem());
	}
	
	/**
     * Used when Total is clicked.
     */
	@FXML private void setLineChartByTotal() {
		updateCharts("");		
	}

	/**
     * Used when Year is clicked.
     */
	@FXML private void setLineChartByYear() {
		updateCharts(lastYearFilter);
	}
	
	/**
     * Used when Element is clicked.
     */
	@FXML private void elementChartView() {
		apChartOptions.getChildren().clear();
		apChartOptions.getChildren().add(elementChart);
	}	
	
	/**
     * Used when Location is clicked.
     */
	@FXML private void locationChartView() {
		apChartOptions.getChildren().clear();
		apChartOptions.getChildren().add(locationChart);
	}
	
	/**
     * Used when Member is clicked.
     */
	@FXML private void memberChartView() {
		apChartOptions.getChildren().clear();
		apChartOptions.getChildren().add(memberChart);
	}
	
	/**
     * Used to connect to the database and get all the data belongs to current user.
     */
	private void extractData() {
		DataConnector dataConnector = new DataConnector();
		userClaims = dataConnector.toGetUserClaims();
	}
}
