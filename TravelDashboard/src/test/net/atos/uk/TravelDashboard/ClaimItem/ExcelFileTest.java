package test.net.atos.uk.TravelDashboard.ClaimItem;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import main.net.atos.uk.TravelDashboard.ClaimItem.ExcelFile;

public class ExcelFileTest {
	ExcelFile ef;
	
	@Test public void isValid_test_should_return_upload_time() {
		ef = new ExcelFile(01, "File1.xlsx", "2017-01-04 14:24:00");
		assertEquals("Return upload time", "2017-01-04 14:24:00", ef.getExcelFileUploadTime());
	}
	
	@Test public void getInformation_test_should_return_file_name() {
		ef = new ExcelFile(01, "File1.xlsx", "2017-01-04 14:24:00");
		assertEquals("Return file name", "File1.xlsx", ef.getExcelFileName());
	}
	
	@Test public void getInformation_test_should_return_file_id() {
		ef = new ExcelFile(01, "File1.xlsx", "2017-01-04 14:24:00");
		assertEquals("Return file ID", 01, ef.getExcelFileId());
	}
}
