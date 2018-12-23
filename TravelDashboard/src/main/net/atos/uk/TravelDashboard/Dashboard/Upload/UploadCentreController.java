package main.net.atos.uk.TravelDashboard.Dashboard.Upload;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.net.atos.uk.TravelDashboard.ClaimItem.ExcelFile;
import main.net.atos.uk.TravelDashboard.Database.DataConnector;
import main.net.atos.uk.TravelDashboard.Login.Main;

/**
 * This class is the controller class of the upload page. It provide an API for 
 * the user to upload the Excel file data into the database, for analysis.
 * 
 * The user need to choose the correct file type, then upload the file to the system.
 * The system will provide a table of to-be-uploaded data. Once confirmed, the data will
 * be uploaded into the database.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class UploadCentreController implements Initializable {
	
	/**
     * Base anchor pane of upload centre.
     */
	@FXML private AnchorPane apUploadCentre;
	
	/**
     * ComboBox of file type, currently two.
     */
	@FXML private ComboBox<String> fileType;
	
	/**
     * Button to upload file, only enable when one file type is chosen.
     */
	@FXML private Button uploadFile;

	/**
     * Button to remove file, only visible when one row is chosen.
     */	
	@FXML private Button removeButton;
	
	/**
     * Table to show all upload records.
     */	
	@FXML private TableView<ExcelFile> excelFileTable;
	@FXML private TableColumn<ExcelFile, String> excelFileName;
	@FXML private TableColumn<ExcelFile, String> excelFileTableUploadTime;
	
	/**
     * Used to make sure the upload records are in the correct (sequential) order.
     */	
	SortedList<ExcelFile> excelFileList;
	
	/**
     * Initialise the the ComboBox and table of upload page. 
     */
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		ObservableList<String> fileTypeOptions = FXCollections.observableArrayList(
    			"File type 1 (Sample Data.xlsx)", "File type 2 (Travel Data Sept Oct.xlsx)");
		fileType.setItems(fileTypeOptions);
		
		fileType.setOnAction(event -> {
			NewUploadTableController.excelFileType = fileType.getSelectionModel().getSelectedIndex(); 
			uploadFile.setVisible(true);
		});
		
		excelFileName.setCellValueFactory(new PropertyValueFactory<>("excelFileName"));
		excelFileTableUploadTime.setCellValueFactory(new PropertyValueFactory<>("excelFileUploadTime"));
		
		DataConnector dataConnector = new DataConnector();
		excelFileList = dataConnector.toGetExcelFileList();
		excelFileTable.setItems(excelFileList);
		
		excelFileTable.getSelectionModel().selectedItemProperty().addListener(event -> {
			ExcelFile ef = excelFileTable.getSelectionModel().getSelectedItem();
			if (ef != null) {
				removeButton.setVisible(true);
			}
			else {
				removeButton.setVisible(false);
			}
		});
		excelFileList.comparatorProperty().bind(excelFileTable.comparatorProperty());
	}
	
	/**
     * The method is called when one file type is selected and the user click Upload Excel button.
     * If a "xlsx" file is chosen, then go to the NewUploadTable page. 
     */
	@FXML private void handleUploadExcel() {
		FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx");
		FileChooser filechooser = new FileChooser();
		filechooser.getExtensionFilters().add(imageFilter);
		File newExcelFile = filechooser.showOpenDialog(apUploadCentre.getScene().getWindow());
		
		if (newExcelFile != null) {
			NewUploadTableController.excelFile = newExcelFile;
			changeToNewUploadTable();
		}
	}
	
	/**
     * The method is called when the user want to delete all the data of one uploaded file. When confirmed,
     * the corresponding data will be deleted in the database side. Both ExcelFile and Claim table in database
     * side will be updated.
     */
	@FXML private void handleRemoveFile() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Remove");
		alert.setHeaderText("You are trying to remove the record");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			ExcelFile ef = excelFileTable.getSelectionModel().getSelectedItem();
			
			DataConnector dataConnector = new DataConnector();
			dataConnector.toDeleteExcelInfo(ef);
			
			excelFileList = dataConnector.toGetExcelFileList();
			excelFileTable.setItems(excelFileList);
		} else {}
	}
	
	/**
     * The method is used to direct the user to NewUploadTable page.
     */
	private void changeToNewUploadTable() {
		try {
			 FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Dashboard/Upload/NewUploadTable.fxml"));
	            BorderPane newUploadTable = (BorderPane) loader.load();
	            newUploadTable.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Dashboard/Upload/NewUploadTable.css");
	            BorderPane dashboardRoot = (BorderPane) Main.rootLayout.getCenter();
	            dashboardRoot.setCenter(newUploadTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}