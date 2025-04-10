package application;

import databasePart1.DatabaseHelper;
import databasePart1.QuestionAnswerDatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The QuestionPage class provides a user interface for managing questions.
 */
public class QuestionPage {

    private final QuestionAnswerDatabaseHelper questionDbHelper;
    private final DatabaseHelper databaseHelper;

    private static final int MIN_TITLE_LENGTH = 5;
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MIN_DESCRIPTION_LENGTH = 10;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    public QuestionPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        this.questionDbHelper = new QuestionAnswerDatabaseHelper();
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label pageTitle = new Label("Manage Questions");
        pageTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // these are the input fields for question, title, and description
        TextField titleField = new TextField();
        titleField.setPromptText("Enter question title");
        titleField.setMaxWidth(300);

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Enter question description");
        descriptionField.setMaxWidth(300);

        ChoiceBox<String> statusChoice = new ChoiceBox<>();
        statusChoice.getItems().addAll("Open", "Resolved");
        
        Button addButton = new Button("Add Question");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        ListView<String> questionListView = new ListView<>();
        loadQuestions(questionListView);

        // this is the label for displaying the selected question's description
        TextArea questionDescription = new TextArea();
        questionDescription.setEditable(false);
        questionDescription.setPromptText("Select a question to view its description");
        questionDescription.setMaxWidth(400);

        // this will update description field once a question is selected
        questionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                questionDescription.setText(getDescriptionByTitle(newValue));
            }
        });

        // adds a Button action to add a new question
        addButton.setOnAction(event -> {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            String status = statusChoice.getValue();

            if (title.isEmpty() || description.isEmpty() || status == null) {
                messageLabel.setText("All fields must be filled!");
                return;
            }

            if (title.length() < MIN_TITLE_LENGTH || title.length() > MAX_TITLE_LENGTH) {
                messageLabel.setText("Title must be between " + MIN_TITLE_LENGTH + " and " + MAX_TITLE_LENGTH + " characters.");
                return;
            }

            if (description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH) {
                messageLabel.setText("Description must be between " + MIN_DESCRIPTION_LENGTH + " and " + MAX_DESCRIPTION_LENGTH + " characters.");
                return;
            }
            
            try {
                questionDbHelper.insertQuestion(title, description, status, 1); // Assuming student_id = 1 for now
                messageLabel.setText("Question added successfully!");
                loadQuestions(questionListView);
                titleField.clear();
                descriptionField.clear();
            } catch (SQLException e) {
                messageLabel.setText("Database Error: " + e.getMessage());
            }
        });

        // Button to view answers related to a selected question
        Button viewAnswersButton = new Button("View Answers");
        viewAnswersButton.setOnAction(a -> {
            String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
            if (selectedQuestion == null) {
                messageLabel.setText("Please select a question first!");
                return;
            }
            
            int questionId = getQuestionIdByTitle(selectedQuestion);
            new AnswerPage(databaseHelper, questionId).show(primaryStage);
        });

        Button returnHome = new Button("Return Home");
        returnHome.setOnAction(a -> new UserHomePage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(pageTitle, titleField, descriptionField, statusChoice, addButton, viewAnswersButton, messageLabel, questionListView, questionDescription, returnHome);
        
        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Question Management");
        primaryStage.show();
    }
    
    // this loads all of the questions into the ListView
    private void loadQuestions(ListView<String> listView) {
        listView.getItems().clear();
        try {
            var rs = questionDbHelper.getAllQuestions();
            while (rs.next()) {
                listView.getItems().add(rs.getString("title"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading questions: " + e.getMessage());
        }
    }
    
    // Retrieves the question ID based on the title
    private int getQuestionIdByTitle(String title) {
        try {
            var rs = questionDbHelper.getAllQuestions();
            while (rs.next()) {
                if (rs.getString("title").equals(title)) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching question ID: " + e.getMessage());
        }
        return -1;
    }

    // Retrieves the question description based on the title
    private String getDescriptionByTitle(String title) {
        try {
            var rs = questionDbHelper.getAllQuestions();
            while (rs.next()) {
                if (rs.getString("title").equals(title)) {
                    return rs.getString("description");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching question description: " + e.getMessage());
        }
        return "Description not found.";
    }
}
