package main.net.atos.uk.TravelDashboard.Dashboard.Report;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import main.net.atos.uk.TravelDashboard.ClaimItem.ExcelFile;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;
import main.net.atos.uk.TravelDashboard.ClaimItem.ReceiptForAnalysis;
import main.net.atos.uk.TravelDashboard.Database.DataConnector;

/**
 * This class is the controller class of the report page. Essentially it has three functions:
 * to specify the options (WBS number, Employee ID, time type options, etc.),
 * to generate the report,
 * to generate the PDF document of the report, to the OS Desktop. 
 * 
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class ReportCentreController implements Initializable {
	
	/**
     * Anchor pane of report page.
     */
	@FXML private AnchorPane apReportCentre;
	
	/**
     * ComboBox to specify WBS number.
     */
	@FXML private ComboBox<String> wbsNumberOptions;
	/**
     * ComboBox to specify report for total or one employee.
     */
	@FXML private ComboBox<String> totalOrPerson;
	/**
     * ComboBox to specify the employee ID if the report is for one employee.
     */
	@FXML private ComboBox<String> personOptions;
	/**
     * ComboBox to specify ExpenseDate or TripEndDate.
     */
	@FXML private ComboBox<String> timeOptions;
	
	/**
     * Button to generate report.
     */
	@FXML private Button generateReportButton;
	
	/**
     * Scroll pane of the report, which contains an anchor pane.
     */
	@FXML private ScrollPane spSummary;
	
	/**
     * Anchor pane of the report, inside the scroll pane.
     */
	@FXML private AnchorPane apSummary;
	
	/**
     * Anchor pane of general information, the first part of the report.
     */
	@FXML private AnchorPane apReportGeneralInfo;
	/**
     * Anchor pane of line chart and a table of time, the second part of the report.
     */
	@FXML private AnchorPane apReportTime;
	/**
     * Anchor pane of bar chart and a table of location, the third part of the report.
     */
	@FXML private AnchorPane apReportLocation;
	/**
     * Anchor pane of bar chart and a table of element, the fourth part of the report.
     */
	@FXML private AnchorPane apReportElement;
	/**
     * Anchor pane of bar chart and a table of member, the fifth part of the report. If
     * individual report is selected, this part will be nothing.
     */
	@FXML private AnchorPane apReportMember;
	
	/**
     * Label for report title, for example "GB.708467.454.01: Team" means Team report for that WBS number.
     * If it's individual, "Team" will be replaced by that employee's ID
     */
	@FXML private Label lbReportTitle;
	
	/**
     * Label for report to show time type: ExpenseDate or TripEndDate.
     */
	@FXML private Label lbReportTimeType;
	
	/**
     * Table to show which files are used in analysis of the report, In the first part of the report.
     */
	@FXML private TableView<ExcelFile> usedFilesTable;
	/**
     * Column of usedFilesTable.
     */
	@FXML private TableColumn<ExcelFile, String> usedFilesColumn;
	
	/**
     * The Total button below the lbReportTitle and lbReportTimeType, meaning all data selected.
     */
	@FXML private Button totalButton;
	/**
     * The Year button below the lbReportTitle and lbReportTimeType, meaning a year data selected.
     */
	@FXML private Button yearButton;
	/**
     * The Quarter button below the lbReportTitle and lbReportTimeType, meaning a quarter data selected.
     */
	@FXML private Button quarterButton;
	/**
     * The Month button below the lbReportTitle and lbReportTimeType, meaning a month data selected.
     */
	@FXML private Button monthButton;
	/**
     * The Week button below the lbReportTitle and lbReportTimeType, meaning a week data selected.
     */
	@FXML private Button weekButton;
	/**
     * The list to provide year options.
     */
	@FXML private ComboBox<String> listOptions1;
	/**
     * The list to provide quarter/month/year options.
     */
	@FXML private ComboBox<String> listOptions2;
	
	/**
     * Label in apReportTime.
     */
	@FXML private Label lbTotalInTime;
	/**
     * Label in apReportTime.
     */
	@FXML private Label lbAveInTime;
	/**
     * Label in apReportTime.
     */
	@FXML private Label lbMaxInTime;
	/**
     * Label in apReportTime.
     */
	@FXML private Label lbMinInTime;
	
	/**
     * Table in apReportTime.
     */
	@FXML private TableView<Recorder> timeTable;
	@FXML private TableColumn<Recorder, String> timeDateColumn;
	@FXML private TableColumn<Recorder, Double> timeAmountColumn;
	/**
     * Anchor pane that contains bar chart in apReportTime.
     */
	@FXML private AnchorPane apTimeChart;
	
	/**
     * Table in apReportLocation.
     */
	@FXML private TableView<Recorder> locationTable;
	@FXML private TableColumn<Recorder, String> locationNameColumn;
	@FXML private TableColumn<Recorder, Double> locationAmountColumn;
	/**
     * Anchor pane that contains bar chart in apReportLocation.
     */
	@FXML private AnchorPane apLocationChart;
	
	/**
     * Table in apReportElement.
     */
	@FXML private TableView<Recorder> costElementTable;
	@FXML private TableColumn<Recorder, String> costElementNameColumn;
	@FXML private TableColumn<Recorder, Double> costElementAmountColumn;
	/**
     * Anchor pane that contains bar chart in apReportElement.
     */
	@FXML private AnchorPane apCostElementChart;
	
	/**
     * Table in apReportMember.
     */
	@FXML private TableView<Recorder> teamMemberTable;
	@FXML private TableColumn<Recorder, String> teamMemberNameColumn;
	@FXML private TableColumn<Recorder, Double> teamMemberAmountColumn;
	/**
     * Anchor pane that contains bar chart in apReportMember.
     */
	@FXML private AnchorPane apTeamMemberChart;
	
	/**
     * Label in apReportLocation.
     */
	@FXML private Label lbTotalInLocation;
	/**
     * Label in apReportLocation.
     */
	@FXML private Label lbAmountInLocation1;
	/**
     * Label in apReportLocation.
     */
	@FXML private Label lbPlaceInLocation1;
	/**
     * Label in apReportLocation.
     */
	@FXML private Label lbAmountInLocation2;
	/**
     * Label in apReportLocation.
     */
	@FXML private Label lbPlaceInLocation2;
	/**
     * Label in apReportLocation.
     */
	@FXML private Label lbAmountInLocation3;
	/**
     * Label in apReportLocation.
     */
	@FXML private Label lbPlaceInLocation3;
	
	/**
     * Label in apReportElement.
     */
	@FXML private Label lbTotalInCostElement;
	/**
     * Label in apReportElement.
     */
	@FXML private Label lbAmountInCostElement1;
	/**
     * Label in apReportElement.
     */
	@FXML private Label lbPlaceInCostElement1;
	/**
     * Label in apReportElement.
     */
	@FXML private Label lbAmountInCostElement2;
	/**
     * Label in apReportElement.
     */
	@FXML private Label lbPlaceInCostElement2;
	/**
     * Label in apReportElement.
     */
	@FXML private Label lbAmountInCostElement3;
	/**
     * Label in apReportElement.
     */
	@FXML private Label lbPlaceInCostElement3;
	
	/**
     * Label in apReportMember.
     */
	@FXML private Label lbTotalInTeamMember;
	/**
     * Label in apReportMember.
     */
	@FXML private Label lbAmountInTeamMember1;
	/**
     * Label in apReportMember.
     */
	@FXML private Label lbNameInTeamMember1;
	/**
     * Label in apReportMember.
     */
	@FXML private Label lbAmountInTeamMember2;
	/**
     * Label in apReportMember.
     */
	@FXML private Label lbNameInTeamMember2;
	/**
     * Label in apReportMember.
     */
	@FXML private Label lbAmountInTeamMember3;
	/**
     * Label in apReportMember.
     */
	@FXML private Label lbNameInTeamMember3;
	/**
     * Label in apReportMember.
     */
	@FXML private Label memberInComparison;
	
	/**
     * Used to generate the second page of PDF report.
     */
	private ObservableList<Recorder> timeListForTablePrintUse;
	/**
     * Used to generate the second page of PDF report.
     */
	private ObservableList<Recorder> locationListForTablePrintUse;
	/**
     * Used to generate the second page of PDF report.
     */
	private ObservableList<Recorder> costElementListForTablePrintUse;
	/**
     * Used to generate the second page of PDF report.
     */
	private ObservableList<Recorder> teamMemberListForTablePrintUse;
	/**
     * Used to generate the second page of PDF report.
     */
	@FXML private AnchorPane apForTablePrintUse;
	
	
	/**
     * Button to generate PDF report.
     */
	@FXML private Button generatePDF;
	/**
     * Label to give successful information when PDF is generated.
     */
	@FXML private Label successPDFInfo;
	
	/**
     * Contains all the data.
     */
	private ArrayList<Receipt> userClaims;
	
	/**
     * Used to generate table in apReportGeneralInfo. It contains the files that are used 
     * to generate the report.
     */
	private ObservableList<ExcelFile> usedFileList = FXCollections.observableArrayList();
	/**
     * The string content is the first selected ComboBox of the report below the apReportGeneralInfo.
     * If Total is selected, it is "", if Year is selected it is "xxxx" (for example "2016").
     */
	private String filterYear = "";
	/**
     * The string content is the second selected ComboBox of the report below the apReportGeneralInfo.
     * For example if the filterYear is "2016", and Month is selected, then it can be "2016Mx", x can be
     * any possible month that is selected.
     */
	private String filterDetail = "";
	
	/**
     * It is used to specify the time type: Expense Date(0) or Trip End Date(1).
     */
	private int timeOptionIndex = 0;
	
	
	/**
     * Initialise all the ComboBoxs and charts. 
     */
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		usedFilesColumn.setCellValueFactory(new PropertyValueFactory<>("excelFileName"));
		
		timeDateColumn.setCellValueFactory(new PropertyValueFactory<>("place"));	
		timeAmountColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		locationNameColumn.setCellValueFactory(new PropertyValueFactory<>("place"));	
		locationAmountColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		costElementNameColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
		costElementAmountColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		teamMemberNameColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
		teamMemberAmountColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		
		listOptions1.getSelectionModel().selectedItemProperty().addListener(event -> {
			filterYear = listOptions1.getSelectionModel().getSelectedItem();
			if (filterYear != null) {
				if (timeOptions.getSelectionModel().getSelectedIndex() == 0) {
					timeOptionIndex = 0;
					updateClaims(filterYear);
				}
				else {
					timeOptionIndex = 1;
					updateClaims(filterYear);
				}
			}
		});
		
		listOptions2.getSelectionModel().selectedItemProperty().addListener(event -> {
			filterDetail = listOptions2.getSelectionModel().getSelectedItem();
			if (filterDetail != null) {
				if (timeOptions.getSelectionModel().getSelectedIndex() == 0) {
					timeOptionIndex = 0;
					updateClaims(filterDetail);
				}
				else {
					timeOptionIndex = 1;
					updateClaims(filterDetail);
				}
			}
		});
		
		extractData();
		
		usedFilesTable.setItems(usedFileList);
		
		ObservableList<String> wbsNumberOptionsList = FXCollections.observableArrayList();
		for (Receipt r : userClaims) {
			if (!wbsNumberOptionsList.contains(String.valueOf(r.getWbsNumber()))) {
				wbsNumberOptionsList.add(r.getWbsNumber());
			}
		}
		
		wbsNumberOptions.getSelectionModel().selectedItemProperty().addListener(event -> {
			ObservableList<String> personList = FXCollections.observableArrayList();
			for (Receipt r : userClaims) {
				
				if (r.getEmployeeID() != 0 && !personList.contains(String.valueOf(r.getEmployeeID()))
						&& r.getWbsNumber().equals(wbsNumberOptions.getSelectionModel().getSelectedItem())) {
					personList.add(String.valueOf(r.getEmployeeID()));
				}
			}
			totalOrPerson.getSelectionModel().select(0);
			personOptions.setItems(personList.sorted());
			personOptions.setDisable(true);
		});
		
		wbsNumberOptions.setItems(wbsNumberOptionsList);
		wbsNumberOptions.getSelectionModel().selectFirst();
		
		ObservableList<String> totalOrPersonList = FXCollections.observableArrayList();
		totalOrPersonList.addAll("Total", "Person");
		totalOrPerson.setItems(totalOrPersonList);
		totalOrPerson.getSelectionModel().selectFirst();
		
		ObservableList<String> timeOptionsList = FXCollections.observableArrayList();
		timeOptionsList.addAll("Claim Date", "Trip End Date");
		timeOptions.setItems(timeOptionsList);
		timeOptions.getSelectionModel().selectFirst();
		
		totalOrPerson.getSelectionModel().selectedItemProperty().addListener(event -> {
			if (totalOrPerson.getSelectionModel().getSelectedItem().equals("Person")) {
				personOptions.setDisable(false);
				personOptions.getSelectionModel().selectFirst();
			}
			else {
				personOptions.getSelectionModel().clearSelection();
				personOptions.setDisable(true);
			}

		});
	}
	
	/**
     * It is called when the user click generate report button. 
     */
	@FXML private void generateReport() {
		wbsNumberOptions.setVisible(false);
		totalOrPerson.setVisible(false);
		timeOptions.setVisible(false);
		personOptions.setVisible(false);
		generateReportButton.setVisible(false);
		spSummary.setVisible(true);
		
		if (totalOrPerson.getSelectionModel().getSelectedItem().equals("Total")) {
			lbReportTitle.setText(wbsNumberOptions.getSelectionModel().getSelectedItem() + ": Team");
		}
		else {
			lbReportTitle.setText(wbsNumberOptions.getSelectionModel().getSelectedItem() + ": " + personOptions.getSelectionModel().getSelectedItem());
		}
		
		if (timeOptions.getSelectionModel().getSelectedItem().equals("Claim Date")) {
			lbReportTimeType.setText("Claim Date");
		}
		else {
			lbReportTimeType.setText("Trip End Date");
		}
		
		selectTotal();
		
		generatePDF.setVisible(true);
	}
	
	/**
     * It is called after the user clicked the generate report button(default setting), and when 
     * the user click Total button in the report. 
     */
	@FXML private void selectTotal() {
		listOptions1.setItems(null);
		listOptions2.setItems(null);
		
		listOptions1.setDisable(true);
		listOptions2.setDisable(true);
		
		quarterButton.setDisable(true);
		monthButton.setDisable(true);
		weekButton.setDisable(true);
		
		updateClaims("");
	}
	
	/**
     * It is called when the user click Year button in the report. The first option is selected by default. 
     */
	@FXML private void selectYear() {
		ArrayList<ReceiptForAnalysis> claimsForAnalysis = setUpClaimsForAnalysis(timeOptionIndex);
		DateSelector ds = new DateSelector(claimsForAnalysis);
		listOptions1.setItems(ds.findComboBoxOptionsByYear());
		
		listOptions1.setDisable(false);
		quarterButton.setDisable(false);
		monthButton.setDisable(false);
		weekButton.setDisable(false);
	
		listOptions1.getSelectionModel().selectFirst();
		
		listOptions2.setItems(null);
		listOptions2.setDisable(true);
	}
	
	/**
     * It is called when the user click Month button in the report. The first option is selected by default. 
     */
	@FXML private void selectMonthWithinYear() {
		ArrayList<ReceiptForAnalysis> claimsForAnalysis = setUpClaimsForAnalysis(timeOptionIndex);
		DateSelector ds = new DateSelector(claimsForAnalysis);
		listOptions1.setDisable(true);
		listOptions2.setDisable(false);
		
		listOptions2.setItems(ds.findComboBoxOptionsByMonthWithinYear(filterYear));
		listOptions2.getSelectionModel().selectFirst();
	}
	
	/**
     * It is called when the user click Quarter button in the report. The first option is selected by default. 
     */
	@FXML private void selectQuarterWithinYear() {
		ArrayList<ReceiptForAnalysis> claimsForAnalysis = setUpClaimsForAnalysis(timeOptionIndex);
		DateSelector ds = new DateSelector(claimsForAnalysis);
		listOptions1.setDisable(true);
		listOptions2.setDisable(false);
		
		listOptions2.setItems(ds.findComboBoxOptionsByQuarterWithinYear(filterYear));
		listOptions2.getSelectionModel().selectFirst();
	}
	
	/**
     * It is called when the user click Week button in the report. The first option is selected by default. 
     */
	@FXML private void selectWeekWithinYear() {
		ArrayList<ReceiptForAnalysis> claimsForAnalysis = setUpClaimsForAnalysis(timeOptionIndex);
		DateSelector ds = new DateSelector(claimsForAnalysis);
		listOptions1.setDisable(true);
		listOptions2.setDisable(false);
		
		listOptions2.setItems(ds.findComboBoxOptionsByWeekWithinYear(filterYear));
		listOptions2.getSelectionModel().selectFirst();
	}
	
	/**
     * It is called when charts and data need to be updated when the user click any of the button in the report
     * page. For example, the default setting is Total, when the user click Year, all ther charts and data need 
     * to be updated.
     * 
     * @param filter the time filter is passed into ReportDataCalculator so that the system can get rid of the
     * no-needed data. For example when "2016" is passed in, all the claims that are not in 2016 will be deleted
     * to generate the correct report.
     */
	private void updateClaims(String filter) {
		ArrayList<ReceiptForAnalysis> userClaimsForAnalysis = setUpClaimsForAnalysis(timeOptionIndex);
		
		ReportDataCalculator rdc = new ReportDataCalculator(userClaimsForAnalysis, filter);
		
		lbTotalInLocation.setText(String.valueOf(rdc.getTotalInLocation()));
		lbTotalInCostElement.setText(String.valueOf(rdc.getTotalInCostElement()));
		lbTotalInTeamMember.setText(String.valueOf(rdc.getTotalInTeamMember()));
		
		timeTable.setItems(rdc.getTimeListForTableUse());
		timeListForTablePrintUse = rdc.getTimeListForTableUse();
		locationTable.setItems(rdc.getLocationListForTableUse());
		locationListForTablePrintUse = rdc.getLocationListForTableUse();
		costElementTable.setItems(rdc.getCostElementListForTableUse());
		costElementListForTablePrintUse = rdc.getCostElementListForTableUse();
		teamMemberTable.setItems(rdc.getTeamMemberListForTableUse());
		teamMemberListForTablePrintUse = rdc.getTeamMemberListForTableUse();
		
		apTimeChart.getChildren().clear();
		apLocationChart.getChildren().clear();
		apCostElementChart.getChildren().clear();
		apTeamMemberChart.getChildren().clear();
		apTimeChart.getChildren().add(rdc.getTimeLineChart());
		apLocationChart.getChildren().add(rdc.getLocationBarChart());
		apCostElementChart.getChildren().add(rdc.getCostElementBarChart());
		apTeamMemberChart.getChildren().add(rdc.getTeamMemberBarChart());
		
        for (Series<String,Number> serie: rdc.getTeamMemberBarChart().getData()){
            for (XYChart.Data<String, Number> item: serie.getData()){
            	item.getNode().setOnMouseEntered(event -> {
            		apReportCentre.setCursor(Cursor.HAND);
            		double ave = rdc.getTotalInTeamMember() / rdc.getTeamMemberListForTableUse().size();
                	double exceed = (item.getYValue().doubleValue() - ave) / ave * 100;
                	memberInComparison.setText("User: " + item.getXValue() 
                		+ "   Amount: " + String.format("%.2f", item.getYValue())
                		+ "   Team Ave: " + String.format("%.2f", ave)
                		+ "   Exceed Pct: " + String.format("%.2f", exceed) + "%");
            	});
            	item.getNode().setOnMouseExited(event -> {
            		apReportCentre.setCursor(Cursor.DEFAULT);
            		memberInComparison.setText("");
                });
            }
        }
		
		lbTotalInTime.setText(String.valueOf(rdc.getTotalInTime()));
		lbAveInTime.setText(String.valueOf(rdc.getAveInTime()));
		lbMaxInTime.setText(String.valueOf(rdc.getMaxInTime()));
		lbMinInTime.setText(String.valueOf(rdc.getMinInTime()));
		
		lbAmountInLocation1.setText(rdc.getAmountInLocation1());
		lbPlaceInLocation1.setText(rdc.getPlaceInLocation1());
		lbAmountInLocation2.setText(rdc.getAmountInLocation2());
		lbPlaceInLocation2.setText(rdc.getPlaceInLocation2());
		lbAmountInLocation3.setText(rdc.getAmountInLocation3());
		lbPlaceInLocation3.setText(rdc.getPlaceInLocation3());
		lbAmountInCostElement1.setText(rdc.getAmountInCostElement1());
		lbPlaceInCostElement1.setText(rdc.getPlaceInCostElement1());
		lbAmountInCostElement2.setText(rdc.getAmountInCostElement2());
		lbPlaceInCostElement2.setText(rdc.getPlaceInCostElement2());
		lbAmountInCostElement3.setText(rdc.getAmountInCostElement3());
		lbPlaceInCostElement3.setText(rdc.getPlaceInCostElement3());
		lbAmountInTeamMember1.setText(rdc.getAmountInTeamMember1());
		lbNameInTeamMember1.setText(rdc.getNameInTeamMember1());
		lbAmountInTeamMember2.setText(rdc.getAmountInTeamMember2());
		lbNameInTeamMember2.setText(rdc.getNameInTeamMember2());
		lbAmountInTeamMember3.setText(rdc.getAmountInTeamMember3());
		lbNameInTeamMember3.setText(rdc.getNameInTeamMember3());
		
		// temp method
		if (totalOrPerson.getSelectionModel().getSelectedItem().equals("Person")) {
			apSummary.getChildren().remove(apReportMember);
		}
	}
	
	/**
     * It is called when the user want to generate PDF version of the report. The PDF has two pages.
     * The first page is the screenshot of the report. The second page is the combination of the tables.
     */
	@FXML private void generatePDF() throws IOException {
		boolean total;
		if (totalOrPerson.getSelectionModel().getSelectedItem().equals("Total")) total = true;
		else total = false;
		
		BufferedImage chart1 = getPrintTableImage(timeListForTablePrintUse, "Time");
		BufferedImage chart2 = getPrintTableImage(locationListForTablePrintUse, "Location");
		BufferedImage chart3 = getPrintTableImage(costElementListForTablePrintUse, "Element");
		BufferedImage chart4 = getPrintTableImage(teamMemberListForTablePrintUse, "Member");
		
		PDFGenerator pdfGenerator = new PDFGenerator(total, apReportGeneralInfo.snapshot(null, null),
				 apReportTime.snapshot(null, null), apTimeChart.snapshot(null, null),
				 apReportLocation.snapshot(null, null), apLocationChart.snapshot(null, null),
				 apReportElement.snapshot(null, null), apCostElementChart.snapshot(null, null),
				 apReportMember.snapshot(null, null), apTeamMemberChart.snapshot(null, null),
				 chart1, chart2, chart3, chart4);
		pdfGenerator.generatePDF();
		
		generatePDF.setVisible(false);
		successPDFInfo.setVisible(true);
	}
	/**
	 * This method is used to generate image of table.
	 * 
	 * Because if the table contains items more than one screen can show, in the software it will become
     * scroll mode automatically. However the screenshot cannot hold all the items.
     * The method is: for each table, create a pane that is longer enough to hole all the items. Then make
     * a screenshot of it. Once it finished, set that pane invisible, repeat. It is very fast that the user 
     * cannot observe the process.
     * Once done it, all the two pages are PNG file, and the apache PDFbox is used to convert the PNG into
     * PDF format. The default path of file is the Desktop.
	 * 
	 * @param list the data of table
	 * @param type the title of table
	 * @return full version of table image
	 */
	@SuppressWarnings("unchecked")
	private BufferedImage getPrintTableImage(ObservableList<Recorder> list, String type) {
		 TableView<Recorder> printTable = new TableView<Recorder>();
		 
		 TableColumn<Recorder, String> nameCol = new TableColumn<Recorder, String>(type);
		 TableColumn<Recorder, String> amountCol = new TableColumn<Recorder, String>("Amount");
		 printTable.getColumns().addAll(nameCol, amountCol);
		 printTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		 
		 nameCol.setCellValueFactory(new PropertyValueFactory<>("place"));	
		 amountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
		 
		 int hight = (list.size()) * 25 + 50;
		 printTable.setPrefSize(500, hight);
		 printTable.setItems(list);
		 
		 apForTablePrintUse.getChildren().clear();
		 apForTablePrintUse.getChildren().add(printTable);
		 WritableImage writableImage = new WritableImage(1000, 800);
		 apForTablePrintUse.setVisible(true);
		 writableImage = apForTablePrintUse.snapshot(null, null);
		 apForTablePrintUse.setVisible(false);
		 
		 return SwingFXUtils.fromFXImage(writableImage, null);
	}
	
	/**
     * Used to connect to the database and get all the data belongs to current user.
     */
	private void extractData() {
		DataConnector dataConnector = new DataConnector();
		usedFileList = dataConnector.toGetExcelFileList();
		userClaims = dataConnector.toGetUserClaims();
	}
	
	/**
	 * It is used to transfer Receipt to ReceiptForAnalysis, when one type of time is selected.
	 * 
	 * @param timeOptionIndex
	 * @return ArrayList that can be passed to ReportDataCalculator to analyse
	 */
	private ArrayList<ReceiptForAnalysis> setUpClaimsForAnalysis(int timeOptionIndex) {
		ArrayList<Receipt> wbsClaims = new ArrayList<Receipt>();		
		ArrayList<Receipt> personClaims = new ArrayList<Receipt>();
		ArrayList<Receipt> currentClaims;

		for (Receipt r : userClaims) {
			if (r.getWbsNumber().equals(wbsNumberOptions.getSelectionModel().getSelectedItem())) {
				wbsClaims.add(r);
			}
		}
		
		if (totalOrPerson.getSelectionModel().getSelectedItem().equals("Person")) {
			for (Receipt r: wbsClaims) {
				if (String.valueOf(r.getEmployeeID()).equals(personOptions.getSelectionModel().getSelectedItem())) {
					personClaims.add(r);
				}
			}
			currentClaims = new ArrayList<Receipt>(personClaims);
		}
		else {
			currentClaims = new ArrayList<Receipt>(wbsClaims);
		}
		
		ArrayList<ReceiptForAnalysis> userClaimsForAnalysis = new ArrayList<ReceiptForAnalysis>();
		
		for (Receipt r : currentClaims) {
			ReceiptForAnalysis rfa;
			if (timeOptionIndex == 0) {
				rfa = new ReceiptForAnalysis(r.getWbsNumber(), r.getAmountInPound(), 
						r.getCostElement(), r.getLocation(), r.getExpenseDate(), r.getEmployeeID(), r.getWeekNumber());	
			}
			else {
				 rfa = new ReceiptForAnalysis(r.getWbsNumber(), r.getAmountInPound(), 
						r.getCostElement(), r.getLocation(), r.getTripEndDate(), r.getEmployeeID(), 0);
			}
			userClaimsForAnalysis.add(rfa);
		}
		return userClaimsForAnalysis;
	}
}
