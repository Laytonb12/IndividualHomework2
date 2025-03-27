package application;

import databasePart1.DatabaseHelper;
import databasePart1.QuestionAnswerDatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user and allows access to question management.
 */
public class UserHomePage {

    private final DatabaseHelper databaseHelper;
    private final QuestionAnswerDatabaseHelper questionDbHelper;

    public UserHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        this.questionDbHelper = new QuestionAnswerDatabaseHelper();
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Label to display Hello user
        Label userLabel = new Label("Hello, User!");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button manageQuestionsButton = new Button("Manage Questions");
        manageQuestionsButton.setOnAction(a -> new QuestionPage(databaseHelper).show(primaryStage));
        
        Button returnHome = new Button("Return Home");
        returnHome.setOnAction(a -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(userLabel, manageQuestionsButton, returnHome);
        Scene userScene = new Scene(layout, 800, 400);

        // Set the scene to primary stage
        primaryStage.setScene(userScene);
        primaryStage.setTitle("User Page");
        primaryStage.show();
    }
}
