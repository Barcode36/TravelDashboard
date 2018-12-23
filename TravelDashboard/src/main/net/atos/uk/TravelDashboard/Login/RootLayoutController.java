package main.net.atos.uk.TravelDashboard.Login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class is the controller of root layout. It is used to display the about information.
 * 
 * @author  Niall Garratt
 * @since   2017-04-8
 * @version 1.0
*/

public class RootLayoutController {
	
	/**
	 * About clicked to show the software information.
	 */
	@FXML private void aboutInfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("About the software");
		alert.setContentText("University of Nottingham - G52GRP16-17 G19\n\nVersion 1.0\n");
		alert.showAndWait();
		alert.setOnCloseRequest(event -> {alert.close();});
	}
}
