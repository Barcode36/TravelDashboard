package main.net.atos.uk.TravelDashboard.ClaimItem;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class is used to hold each of the Excel file information inside the software, essentially:
 * excelFileId (ID used in database side, primary key of the Excel file table), Excel file name
 * and the upload time.
 * 
 * It is used in:
 * upload process,
 * main Dashboard to display the last four records of upload,
 * upload page to display all of the records of upload.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class ExcelFile {
	private int excelFileId;
	private SimpleStringProperty excelFileName;
	private SimpleStringProperty excelFileUploadTime;
	
	public ExcelFile(int excelFileId, String excelFileName,  String excelFileUploadTime) {
		this.excelFileId = excelFileId;
		this.excelFileName = new SimpleStringProperty(excelFileName);
		this.excelFileUploadTime = new SimpleStringProperty(excelFileUploadTime);
	}

	public String getExcelFileUploadTime() {
		return excelFileUploadTime.get();
	}

	public String getExcelFileName() {
		return excelFileName.get();
	}

	public int getExcelFileId() {
		return excelFileId;
	}

}
