package test.net.atos.uk.TravelDashboard.Dashboard.Upload;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import javafx.collections.ObservableList;
import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;
import main.net.atos.uk.TravelDashboard.Dashboard.Upload.FileReader;

public class FileReaderTest {
	FileReader fr;
	
	@Test public void returnDataList_test_return_correct_array() {
		FileReader fr = new FileReader(new File("/Users/apple/Desktop/uk/GRP/G52GRP_TEAM19_2016_Beliebers/SampleData/test.xlsx"), 1);
		try {
			fr.handleData();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObservableList<Receipt> testList = fr.returnDataList();
		
		assertEquals("Week number", 39, testList.get(0).getWeekNumber());
		assertEquals(5, testList.get(1).getAmountInPound(), 0);
		assertEquals("Location", "Barnwood", testList.get(2).getLocation());
		assertEquals("Expense date", "07.09.2016", testList.get(3).getExpenseDate());
	}
}
