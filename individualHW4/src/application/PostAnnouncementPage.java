package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import databasePart1.DatabaseHelper;

public class PostAnnouncementPage {

    private DatabaseHelper databaseHelper;
    private String userName;

    public PostAnnouncementPage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
        Label instructionLabel = new Label("Enter your announcement:");
        TextArea announcementField = new TextArea();
        announcementField.setWrapText(true);

        Button submitButton = new Button("Submit Announcement");
        submitButton.setOnAction(e -> {
            String announcement = announcementField.getText().trim();
            if (!announcement.isEmpty()) {
                AnnouncementManager.addAnnouncement(announcement); // <-- save it to manager
                System.out.println("New Announcement Posted by Staff: " + announcement);
                new StaffHomePage(databaseHelper, userName).show(primaryStage);
            } else {
                System.out.println("Announcement cannot be empty!");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new StaffHomePage(databaseHelper, userName).show(primaryStage);
        });

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30;");
        layout.getChildren().addAll(instructionLabel, announcementField, submitButton, backButton);

        Scene scene = new Scene(layout, 500, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Post Announcement - Staff");
    }
}
