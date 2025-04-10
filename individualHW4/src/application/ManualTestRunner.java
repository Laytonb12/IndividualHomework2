package application;

import databasePart1.DatabaseHelper;
import java.sql.SQLException;

/**
 * ManualTestRunner is a standalone test runner for performing automated tests
 * on selected classes within the project.
 * <p>
 * This file contains basic functional tests for the User, Answer, DatabaseHelper,
 * and Staff announcement and question/answer management functionalities.
 * </p>
 */
public class ManualTestRunner {

    public static void main(String[] args) {
        System.out.println("=== Running Manual Tests ===");

        testUser();
        testAnswer();
        testDatabaseHelper();
        testAnnouncementManager(); // NEW
        testQuestionListOperations(); // NEW
        testAnswerListOperations(); // NEW

        System.out.println("=== Tests Finished ===");
    }

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

    // === NEW TEST CASES FOR STAFF FEATURES ===

    /**
     * Test Case 6:
     * Verifies that announcements can be added and retrieved using AnnouncementManager.
     */
    public static void testAnnouncementManager() {
        System.out.print("Test Announcement Manager: ");
        try {
            AnnouncementManager.addAnnouncement("Test Announcement 1");
            AnnouncementManager.addAnnouncement("Test Announcement 2");

            boolean hasFirst = AnnouncementManager.getAnnouncements().contains("Test Announcement 1");
            boolean hasSecond = AnnouncementManager.getAnnouncements().contains("Test Announcement 2");

            if (hasFirst && hasSecond) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED (announcement missing)");
            }
        } catch (Exception e) {
            System.out.println("FAILED with exception: " + e.getMessage());
        }
    }

    /**
     * Test Case 7:
     * Simulates adding and removing Questions from a list like in the Staff ViewQuestionsPage.
     */
    public static void testQuestionListOperations() {
        System.out.print("Test Question List Operations: ");
        try {
            Question q1 = new Question(1, "Title1", "Description1");
            Question q2 = new Question(2, "Title2", "Description2");

            java.util.List<Question> questions = new java.util.ArrayList<>();
            questions.add(q1);
            questions.add(q2);

            // Remove one
            questions.remove(q1);

            if (questions.size() == 1 && questions.get(0).equals(q2)) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED (list operations incorrect)");
            }
        } catch (Exception e) {
            System.out.println("FAILED with exception: " + e.getMessage());
        }
    }

    /**
     * Test Case 8:
     * Simulates adding and removing Answers from a list like in the Staff ViewAnswerPage.
     */
    public static void testAnswerListOperations() {
        System.out.print("Test Answer List Operations: ");
        try {
            Answer a1 = new Answer(1, "Answer1");
            Answer a2 = new Answer(2, "Answer2");

            java.util.List<Answer> answers = new java.util.ArrayList<>();
            answers.add(a1);
            answers.add(a2);

            // Remove one
            answers.remove(a1);

            if (answers.size() == 1 && answers.get(0).equals(a2)) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED (list operations incorrect)");
            }
        } catch (Exception e) {
            System.out.println("FAILED with exception: " + e.getMessage());
        }
    }
}
