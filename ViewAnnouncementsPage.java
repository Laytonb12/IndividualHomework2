package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import javafx.geometry.Pos;

/**
 * ViewAnnouncementsPage displays all announcements posted by staff.
 *
 * This page shows:
 * - A ListView containing all announcements retrieved from AnnouncementManager
 * - A Back button to return to the StaffHomePage
 *
 * Announcements are shown in the order they were posted.
 *
 * This class is part of the Staff functionality for the Question and Answer System.
 */
public class ViewAnnouncementsPage {
    private DatabaseHelper databaseHelper;
    private String userName;

    public ViewAnnouncementsPage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
        ListView<String> announcementsList = new ListView<>();
        ObservableList<String> announcements = FXCollections.observableArrayList(AnnouncementManager.getAnnouncements());
        announcementsList.setItems(announcements);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new StaffHomePage(databaseHelper, userName).show(primaryStage);
        });

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30;");
        layout.getChildren().addAll(announcementsList, backButton);

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Announcements");
    }
}
