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

public class ViewAnnouncementsPage {

    private DatabaseHelper databaseHelper;
    private String userName;

    public ViewAnnouncementsPage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
        // Create a ListView to show announcements
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
