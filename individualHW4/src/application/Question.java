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

    // ORIGINAL constructor (for real questions)
    public Question(int id, String title, String description, int studentId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = new Date();
        this.status = "open";
        this.studentId = studentId;
        this.answers = new ArrayList<>();
    }

    // NEW constructor (for dummy questions without studentId)
    public Question(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = new Date();
        this.status = "open";
        this.studentId = -1; // Dummy value since not needed for staff viewing
        this.answers = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public int getStudentId() { return studentId; }
    public List<Answer> getAnswers() { return answers; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    
    // CRUD Operations
    public void addAnswer(Answer answer) {
        answers.add(answer);
    }
    
    public void removeAnswer(int answerId) {
        answers.removeIf(answer -> answer.getId() == answerId);
    }
    
    public void updateAnswer(int answerId, String newText) {
        for (Answer answer : answers) {
            if (answer.getId() == answerId) {
                answer.setText(newText);
            }
        }
    }
    
    public Answer getAnswer(int answerId) {
        for (Answer answer : answers) {
            if (answer.getId() == answerId) {
                return answer;
            }
        }
        return null;
    }
}
