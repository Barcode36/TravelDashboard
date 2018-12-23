package test.net.atos.uk.TravelDashboard.Database;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.net.atos.uk.TravelDashboard.Database.CurrentUserInfo;

public class CurrentUserInfoTest {
	
	@Test public void getUserID_setUserID_test_ID_match() {
		CurrentUserInfo.setUserID(12);
		assertEquals("User ID should match", 12, CurrentUserInfo.getUserID());
	}
	
	@Test public void getUsername_setUsername_test_username_match() {
		CurrentUserInfo.setUsername("allean123");
		assertEquals("Username should match", "allean123", CurrentUserInfo.getUsername());
	}
	
	@Test public void getUserEmail_setUserEmail_test_email_match() {
		CurrentUserInfo.setUserEmail("allean123@gmail.com");
		assertEquals("Email should match", "allean123@gmail.com", CurrentUserInfo.getUserEmail());
	}
}
