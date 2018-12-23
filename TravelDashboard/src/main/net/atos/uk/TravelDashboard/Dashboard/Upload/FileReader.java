package main.net.atos.uk.TravelDashboard.Dashboard.Upload;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;

/**
 * This class is used to read Excel file. Apache POI is the library that provides the API.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class FileReader {
	/**
	 * Represents: WBS, pounds, coseEle, location, expenseDate, tripEndDate, employeeID.
	 * To add new type of file. The column number must be added here.
	 */
	private int[][] allTypesColumnIndex = {{1, 5, 11, 16, 0, 16, 25}, {43, 7, 29, 15, 0, 19, 8}}; 
	
	/**
	 * The list will be returned if the process is successfully completed.
	 */
	private ObservableList<Receipt> dataListInTable = FXCollections.observableArrayList();
	
	/**
	 * The row is used to deal with single row of Excel file.
	 */
	private XSSFRow row;
	
	
	/**
     * The uploading Excel file.
     */
	private File newExcelFile;
	/**
     * The file type.
     */
	private int excelFileType;
	
	/**
	 * Used to get correct needed column index according to file type.
	 */
	private int[] colunmIndex;
	
	/**
     * Constructor, receive the excel file and file type.
     * Make sure the colunmIndex as type selected.
     * 
     * @param newExcelFile the uploading Excel file 
     * @param excelFileType the file type
     */
	public FileReader(File newExcelFile, int excelFileType) {
		this.newExcelFile = newExcelFile;
		this.excelFileType = excelFileType;
		colunmIndex = getColumnIndex(excelFileType);
	}
	
	/**
	 * Used to get correct needed column index according to file type.
	 * 
	 * @param excelFileType the file type
	 * @return columns index of specified file type
	 */
	private int[] getColumnIndex(int excelFileType) {
		int[] colunmIndex = new int[7];
		
		// temp
		for (int i = 0; i < 7; i++) {
			colunmIndex[i] = allTypesColumnIndex[excelFileType][i];
		}
		return colunmIndex;
	}

	/**
	 * Used to read the Excel file. Notice the first row is skipped because it contains attributes name.
	 */
	@SuppressWarnings("resource")
	public void handleData() throws InvalidFormatException, IOException {

		// Here, the limitation of 10 thousands line data, is because of the XSSFWorkbook parser
		OPCPackage pkg= OPCPackage.open(newExcelFile);
		XSSFWorkbook workbook = new XSSFWorkbook(pkg);
		
		XSSFSheet spreadsheet = workbook.getSheetAt(0);
		
		if (newExcelFile != null) {
			try {
				Iterator<Row> rowIterator = spreadsheet.iterator();
				 
				// skip the first row which are attributes name
				row = (XSSFRow) rowIterator.next();
				
				while (rowIterator.hasNext()) {
					row = (XSSFRow) rowIterator.next();
					 
					String wbsIndex;
					double amount;
					String element;
					String location;
					String expenseDate;
					String endDate;
					int employeeID;
					int weekNumber;
					
					try {
						if (toGetStringCellValue(row.getCell(colunmIndex[2])).contains("Internal")) continue;
						
						wbsIndex = toGetStringCellValue(row.getCell(colunmIndex[0]));
						amount = toGetDoubleCellValue(row.getCell(colunmIndex[1]));
						element = toGetStringCellValue(row.getCell(colunmIndex[2]));
						if (excelFileType == 0) {
							location = dealwithType1SpecialLocation(toGetStringCellValue(row.getCell(colunmIndex[3])));
							expenseDate = dealwithSpecialExpenseDate(toGetSpecialExpenseCellValue(row.getCell(colunmIndex[4])));
							endDate = dealwithType1SpecialEndDate(toGetStringCellValue(row.getCell(colunmIndex[5])));
						}
						else {
							location = toGetStringCellValue(row.getCell(colunmIndex[3]));
							expenseDate = dealwithSpecialExpenseDate(toGetSpecialExpenseCellValue(row.getCell(colunmIndex[4])));
							endDate = toGetStringCellValue(row.getCell(colunmIndex[5])); 
						}
						employeeID = toGetIntCellValue(row.getCell(colunmIndex[6]));
						 
						Receipt rt;
						if (excelFileType == 0) {
							rt = new Receipt(wbsIndex, amount, element, 
									location, expenseDate, endDate, employeeID, 0); 
						}
						else {
							weekNumber = toGetIntCellValue(row.getCell(3));
							rt = new Receipt(wbsIndex, amount, element, 
									location, expenseDate, endDate, employeeID, weekNumber);
						}
						 
						dataListInTable.add(rt);
					} catch (IllegalStateException e) {
						continue;
					}
				}
			 } finally {
				 pkg.close();
			}
	    }	
	}
	
	public ObservableList<Receipt> returnDataList() {
		return dataListInTable;
	}
	
	/**
	 * Used to get the value if it is expected to be double cell. NUMERIC type is double value by default.
	 * Please refer to Apache POI.
	 * 
	 * Apache POI has not provide appropriate API by the time the code is written, even though 
	 * the currently used method is deprecated.
	 * 
	 * @param cell the cell in Excel file
	 * @return cell value in double
	 */
	@SuppressWarnings("deprecation")
	private double toGetDoubleCellValue(XSSFCell cell) {
		double value;
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            value = cell.getNumericCellValue();
        } else {
        	value = 0;
        }
		return value;
	}
	
	/**
	 * Used to get the value if it is expected to be int cell. NUMERIC type is double value by default,
	 * so the method transfer double to int.
	 * Please refer to Apache POI.
	 * 
	 * Apache POI has not provide appropriate API by the time the code is written, even though 
	 * the currently used method is deprecated.
	 * 
	 * @param cell the cell in Excel file
	 * @return cell value in integer
	 */
	@SuppressWarnings("deprecation")
	private int toGetIntCellValue(XSSFCell cell) {
		int value;
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            value = (int) cell.getNumericCellValue();
        } else {
        	value = 0;
        }
		return value;
	}
	
	/**
	 * Used to get the value if it is expected to be string cell. STRING type is string value by default,
	 * Please refer to Apache POI.
	 * 
	 * Apache POI has not provide appropriate API by the time the code is written, even though 
	 * the currently used method is deprecated.
	 * 
	 * @param cell the cell in Excel file
	 * @return cell value in string
	 */
	@SuppressWarnings("deprecation")
	private String toGetStringCellValue(XSSFCell cell) {
		String value;
	    
	    
		if (cell.getCellTypeEnum() == CellType.STRING) {
            value = cell.getStringCellValue();
        } else {
        	value = "null";
        	System.out.println("This is null!");
        }
		return value;
	}
	
	/**
	 * For some reason, in the two given file by Atos, the Expense Date column is read as NUMERIC type.
	 * However, STRING type is expected. The method is used to deal with this special case.
	 * 
	 * @param cell the cell in Excel file
	 * @return cell value in string
	 */
	private String toGetSpecialExpenseCellValue(XSSFCell cell) {	
		DataFormatter df = new DataFormatter();
		String value = df.formatCellValue(cell);
		
	    return value;
	}

	/**
	 * The method is used to deal with the special case in the first given file by Atos. That is:
	 * Trip start date, trip end date and location are all in the same column.
	 * 
	 * @param str the special column which contains trip start date, trip end date and location
	 * @return cell value in string
	 */
	private String dealwithType1SpecialLocation(String str) {
		String location;
		if (str.length() > 37 && str.substring(0, 5).equals("*Trip")) {
			location = str.substring(38);
		}
		else {
			location = "";
		}
		return location;
	}
	
	/**
	 * The method is used to deal with the special case in the first given file by Atos. That is:
	 * Trip start date, trip end date and location are all in the same column.
	 * 
	 * @param str the special column which contains trip start date, trip end date and location
	 * @return cell value in string
	 */
	private String dealwithType1SpecialEndDate(String str) {
		String endDate;
		if (str.length() >= 32 && str.substring(0, 5).equals("*Trip")) {
			endDate = str.substring(24, 30) + "20" + str.substring(30, 32);
		}
		else {
			endDate = "";
		}
		return endDate;
	}
	
	/**
	 * This method is used to deal with the special case mentioned in toGetSpecialExpenseCellValue().
	 * It takes a string and convert it into a format that is used in the database to store the date,
	 * so that the format is consistent when used.
	 * 
	 * @param str given string contains date in a default format from Java DataFormatter
	 * @return cell value in string
	 */
	private String dealwithSpecialExpenseDate(String str) {		
		String month = str.substring(0, str.indexOf('/'));
		if (month.length() < 2) month = "0" + month;
		String day = str.substring(str.indexOf('/') + 1, str.indexOf('/', str.indexOf('/') + 1));
		if (day.length() < 2) day = "0" + day;
		String year = "20" + str.substring(str.length()-2);

		return day + '.' + month + '.' + year;
	}
}
