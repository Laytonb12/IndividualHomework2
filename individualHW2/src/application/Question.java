package application;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Question {
    private int id;
    private String title;
    private String description;
    private Date timestamp;
    private String status; // "open", "resolved"
    private int studentId;
    private List<Answer> answers;

    public Question(int id, String title, String description, int studentId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = new Date();
        this.status = "open";
        this.studentId = studentId;
        this.answers = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public int getStudentId() { return studentId; }
    public List<Answer> getAnswers() { return answers; }
    
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    
    // CRUD Operations
    public void addAnswer(Answer answer) {
        answers.add(answer);
    }
    
    public void removeAnswer(int answerId) { //removes answer 
        answers.removeIf(answer -> answer.getId() == answerId);
    }
    //updates answer
    public void updateAnswer(int answerId, String newText) {
        for (Answer answer : answers) {
            if (answer.getId() == answerId) {
                answer.setText(newText);
            }
        }
    }
    //gets the answer
    public Answer getAnswer(int answerId) {
        for (Answer answer : answers) {
            if (answer.getId() == answerId) {
                return answer;
            }
        }
        return null;
    }
}
