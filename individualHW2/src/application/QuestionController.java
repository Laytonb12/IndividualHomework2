package application;

import databasePart1.QuestionAnswerDatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionController {
    
    private TextField titleField;
    private TextArea descriptionField;
    private ChoiceBox<String> statusChoice;
    private ListView<String> questionListView;
    
    private QuestionAnswerDatabaseHelper dbHelper = new QuestionAnswerDatabaseHelper();
    private ObservableList<String> questions = FXCollections.observableArrayList();
    
    public QuestionController(TextField titleField, TextArea descriptionField, ChoiceBox<String> statusChoice, ListView<String> questionListView) {
        this.titleField = titleField;
        this.descriptionField = descriptionField;
        this.statusChoice = statusChoice;
        this.questionListView = questionListView;
        
        statusChoice.setItems(FXCollections.observableArrayList("Open", "Resolved"));
        try {
            dbHelper.connectToDatabase();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to connect to database: " + e.getMessage());
        }
        loadQuestions();
    }
    
    public void addQuestion(ActionEvent event) {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String status = statusChoice.getValue();
        
        if (title.isEmpty() || description.isEmpty() || status == null) {
            showAlert("Error", "All fields must be filled!");
            return;
        }
        
        try {
            dbHelper.insertQuestion(title, description, status, 1); // Assuming student_id = 1 for now
            showAlert("Success", "Question added successfully!");
            loadQuestions();
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }
    
    private void loadQuestions() {
        questions.clear();
        try {
            ResultSet rs = dbHelper.getAllQuestions();
            while (rs.next()) {
                questions.add(rs.getString("title"));
            }
            questionListView.setItems(questions);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load questions!");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
