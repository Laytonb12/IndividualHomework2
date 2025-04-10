package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import databasePart1.DatabaseHelper;

public class StaffHomePage {
    private DatabaseHelper databaseHelper;
    private String userName;

    public StaffHomePage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
        // Title
        Label titleLabel = new Label("Staff Home Page");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Create Buttons
        Button viewQuestionsButton = new Button("View Questions");
        Button viewAnswersButton = new Button("View Answers");
        Button postAnnouncementButton = new Button("Post Announcement");
        Button viewAnnouncementsButton = new Button("View Announcements"); // NEW button
        Button homeButton = new Button("Return Home");
        Button logoutButton = new Button("Logout");

        // Button Actions
        viewQuestionsButton.setOnAction(e -> {
            new ViewQuestionPage(databaseHelper, userName).show(primaryStage);
        });

        viewAnswersButton.setOnAction(e -> {
            new ViewAnswerPage(databaseHelper, userName).show(primaryStage);
        });

        postAnnouncementButton.setOnAction(e -> {
            new PostAnnouncementPage(databaseHelper, userName).show(primaryStage);
        });
        
        viewAnnouncementsButton.setOnAction(e -> {
            new ViewAnnouncementsPage(databaseHelper, userName).show(primaryStage);
        });

        homeButton.setOnAction(e -> {
            new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });

        logoutButton.setOnAction(e -> {
            databaseHelper.closeConnection();
            primaryStage.close();
        });

        // Layout
        VBox layout = new VBox(20); // 20px spacing between elements
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30;");
        layout.getChildren().addAll(
            titleLabel,
            viewQuestionsButton,
            viewAnswersButton,
            postAnnouncementButton,
            viewAnnouncementsButton, // Add new button into layout
            homeButton,
            logoutButton
        );

        // Scene
        Scene scene = new Scene(layout, 400, 550);

        primaryStage.setTitle("Staff Home - Welcome " + userName);
        primaryStage.setScene(scene);
    }
}
