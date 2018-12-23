package main.net.atos.uk.TravelDashboard.Dashboard.Upload;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;
import main.net.atos.uk.TravelDashboard.ClaimItem.ExcelFile;
import main.net.atos.uk.TravelDashboard.Database.DataConnector;

/**
 * This class is used to upload data to the database.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class ClaimsUploader extends Task<Boolean> {
	
	/**
     * Return the boolean value of the whole upload process. True represents success.
     */
	private boolean ifSuccess = true;
	
	/**
     * The uploading Excel file. Used here to get the Excel file name, as a record of upload.
     */
	private File excelFile;
	/**
     * The list contains all the data read from the Excel file.
     */
	private ObservableList<Receipt> claims;
	
	/**
     * This is the method that need to be processed by the thread. Refer to JavaFx concurrency.
     * 
     * The method first insert a record to Excel File table in the database side, then get the
     * Excel file ID (database side), and use it to upload all the data to Claim table in the database.
     */
	@Override
	protected Boolean call() throws Exception {
		DataConnector dataConnector = new DataConnector();
		
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date d = new Date();
		ExcelFile ef = new ExcelFile(0, excelFile.getName(), df.format(d));
		if (!dataConnector.toUploadExcelInfo(ef)) {
			ifSuccess = false;
			return ifSuccess;
		}
		
		int fileID = dataConnector.getLatestFileID();
		
		if (!dataConnector.toUploadClaims(fileID, claims)) {
			ifSuccess = false;
		}
		return ifSuccess;
	}
	
	/**
     * Constructor, receive the excel file and file type.
     * 
     * @param claims all the data read from the Excel file
     * @param excelFile the uploading Excel file 
     */
	public ClaimsUploader(File excelFile, ObservableList<Receipt> claims) {
		this.claims = claims;
		this.excelFile = excelFile;
	}
}
