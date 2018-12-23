package test.net.atos.uk.TravelDashboard.Login;

import static org.junit.Assert.*;
import org.junit.Test;

import main.net.atos.uk.TravelDashboard.Login.LoginAuthorization;

public class LoginAuthorizationTest {
	LoginAuthorization la = new LoginAuthorization();
	
	@Test public void userInfoCorrect_test_find_correct() {	
		assertTrue("Return the user info correct", la.userInfoCorrect("test", "000000"));
	}
	
	@Test public void userInfoCorrect_test_wrong_password() {
		assertFalse("Return the user info wrong", la.userInfoCorrect("test", "1234"));
	}
	
	@Test public void userInfoCorrect_test_nonexistent_username() {
		assertFalse("Return the user info wrong", la.userInfoCorrect("jim123", "1234"));
	}
}
