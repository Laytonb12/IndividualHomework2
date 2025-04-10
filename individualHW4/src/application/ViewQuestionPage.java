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

public class ViewQuestionPage {

    private DatabaseHelper databaseHelper;
    private String userName;
    private ObservableList<Question> questions;

    public ViewQuestionPage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
        // Create TableView
        TableView<Question> table = new TableView<>();

        // Columns
        TableColumn<Question, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Question, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Question, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.getColumns().addAll(idColumn, titleColumn, descriptionColumn);

        // Dummy data
        questions = FXCollections.observableArrayList(
            new Question(1, "How to reset password?", "I'm locked out, how do I reset it?"),
            new Question(2, "Where is Assignment 2?", "I can't find Assignment 2 on the portal."),
            new Question(3, "Error compiling project", "I keep getting a Java compile error. Help?")
        );

        table.setItems(questions);

        // Buttons
        Button deleteButton = new Button("Delete Selected Question");
        deleteButton.setOnAction(e -> {
            Question selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                questions.remove(selected);
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
        primaryStage.setTitle("View Question - Staff");
    }
}
