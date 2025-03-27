package application;

import databasePart1.DatabaseHelper;
import java.sql.SQLException;

/**
 * ManualTestRunner is a standalone test runner for performing automated tests
 * on selected classes within the project.
 * <p>
 * This file contains basic functional tests for the User, Answer, and DatabaseHelper classes.
 * It is part of Individual Homework 3 for demonstrating automated testing and Javadoc documentation.
 * </p>
 */
public class ManualTestRunner {

    /**
     * The main method that runs all automated test cases in sequence.
     *
     * @param args standard command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Running Manual Tests ===");

        testUser();
        testAnswer();
        testDatabaseHelper();

        System.out.println("=== Tests Finished ===");
    }

    /**
     * Test Case 1: Verifies that a User object can be created
     * with valid data and that its getter methods return the expected values.
     */
    public static void testUser() {
        System.out.print("Test User: ");
        try {
            User user = new User("jdoe", "password123", "student");
            if ("jdoe".equals(user.getUserName()) && "student".equals(user.getRole())) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED (unexpected field values)");
            }
        } catch (Exception e) {
            System.out.println("FAILED with exception: " + e.getMessage());
        }
    }

    /**
     * Test Case 2, 3, and 4:
     * Verifies that an Answer object is constructed correctly,
     * and that its text and review status can be updated using setter methods.
     */
    public static void testAnswer() {
        System.out.print("Test Answer: ");
        try {
            Answer answer = new Answer(1, 100, "42", 123);

            boolean idCorrect = answer.getId() == 1;
            boolean questionIdCorrect = answer.getQuestionId() == 100;
            boolean textCorrect = "42".equals(answer.getText());
            boolean studentIdCorrect = answer.getStudentId() == 123;
            boolean statusCorrect = "pending".equals(answer.getReviewStatus());
            boolean timestampExists = answer.getTimestamp() != null;

            // Update fields and verify updates
            answer.setText("Updated Answer");
            answer.setReviewStatus("approved");

            boolean updatedText = "Updated Answer".equals(answer.getText());
            boolean updatedStatus = "approved".equals(answer.getReviewStatus());

            if (idCorrect && questionIdCorrect && textCorrect && studentIdCorrect &&
                statusCorrect && timestampExists && updatedText && updatedStatus) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED (one or more checks failed)");
            }
        } catch (Exception e) {
            System.out.println("FAILED with exception: " + e.getMessage());
        }
    }

    /**
     * Test Case 5:
     * Verifies that the DatabaseHelper class can connect to the database
     * without throwing a SQLException or runtime error.
     */
    public static void testDatabaseHelper() {
        System.out.print("Test Database Connection: ");
        try {
            DatabaseHelper db = new DatabaseHelper();
            db.connectToDatabase();
            System.out.println("PASSED");
        } catch (SQLException e) {
            System.out.println("FAILED with SQLException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("FAILED with exception: " + e.getMessage());
        }
    }
}
