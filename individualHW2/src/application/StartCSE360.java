package application;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/**
 * Entry point for the CSE360 JavaFX application.
 * Runs automated manual tests before launching the application window.
 */
public class StartCSE360 extends Application {

    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    public static void main(String[] args) {
        // Run automated tests before launching the GUI
        ManualTestRunner.main(null);

        // Launch JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            databaseHelper.connectToDatabase(); // Connect to the database
            if (databaseHelper.isDatabaseEmpty()) {
                new FirstPage(databaseHelper).show(primaryStage);
            } else {
                new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
