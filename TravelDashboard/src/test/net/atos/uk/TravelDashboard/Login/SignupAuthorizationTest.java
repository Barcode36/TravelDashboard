package test.net.atos.uk.TravelDashboard.Login;

import static org.junit.Assert.*;

import org.junit.Test;

import main.net.atos.uk.TravelDashboard.Login.Authorization;
import main.net.atos.uk.TravelDashboard.Login.SignupAuthorization;

public class SignupAuthorizationTest {
	SignupAuthorization sa = new SignupAuthorization();
	Authorization azExpect = new Authorization();
	Authorization azActual = new Authorization();
	
	@Test public void userInputVaild_test_register_code_incorrect() {
		azExpect.setValid(false);
		azExpect.setInformation("Register code incorrect!");
		azActual = sa.userInputValid("test", "000000", "000000", "test@gmail.com", "aaabbb");
		assertFalse("Return authorization false", azActual.isValid());
		assertEquals("Return authorization details", azExpect.getInformation(), azActual.getInformation());
	}
	
	@Test public void userInputVaild_test_username_unavailable() {
		azExpect.setValid(false);
		azExpect.setInformation("Username not available!");
		azActual = sa.userInputValid("test", "000000", "000000", "test@gmail.com", "Atos17");
		assertFalse("Return authorization false", azActual.isValid());
		assertEquals("Return authorization details", azExpect.getInformation(), azActual.getInformation());
	}

	@Test public void userInputVaild_test_username_invalid_character() {
		azExpect.setValid(false);
		azExpect.setInformation("Username contains invalid character!");
		azActual = sa.userInputValid("test!@", "123456", "123456", "test@gmail.com", "Atos17");
		assertFalse("Return authorization false", azActual.isValid());
		assertEquals("Return authorization details", azExpect.getInformation(), azActual.getInformation());
	}
	
	@Test public void userInputVaild_test_password_not_match() {
		azExpect.setValid(false);
		azExpect.setInformation("Password not matched!");
		azActual = sa.userInputValid("wide", "123456", "654321", "test@gmail.com", "Atos17");
		assertFalse("Return authorization false", azActual.isValid());
		assertEquals("Return authorization details", azExpect.getInformation(), azActual.getInformation());
	}
	
	@Test public void userInputVaild_test_password_too_short() {
		azExpect.setValid(false);
		azExpect.setInformation("Password too short!");
		azActual = sa.userInputValid("jimmy", "002", "002", "test@gmail.com", "Atos17");
		assertFalse("Return authorization false", azActual.isValid());
		assertEquals("Return authorization details", azExpect.getInformation(), azActual.getInformation());
	}
	
	@Test public void userInputVaild_test_email_invalid() {
		azExpect.setValid(false);
		azExpect.setInformation("Email address invalid!");
		azActual = sa.userInputValid("jimmy", "002222", "002222", "test.gmail.com", "Atos17");
		assertFalse("Return authorization false", azActual.isValid());
		assertEquals("Return authorization details", azExpect.getInformation(), azActual.getInformation());
	}
	
	@Test public void userInputVaild_test_account_create_successfully() {
		azExpect.setValid(true);
		azExpect.setInformation("Account created successfully!");
		azActual = sa.userInputValid("tom", "000000", "000000", "test@gmail.com", "Atos17");
		assertTrue("Return authorization true", azActual.isValid());
		assertEquals("Return authorization details", azExpect.getInformation(), azActual.getInformation());
	}
	

}
