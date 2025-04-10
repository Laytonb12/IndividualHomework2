package application;

import java.util.Date;

public class Answer {
    private int id; 
    private int questionId;
    private String text;
    private Date timestamp;
    private String reviewStatus; // "pending", "approved", "rejected"
    private int studentId;

    // Full constructor (for real answers)
    public Answer(int id, int questionId, String text, int studentId) {
        this.id = id;
        this.questionId = questionId;
        this.text = text;
        this.timestamp = new Date();
        this.reviewStatus = "pending";
        this.studentId = studentId;
    }

    // NEW simpler constructor (for dummy answers)
    public Answer(int id, String text) {
        this.id = id;
        this.questionId = -1; // dummy value
        this.text = text;
        this.timestamp = new Date();
        this.reviewStatus = "pending";
        this.studentId = -1; // dummy value
    }

    // Getters
    public int getId() { return id; }
    public int getQuestionId() { return questionId; }
    public String getText() { return text; }
    public Date getTimestamp() { return timestamp; }
    public String getReviewStatus() { return reviewStatus; }
    public int getStudentId() { return studentId; }
    
    // Setters
    public void setText(String text) { this.text = text; }
    public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }
}
