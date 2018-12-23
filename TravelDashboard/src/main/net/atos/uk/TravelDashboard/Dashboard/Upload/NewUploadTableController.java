package main.net.atos.uk.TravelDashboard.Dashboard.Upload;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;

/**
 * This class is the controller class of the new upload page. It will first load the table
 * which contains all the data read from the given Excel file. After confirmed, the data will
 * be uploaded into the database. If not confirmed, nothing will happen.
 * 
 * The software use multi-thread of JavaFx to handle the data reading and uploading process,
 * because the time needed is quite long, and can easily crash the software if multi-thread is not used. 
 * 
 * NOTICE that the maximum of file rows is 10 thousands. The software can be easily crashed due
 * to the apache POI limitation, and has the possibility to damage the uploading file.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class NewUploadTableController implements Initializable {

	/**
     * The uploading Excel file.
     */
	public static File excelFile;
	/**
     * The file type.
     */
	public static int excelFileType;
	
	/**
     * Base anchor pane of new upload page.
     */
	@FXML private AnchorPane apPage;
	
	/**
     * Anchor pane that contains the table.
     */
	@FXML private AnchorPane apFileData;
	
	/**
     * Dynamic progress bar when handling the data reading and uploading process.
     */
	@FXML private ProgressBar progressBar;
	
	/**
     * Label to show the information if upload is successful or not.
     */
	@FXML private Label successLabel;
	
	/**
     * Button to confirm the table content then upload data to the database.
     */
	@FXML private Button confirmButton;
	
	/**
     * Used to read Excel file.
     */
	private TableLoader tl;
	
	/**
     * Table which contains all the data read from the given Excel file.
     */
	private TableView<Receipt> tv;
		
	/**
     * Used to upload the data to the database.
     */
	private ClaimsUploader cu;
	
	/**
     * Initialise the process of reading Excel file. Multi-thread is used here.
     * The reading process is done in the background.
     */
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		tl = new TableLoader(excelFile, excelFileType);
		tl.setOnSucceeded(event -> {
			tv = tl.getValue();
			confirmButton.setVisible(true);
			progressBar.setVisible(false);
			apFileData.getChildren().add(tv);});
		tl.setOnFailed(event -> {
			progressBar.setVisible(false);
			successLabel.setText("Sorry, failed. Try the following solutions:\n"
					+ "Separate the file manually. (Maximum 10,000 lines)\n"
					+ "Double check you selected the correct file type.\n"
					+ "Copy the content to a new file manually and try it again.");
			successLabel.setVisible(true);});
		
		new Thread(tl).start();
		newTimeLine();
	}
	
	/**
     * Used when Confirm button is clicked. Multi-thread is used here.
     * The uploading process is done in the background.
     */
	@FXML private void confirmUploading() {
		tv.setVisible(false);
		confirmButton.setVisible(false);
		progressBar.setVisible(true);
		
		cu = new ClaimsUploader(excelFile, tl.claims);
		cu.setOnSucceeded(event -> {
			System.out.println("Success!");
			progressBar.setVisible(false);
			successLabel.setVisible(true);
		});
		
		new Thread(cu).start();
		newTimeLine();
	}
	
	/**
     * Used to generate a time line for the progress bar. It will repeat many times until
     * the success/fail sign is sent back by the thread.
     */
	private void newTimeLine() {
		IntegerProperty seconds = new SimpleIntegerProperty();
		progressBar.progressProperty().bind(seconds.divide(100.0));
		
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(seconds, 0)),
				new KeyFrame(Duration.seconds(6), new KeyValue(seconds, 100))   
				);
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
	}
}
