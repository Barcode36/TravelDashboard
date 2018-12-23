package main.net.atos.uk.TravelDashboard.Login;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * This class is the controller class of the login page.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class LoginController implements Initializable { 
	
	/**
     * Base anchor pane.
     */
	@FXML private AnchorPane apLogin;
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
     * Login button.
     */
	@FXML private Button loginButton;
	/**
     * Label to sign up page.
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
			handleLogin();
	    }
	}
	
	/**
     * Main check method. Load the main Dashboard if input information matched in the database,
     * otherwise blocked, alerted with a dialog box.
     */
	@FXML private void handleLogin() {
		LoginAuthorization la = new LoginAuthorization();

		try {

			if(la.userInfoCorrect(inputUsername.getText(), inputPassword.getText())) {
				
	            FXMLLoader loader1 = new FXMLLoader();
	            loader1.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Dashboard/Main/DashboardRoot.fxml"));
	            BorderPane dashboardRoot = (BorderPane) loader1.load();
	            dashboardRoot.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Dashboard/Main/DashboardRoot.css");
	            Main.rootLayout.setCenter(dashboardRoot);

	            FXMLLoader loader2 = new FXMLLoader();
	            loader2.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Dashboard/Main/MainDashboard.fxml"));
	            AnchorPane mainDashboard = (AnchorPane) loader2.load();
	            mainDashboard.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Dashboard/Main/MainDashboard.css");
	            dashboardRoot.setCenter(mainDashboard);
	            
			}
			else {
				Alert alert = new Alert(AlertType.WARNING);
		        alert.setTitle("Wrong username/password!");
		        alert.setHeaderText("Wrong username/password!");
		        alert.showAndWait();
			}
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	/**
     * Go to sign up page if the label is clicked.
     */
	@FXML void toSignup() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Login/SignupLayout.fxml"));
			BorderPane signupOverview = (BorderPane) loader.load();
			
			signupOverview.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Login/loginSystem.css");
			Main.rootLayout.setCenter(signupOverview);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
}
