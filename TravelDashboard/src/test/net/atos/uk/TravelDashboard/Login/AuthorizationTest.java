package test.net.atos.uk.TravelDashboard.Login;

import static org.junit.Assert.*;
import org.junit.Test;

import main.net.atos.uk.TravelDashboard.Login.Authorization;

public class AuthorizationTest {
	Authorization az = new Authorization();
	
	@Test public void isValid_test_should_return_defalut_false() {
		assertFalse("Default value is false", az.isValid());
	}
	
	@Test public void getInformation_test_should_return_defalut_empty() {
		assertNotNull("Default value is not null", az.getInformation());
		assertEquals("Default value is empty", "", az.getInformation());
	}
	
	@Test public void setValid_test_should_set_valid_true() {
		az.setValid(true);
		assertTrue("Set valid value to be true", az.isValid());
	}
	
	@Test public void setInformation_test_should_change_information() {
		az.setInformation("Looks good.");
		assertEquals("Change information", "Looks good.", az.getInformation());
	}

}
