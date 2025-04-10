package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

public class ViewAnswerPage {

    private DatabaseHelper databaseHelper;
    private String userName;
    private ObservableList<Answer> answers;

    public ViewAnswerPage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
        // Create TableView
        TableView<Answer> table = new TableView<>();

        // Columns
        TableColumn<Answer, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Answer, String> textColumn = new TableColumn<>("Answer Text");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        table.getColumns().addAll(idColumn, textColumn);

        // Dummy data
        answers = FXCollections.observableArrayList(
            new Answer(1, "You can reset your password by clicking 'Forgot Password' on the login page."),
            new Answer(2, "Assignment 2 is located under the 'Assignments' tab."),
            new Answer(3, "Check your syntax around your main method for missing brackets.")
        );

        table.setItems(answers);

        // Buttons
        Button deleteButton = new Button("Delete Selected Answer");
        deleteButton.setOnAction(e -> {
            Answer selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                answers.remove(selected);
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new StaffHomePage(databaseHelper, userName).show(primaryStage);
        });

        // Layout
        VBox layout = new VBox(20);
        layout.getChildren().addAll(table, deleteButton, backButton);

        Scene scene = new Scene(layout, 600, 500);

        primaryStage.setScene(scene);
        primaryStage.setTitle("View Answers - Staff");
    }
}
