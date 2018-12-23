package main.net.atos.uk.TravelDashboard.Login;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * This class is the controller class of the sign up page.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class SignupController implements Initializable  {
	
	/**
     * Base anchor pane.
     */
	@FXML private AnchorPane apSignup;
	/**
     * The circle is used to load the Atos image.
     */
	@FXML private Circle logo;
	/**
     * Text field to input username.
     */
    @FXML private TextField inputUsername;
    /**
     * Text field to input password.
     */
	@FXML private PasswordField inputPassword;
	/**
     * Text field to input password again.
     */
	@FXML private PasswordField inputPasswordAgain;
	/**
     * Text field to input email.
     */
	@FXML private TextField emailAddress;
	/**
     * Text field to input register code: default 'Atos17', change it in SiguupAuthorization.
     */
	@FXML private PasswordField registerCode;
	/**
     * Sign up button.
     */
	@FXML private Button signupButton;
	/**
     * Label to login page.
     */
	@FXML private Label changePage;
	
	/**
     * Load the Atos logo.
     */
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		Image img = new Image("atos.png");
		logo.setFill(new ImagePattern(img));
	}
	
	/**
     * Press Enter to trigger login action, the same as the login button.
     * 
     * @param event the key press event
     */
	@FXML private void handleKeyAction(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			handleSignup();
	    }
	}
	
	/**
     * Main check method. Go to the login page if input register code correct and username available 
     * in the database, otherwise blocked, alerted with a dialog box.
     */
	@FXML private void handleSignup() {
		SignupAuthorization sa = new SignupAuthorization();
		Authorization az = new Authorization();
		try {
			
			az = sa.userInputValid(inputUsername.getText(), inputPassword.getText(), inputPasswordAgain.getText(), 
					emailAddress.getText(), registerCode.getText());
			if(az.isValid()) {
				
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle(az.getInformation());
		        alert.setHeaderText(az.getInformation());
		        alert.showAndWait();
		        
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Login/LoginLayout.fxml"));
	            BorderPane loginOverview = (BorderPane) loader.load();

	            loginOverview.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Login/loginSystem.css");
	            Main.rootLayout.setCenter(loginOverview);
			}
			else {
				Alert alert = new Alert(AlertType.WARNING);
		        alert.setTitle(az.getInformation());
		        alert.setHeaderText(az.getInformation());
		        alert.showAndWait();
			}
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	/**
     * Go to login page if the label is clicked.
     */
	@FXML void toLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Login/LoginLayout.fxml"));
			BorderPane signupOverview = (BorderPane) loader.load();
    
			signupOverview.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Login/loginSystem.css");
			Main.rootLayout.setCenter(signupOverview);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
}