package main.net.atos.uk.TravelDashboard.Dashboard.Upload;

import java.io.File;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;

/**
 * This class is used to get ObservableList from FileReader, and put the data into
 * the table, which will be shown to the user.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class TableLoader extends Task<TableView<Receipt>> {
	
	/**
     * The list contains all the data read from the Excel file.
     */
	public ObservableList<Receipt> claims;
	
	/**
     * The uploading Excel file.
     */
	private File excelFile;
	/**
     * The file type.
     */
	private int excelFileType;
	
	/**
     * This is the method that need to be processed by the thread. Refer to JavaFx concurrency.
     */
	@Override
	protected TableView<Receipt> call() throws Exception {
		//System.out.println("Read file start");
		claims = getDataList();
		//System.out.println("Read file end");
		return getTable();
	}
	
	/**
     * Constructor, receive the excel file and file type.
     * 
     * @param excelFile the uploading Excel file 
     * @param excelFileType the file type
     */
	public TableLoader(File excelFile, int excelFileType) {
		this.excelFile = excelFile;
		this.excelFileType = excelFileType;
	}
	
	/**
     * Used to initialise the table.
     */
	@SuppressWarnings("unchecked")
	private TableView<Receipt> getTable() {
		
		TableView<Receipt> fileDataTable = new TableView<Receipt>();
		fileDataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		fileDataTable.setPrefSize(1020, 620);
		
		TableColumn<Receipt, String> wbsIndexCol = new TableColumn<Receipt, String>("WBS Index");
		TableColumn<Receipt, Double> amountCol = new TableColumn<Receipt, Double>("Amount");
        TableColumn<Receipt, String> costElementCol = new TableColumn<Receipt, String>("Cost Element");
        TableColumn<Receipt, String> locationCol = new TableColumn<Receipt, String>("Location");
        TableColumn<Receipt, String> expenseDateCol = new TableColumn<Receipt, String>("Expense Date");
        TableColumn<Receipt, String> endDateCol = new TableColumn<Receipt, String>("End Date");
        TableColumn<Receipt, Integer> employeeIDCol = new TableColumn<Receipt, Integer>("Employee ID");
        
        fileDataTable.getColumns().addAll(wbsIndexCol, amountCol, costElementCol,locationCol,
        		expenseDateCol, endDateCol, employeeIDCol);
        
        wbsIndexCol.setCellValueFactory(new PropertyValueFactory<>("wbsNumber"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountInPound"));
        costElementCol.setCellValueFactory(new PropertyValueFactory<>("costElement"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        expenseDateCol.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("tripEndDate"));
        employeeIDCol.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        
        fileDataTable.setItems(claims);

        return fileDataTable;
	}
	
	/**
     * Use the FileReader to get the list which contains all the data read from the Excel file.
     */
	private ObservableList<Receipt> getDataList() {
		FileReader fr = new FileReader(excelFile, excelFileType);
		try {
			fr.handleData();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fr.returnDataList();
	}
}
