package databasePart1;

import java.sql.*;

/**
 * The QuestionAnswerDatabaseHelper class is responsible for managing the database operations
 * for questions and answers, using the same structured approach as DatabaseHelper.
 */
public class QuestionAnswerDatabaseHelper {

    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

    // Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 

    private Connection connection = null;

    public QuestionAnswerDatabaseHelper() {
        try {
            connectToDatabase(); // Ensure connection is established and tables are created
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 // Insert a new answer
    public void insertAnswer(int questionId, String text, String reviewStatus, int studentId) throws SQLException {
        String query = "INSERT INTO answers (question_id, text, review_status, student_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, questionId);
            pstmt.setString(2, text);
            pstmt.setString(3, reviewStatus);
            pstmt.setInt(4, studentId);
            pstmt.executeUpdate();
        }
    }

    // Fetch all answers for a specific question
    public ResultSet getAnswersForQuestion(int questionId) throws SQLException {
        String query = "SELECT * FROM answers WHERE question_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, questionId);
        return pstmt.executeQuery();
    }

    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER); // Load the JDBC driver
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            createTables();  // Ensure tables exist
            checkTables();   // Debugging step to confirm table creation
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create the questions table
            String questionsTable = "CREATE TABLE IF NOT EXISTS questions ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255) NOT NULL, "
                    + "description TEXT NOT NULL, "
                    + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "status VARCHAR(20), "
                    + "student_id INT NOT NULL)";
            stmt.execute(questionsTable);

            // Create the answers table
            String answersTable = "CREATE TABLE IF NOT EXISTS answers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "question_id INT NOT NULL, "
                    + "text TEXT NOT NULL, "
                    + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "review_status VARCHAR(20), "
                    + "student_id INT NOT NULL, "
                    + "FOREIGN KEY (question_id) REFERENCES questions(id))";
            stmt.execute(answersTable);
        }
    }

    // Debugging method to confirm table existence
    public void checkTables() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
            System.out.println("Existing tables in the database:");
            while (rs.next()) {
                System.out.println(" - " + rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a new question
    public void insertQuestion(String title, String description, String status, int studentId) throws SQLException {
        String query = "INSERT INTO questions (title, description, status, student_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, status);
            pstmt.setInt(4, studentId);
            pstmt.executeUpdate();
        }
    }

    // Fetch all questions
    public ResultSet getAllQuestions() throws SQLException {
        String query = "SELECT * FROM questions";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    // Close connection
    public void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
