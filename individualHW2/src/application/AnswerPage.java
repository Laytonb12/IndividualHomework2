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
 * The AnswerPage class allows users to add and view answers for a selected question.
 */
public class AnswerPage {

    private final QuestionAnswerDatabaseHelper dbHelper;
    private final DatabaseHelper databaseHelper;
    private final int questionId;

    private static final int MIN_ANSWER_LENGTH = 5;
    private static final int MAX_ANSWER_LENGTH = 500;

    public AnswerPage(DatabaseHelper databaseHelper, int questionId) {
        this.databaseHelper = databaseHelper;
        this.dbHelper = new QuestionAnswerDatabaseHelper();
        this.questionId = questionId;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label pageTitle = new Label("Answers for Question ID: " + questionId);
        pageTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // uses ListView to display answers on screen
        ListView<String> answerListView = new ListView<>();
        loadAnswers(answerListView);

        TextArea answerField = new TextArea();
        answerField.setPromptText("Enter your answer");
        answerField.setMaxWidth(300);

        ChoiceBox<String> reviewStatusChoice = new ChoiceBox<>();
        reviewStatusChoice.getItems().addAll("Pending", "Reviewed");
        
        Button addAnswerButton = new Button("Submit Answer");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // this segment will take care of creating answers
        addAnswerButton.setOnAction(event -> {
            String answerText = answerField.getText().trim();
            String reviewStatus = reviewStatusChoice.getValue();

            if (answerText.isEmpty() || reviewStatus == null) {
                messageLabel.setText("All fields must be filled!");
                return;
            }

            if (answerText.length() < MIN_ANSWER_LENGTH) {
                messageLabel.setText("Answer must be at least " + MIN_ANSWER_LENGTH + " characters long.");
                return;
            }

            if (answerText.length() > MAX_ANSWER_LENGTH) {
                messageLabel.setText("Answer cannot exceed " + MAX_ANSWER_LENGTH + " characters.");
                return;
            }
            
            try {
                dbHelper.insertAnswer(questionId, answerText, reviewStatus, 1); // Assuming student_id = 1
                messageLabel.setText("Answer submitted successfully!");
                loadAnswers(answerListView);
                answerField.clear(); // Clear the input field after submission
            } catch (SQLException e) {
                messageLabel.setText("Database Error: " + e.getMessage());
            }
        });
        
        Button returnToQuestions = new Button("Back to Questions");
        returnToQuestions.setOnAction(a -> new QuestionPage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(pageTitle, answerListView, answerField, reviewStatusChoice, addAnswerButton, messageLabel, returnToQuestions);
        
        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Answer Management");
        primaryStage.show();
    }

    // this will load the answers for a specific question
    private void loadAnswers(ListView<String> listView) {
        listView.getItems().clear();
        try {
            ResultSet rs = dbHelper.getAnswersForQuestion(questionId);
            while (rs.next()) {
                listView.getItems().add("Answer: " + rs.getString("text") + " (Status: " + rs.getString("review_status") + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error loading answers: " + e.getMessage());
        }
    }
}
