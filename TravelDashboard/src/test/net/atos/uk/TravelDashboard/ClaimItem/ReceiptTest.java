package test.net.atos.uk.TravelDashboard.ClaimItem;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.net.atos.uk.TravelDashboard.ClaimItem.Receipt;

public class ReceiptTest {
	Receipt r;
	
	@Test public void getWbsNumber_test_return_WbsNumber() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals("Return WBS number", "WBS123", r.getWbsNumber());
	}
	
	@Test public void getAmountInPound_test_return_amount() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals(12.5, r.getAmountInPound(), 0.001);
	}
	
	@Test public void getCostElement_test_return_cost_element() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals("Return cost element", "Hotel", r.getCostElement());
	}
	
	@Test public void getLocation_test_return_location() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals("Return location", "London", r.getLocation());
	}
	
	@Test public void getExpenseDate_test_return_expense_date() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals("Return expense date", "2017-04-08", r.getExpenseDate());
	}
	
	@Test public void getTripEndDate_test_return_trip_end_date() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals("Return trip end date", "2017-04-12", r.getTripEndDate());
	}
	
	@Test public void getEmployeeID_return_employee_id() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals("Return employee id", 11233, r.getEmployeeID());
	}
	
	@Test public void getWeekNumber_return_week_number() {
		r = new Receipt("WBS123", 12.5, "Hotel", "London", "2017-04-08", "2017-04-12", 11233, 22);
		assertEquals("Return week number", 22, r.getWeekNumber());
	}
}
