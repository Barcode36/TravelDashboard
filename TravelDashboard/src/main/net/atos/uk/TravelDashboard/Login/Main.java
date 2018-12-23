package main.net.atos.uk.TravelDashboard.Login;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This class is the main class of Travel Dashboard. It loads the root layout (contains the menu bar), then it
 * automatically loads the login page.
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class Main extends Application {
    public static Stage primaryStage;
    public static BorderPane rootLayout;
    
	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;
        Main.primaryStage.setTitle("Travel Dashboard");

        initRootLayout();
        showLoginOverview();
	}
	
	/**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {

        	// Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Login/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the login page on the root layout.
     */
    public void showLoginOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/main/net/atos/uk/TravelDashboard/Login/LoginLayout.fxml"));
            BorderPane loginOverview = (BorderPane) loader.load();
            
            loginOverview.getStylesheets().add("/main/net/atos/uk/TravelDashboard/Login/loginSystem.css");
            rootLayout.setCenter(loginOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * The main method.
     */
	public static void main(String[] args) {
		launch(args);
	}
}