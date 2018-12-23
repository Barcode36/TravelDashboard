package main.net.atos.uk.TravelDashboard.Dashboard.Main;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.net.atos.uk.TravelDashboard.Database.CurrentUserInfo;
import main.net.atos.uk.TravelDashboard.Login.Main;

/**
 * This class is the controller class of the root layout of the Dashboard. Essentially it contains
 * the menu bar which specifies four options: Main, Upload, Report and Exit.
 * 
 * The menu bar will always appear except the user log out. This is the reason that Root layout and Main
 * layout are separated.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class DashboardRootController {
	
	/**
     * The anchor pane which contains the menu bar.
     */
	@FXML private AnchorPane apMenu;
	/**
     * Main button.
     */
	@FXML private Button mainInMenu;
	/**
     * Upload button.
     */
	@FXML private Button uploadInMenu;
	/**
     * Report button.
     */
	@FXML private Button reportInMenu;
	/**
     * Exit button.
     */
	@FXML private Button exitInMenu;
	
	
	/**
     * Handle the request when Main is clicked.
     */
	@FXML private void handleMainDashboard() {
		try {
			 FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Dashboard/Main/MainDashboard.fxml"));
	            AnchorPane mainDashboard = (AnchorPane) loader.load();
	            mainDashboard.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Dashboard/Main/MainDashboard.css");
	            BorderPane dashboardRoot = (BorderPane) Main.rootLayout.getCenter();
	            dashboardRoot.setCenter(mainDashboard);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	/**
     * Handle the request when Upload is clicked.
     */
	@FXML private void handleUploadCentre() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Dashboard/Upload/UploadCentre.fxml"));
			BorderPane uploadCentre = (BorderPane) loader.load();
			
			uploadCentre.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Dashboard/Upload/UploadCentre.css");
			BorderPane dashboardRoot = (BorderPane) Main.rootLayout.getCenter();
			dashboardRoot.setCenter(uploadCentre);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	/**
     * Handle the request when Upload is clicked.
     */
	@FXML private void handleReportCentre() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Dashboard/Report/ReportCentre.fxml"));
			BorderPane reportCentre = (BorderPane) loader.load();
			
			reportCentre.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Dashboard/Report/ReportCentre.css");
			BorderPane dashboardRoot = (BorderPane) Main.rootLayout.getCenter();
			dashboardRoot.setCenter(reportCentre);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	/**
     * Handle the request when Exit is clicked. To log out, set the static class CurrentUserInfo
     * information to default null and 0.
     */
	@FXML private void handleLogout() {
    	CurrentUserInfo.setUsername(null);
    	CurrentUserInfo.setUserID(0);
    	CurrentUserInfo.setUserEmail(null);
    	
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
