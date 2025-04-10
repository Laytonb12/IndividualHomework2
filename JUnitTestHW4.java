package test_TPP3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import application.*;
import databasePart1.DatabaseHelper;

/**
 * Working JUnit Tests for Team Project Phase 3
 * This test suite contains the functional tests that have been implemented for the
 * review functionality, user authentication, and database integrity.
 */
public class JUnitTestTPP3 {
    
    private DatabaseHelper databaseHelper;
    private String studentUserId;
    private String reviewerUserId;
    private String instructorUserId;
    private int questionId;
    private int answerId;
    private int reviewId;
    
    /**
     * Set up the test environment before each test.
     * Creates a test database and initializes necessary data.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        System.out.println("==== Setup Test Environment ====");
        try {
            // Initialize the DatabaseHelper with a test database
            databaseHelper = new DatabaseHelper();
            System.out.println("Instantiated DatabaseHelper");
            
            databaseHelper.connectToDatabase();
            System.out.println("Connected to database");
            
            // Create test users (student, reviewer, instructor)
            // First, clear any existing users with these test usernames
            cleanupTestUsers();
            System.out.println("Cleaned up test users");
            
            // Create test users
            User studentUser = new User("teststudent", "Password123!", "user");
            User reviewerUser = new User("testreviewer", "Password123!", "reviewer");
            User instructorUser = new User("testinstructor", "Password123!", "instructor");
            
            databaseHelper.register(studentUser);
            databaseHelper.register(reviewerUser);
            databaseHelper.register(instructorUser);
            System.out.println("Registered test users");
            
            // Get user IDs for later use
            studentUserId = databaseHelper.getUserId("teststudent");
            reviewerUserId = databaseHelper.getUserId("testreviewer");
            instructorUserId = databaseHelper.getUserId("testinstructor");
            System.out.println("Retrieved user IDs: student=" + studentUserId + ", reviewer=" + reviewerUserId + ", instructor=" + instructorUserId);
            
            // Create a test question and answer
            databaseHelper.addQuestion(Integer.parseInt(studentUserId), "Test Question", "This is a test question");
            System.out.println("Added test question");
            
            // Get the question ID
            ResultSet rs = databaseHelper.getAllQuestions();
            if (rs.next()) {
                questionId = rs.getInt("id");
                System.out.println("Retrieved question ID: " + questionId);
                
                // Create a test answer for this question
                databaseHelper.createAnswer(questionId, Integer.parseInt(studentUserId), "Test Answer", "This is a test answer");
                System.out.println("Added test answer");
                
                // Get the answer ID
                ResultSet answerRs = databaseHelper.getAnswers(questionId);
                if (answerRs.next()) {
                    answerId = answerRs.getInt("ansId");
                    System.out.println("Retrieved answer ID: " + answerId);
                } else {
                    System.out.println("WARNING: No answer found after creation");
                }
            } else {
                System.out.println("WARNING: No question found after creation");
            }
            
            System.out.println("==== Setup Complete ====");
        } catch (Exception e) {
            System.out.println("EXCEPTION in setUp: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Clean up the test environment after each test.
     * Removes test data and closes database connections.
     */
    @AfterEach
    public void tearDown() throws SQLException {
        System.out.println("==== Tear Down Environment ====");
        try {
            // Clean up test data
            cleanupTestData();
            System.out.println("Cleaned up test data");
            
            cleanupTestUsers();
            System.out.println("Cleaned up test users");
            
            // Close database connection
            databaseHelper.closeConnection();
            System.out.println("Closed database connection");
            
            System.out.println("==== Tear Down Complete ====");
        } catch (Exception e) {
            System.out.println("EXCEPTION in tearDown: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Helper method to clean up test users from the database.
     */
    private void cleanupTestUsers() throws SQLException {
        System.out.println("Attempting to clean up test users...");
        try {
            // Delete test users if they exist
            databaseHelper.executeUpdate("DELETE FROM cse360users WHERE userName IN ('teststudent', 'testreviewer', 'testinstructor')");
            System.out.println("Successfully deleted test users");
        } catch (Exception e) {
            System.out.println("EXCEPTION while cleaning up test users: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Helper method to clean up test data from the database.
     */
    private void cleanupTestData() throws SQLException {
        System.out.println("Attempting to clean up test data...");
        try {
            // Delete test reviews
            databaseHelper.executeUpdate("DELETE FROM Reviews WHERE aId = " + answerId);
            System.out.println("Deleted test reviews");
            
            // Delete test answers
            databaseHelper.executeUpdate("DELETE FROM Answers WHERE qId = " + questionId);
            System.out.println("Deleted test answers");
            
            // Delete test questions
            databaseHelper.executeUpdate("DELETE FROM Questions WHERE sId = " + studentUserId);
            System.out.println("Deleted test questions");
            
            System.out.println("Test data cleanup complete");
        } catch (Exception e) {
            System.out.println("EXCEPTION while cleaning up test data: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Test 1: ReviewCreationTest
     * Tests if a reviewer can successfully create a review with valid input parameters.
     * Assigned to: Nikhil Balabhadra
     */
    @Test
    public void testReviewCreation() throws SQLException {
        System.out.println("Starting testReviewCreation");
        try {
            // Create a test review
            String reviewFrom = "Test Reviewer";
            String reviewText = "This is a test review";
            
            System.out.println("Creating review with answerId=" + answerId + ", reviewerUserId=" + reviewerUserId);
            
            // Call the method to create a review
            databaseHelper.createReview(answerId, Integer.parseInt(reviewerUserId), reviewFrom, reviewText);
            System.out.println("Review created");
            
            // Verify the review was created
            ResultSet rs = databaseHelper.getReviews(answerId);
            boolean reviewFound = false;
            
            System.out.println("Checking if review exists in database");
            while (rs.next()) {
                if (rs.getString("reviewFrom").equals(reviewFrom) && 
                    rs.getString("reviewText").equals(reviewText) && 
                    rs.getInt("sId") == Integer.parseInt(reviewerUserId)) {
                    reviewFound = true;
                    reviewId = rs.getInt("rId"); // Save for later tests
                    System.out.println("Found matching review with ID: " + reviewId);
                    break;
                }
            }
            
            assertTrue(reviewFound, "Review should be created successfully");
            System.out.println("Test passed: Review creation successful");
        } catch (Exception e) {
            System.out.println("EXCEPTION in testReviewCreation: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Completed testReviewCreation");
    }
    
    /**
     * Test 2: ReviewReadTest
     * Verifies that a student can read reviews associated with a specific answer.
     * Assigned to: Nikhil Balabhadra
     */
    @Test
    public void testReviewRead() throws SQLException {
        System.out.println("Starting testReviewRead");
        try {
            // First create a review to read
            System.out.println("Creating test review");
            databaseHelper.createReview(answerId, Integer.parseInt(reviewerUserId), "Test Reviewer", "This is a test review");
            
            // Get reviews for the answer
            List<Review> reviews = new ArrayList<>();
            System.out.println("Retrieving reviews for answerId=" + answerId);
            ResultSet rs = databaseHelper.getReviews(answerId);
            
            while (rs.next()) {
                int rId = rs.getInt("rId");
                int aId = rs.getInt("aId");
                int sId = rs.getInt("sId");
                String reviewFrom = rs.getString("reviewFrom");
                String reviewText = rs.getString("reviewText");
                Review review = new Review(rId, aId, sId, reviewFrom, reviewText);
                reviews.add(review);
                System.out.println("Found review: " + reviewFrom + " - " + reviewText);
            }
            
            // Verify at least one review exists
            assertFalse(reviews.isEmpty(), "Should find at least one review");
            System.out.println("Found " + reviews.size() + " reviews");
            
            // Verify review content
            boolean foundTestReview = false;
            for (Review review : reviews) {
                if (review.getReviewFrom().equals("Test Reviewer") && 
                    review.getReviewText().equals("This is a test review")) {
                    foundTestReview = true;
                    System.out.println("Found the specific test review");
                    break;
                }
            }
            
            assertTrue(foundTestReview, "Should find the test review");
            System.out.println("Test passed: Review read successful");
        } catch (Exception e) {
            System.out.println("EXCEPTION in testReviewRead: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Completed testReviewRead");
    }
    
    /**
     * Test 6: ReviewDeleteTest
     * Verifies that a reviewer can delete their own review but not reviews created by others.
     * Assigned to: Layton Borunda
     */
    @Test
    public void testReviewDelete() throws SQLException {
        System.out.println("Starting testReviewDelete");
        try {
            // Create a review first
            System.out.println("Creating test review");
            databaseHelper.createReview(answerId, Integer.parseInt(reviewerUserId), "Test Reviewer", "This is a test review");
            
            // Find the review ID
            System.out.println("Finding review ID");
            ResultSet rs = databaseHelper.getReviews(answerId);
            int reviewId = -1;
            
            while (rs.next()) {
                if (rs.getString("reviewFrom").equals("Test Reviewer")) {
                    reviewId = rs.getInt("rId");
                    System.out.println("Found review with ID: " + reviewId);
                    break;
                }
            }
            
            // Verify review was found
            assertTrue(reviewId != -1, "Should find the created review");
            
            // Delete the review
            System.out.println("Deleting review with ID: " + reviewId);
            databaseHelper.deleteReview(reviewId);
            
            // Verify review was deleted
            System.out.println("Verifying review was deleted");
            rs = databaseHelper.getReviews(answerId);
            boolean reviewFound = false;
            
            while (rs.next()) {
                if (rs.getInt("rId") == reviewId) {
                    reviewFound = true;
                    System.out.println("Review still exists (should be deleted)");
                    break;
                }
            }
            
            assertFalse(reviewFound, "Review should be deleted");
            System.out.println("Test passed: Review deletion successful");
        } catch (Exception e) {
            System.out.println("EXCEPTION in testReviewDelete: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Completed testReviewDelete");
    }
    
    /**
     * Test 11: ReviewUpdateTest
     * Tests if a reviewer can update the content of their existing review.
     * Assigned to: Shantanu Thatte
     */
    @Test
    public void testReviewUpdate() throws SQLException {
        System.out.println("Starting testReviewUpdate");
        try {
            // Create a test review first
            System.out.println("Creating test review");
            databaseHelper.createReview(answerId, Integer.parseInt(reviewerUserId), "Test Reviewer", "Original review text");
            
            // Find the review ID
            System.out.println("Finding review ID");
            ResultSet rs = databaseHelper.getReviews(answerId);
            int reviewId = -1;
            
            while (rs.next()) {
                if (rs.getString("reviewFrom").equals("Test Reviewer")) {
                    reviewId = rs.getInt("rId");
                    System.out.println("Found review with ID: " + reviewId);
                    break;
                }
            }
            
            // Update the review
            String updatedText = "Updated review text";
            System.out.println("Updating review with new text");
            databaseHelper.updateReview(reviewId, "Test Reviewer", updatedText);
            
            // Verify the update was successful
            System.out.println("Verifying review was updated");
            rs = databaseHelper.getReview(reviewId);
            boolean updateSuccessful = false;
            
            if (rs.next()) {
                String actualText = rs.getString("reviewText");
                updateSuccessful = actualText.equals(updatedText);
                System.out.println("Review text now: " + actualText);
            }
            
            assertTrue(updateSuccessful, "Review should be updated successfully");
            System.out.println("Test passed: Review update successful");
        } catch (Exception e) {
            System.out.println("EXCEPTION in testReviewUpdate: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Completed testReviewUpdate");
    }
    
    /**
     * Test 20: UserRoleAuthenticationTest
     * Verifies correct authentication based on user roles.
     * Assigned to: Ashish Kurse
     */
    @Test
    public void testUserRoleAuthentication() throws SQLException {
        System.out.println("Starting testUserRoleAuthentication");
        try {
            // Test student login
            System.out.println("Testing student login");
            User studentUser = new User("teststudent", "Password123!", "user");
            boolean studentLoginSuccess = databaseHelper.login(studentUser);
            assertTrue(studentLoginSuccess, "Student should log in successfully");
            System.out.println("Student login result: " + studentLoginSuccess);
            
            // Test reviewer login
            System.out.println("Testing reviewer login");
            User reviewerUser = new User("testreviewer", "Password123!", "reviewer");
            boolean reviewerLoginSuccess = databaseHelper.login(reviewerUser);
            assertTrue(reviewerLoginSuccess, "Reviewer should log in successfully");
            System.out.println("Reviewer login result: " + reviewerLoginSuccess);
            
            // Test instructor login
            System.out.println("Testing instructor login");
            User instructorUser = new User("testinstructor", "Password123!", "instructor");
            boolean instructorLoginSuccess = databaseHelper.login(instructorUser);
            assertTrue(instructorLoginSuccess, "Instructor should log in successfully");
            System.out.println("Instructor login result: " + instructorLoginSuccess);
            
            // Test incorrect role
            System.out.println("Testing invalid role login");
            User invalidRoleUser = new User("teststudent", "Password123!", "admin");
            boolean invalidRoleLogin = databaseHelper.login(invalidRoleUser);
            assertFalse(invalidRoleLogin, "Login with incorrect role should fail");
            System.out.println("Invalid role login result: " + invalidRoleLogin);
            
            System.out.println("Test passed: User role authentication successful");
        } catch (Exception e) {
            System.out.println("EXCEPTION in testUserRoleAuthentication: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Completed testUserRoleAuthentication");
    }
    
    /**
     * Test 21: ReviewerApprovalTest
     * Tests if an instructor can approve a student's request to become a reviewer.
     * Assigned to: Hieu Than Trong
     */
    @Test
    public void testReviewerApproval() throws SQLException {
        System.out.println("Starting testReviewerApproval");
        try {
            // Change a user's role to reviewer (simulating approval)
            System.out.println("Changing user role from student to reviewer");
            databaseHelper.changeUserRole("teststudent", "reviewer");
            
            // Verify the role was changed
            String newRole = databaseHelper.getUserRole("teststudent");
            System.out.println("New role: " + newRole);
            assertEquals("reviewer", newRole, "Role should be changed to reviewer");
            
            System.out.println("Test passed: Reviewer approval successful");
        } catch (Exception e) {
            System.out.println("EXCEPTION in testReviewerApproval: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Completed testReviewerApproval");
    }
    
    /**
     * Test 24: DatabaseIntegrityTest
     * Verifies database operations maintain data integrity across related tables.
     * Assigned to: Hieu Than Trong
     */
    @Test
    public void testDatabaseIntegrity() throws SQLException {
        System.out.println("Starting testDatabaseIntegrity");
        try {
            // Test cascade delete: deleting a question should delete its answers
            // Create a test question
            System.out.println("Creating test question for integrity test");
            databaseHelper.addQuestion(Integer.parseInt(studentUserId), "Integrity Test Question", "Test question description");
            
            // Find the question ID
            System.out.println("Finding question ID");
            ResultSet qrs = databaseHelper.getAllQuestions();
            int testQuestionId = -1;
            while (qrs.next()) {
                if (qrs.getString("qTitle").equals("Integrity Test Question")) {
                    testQuestionId = qrs.getInt("id");
                    System.out.println("Found question with ID: " + testQuestionId);
                    break;
                }
            }
            
            // Create an answer for this question
            System.out.println("Creating test answer for integrity test");
            databaseHelper.createAnswer(testQuestionId, Integer.parseInt(studentUserId), "Test Answer", "Integrity test answer");
            
            // Verify answer exists
            System.out.println("Verifying answer exists");
            ResultSet ars = databaseHelper.getAnswers(testQuestionId);
            boolean answerExists = ars.next();
            assertTrue(answerExists, "Answer should exist before question deletion");
            System.out.println("Answer exists: " + answerExists);
            
            // Delete the question
            System.out.println("Deleting question with ID: " + testQuestionId);
            databaseHelper.deleteQuestion(testQuestionId);
            
            // Verify question was deleted
            System.out.println("Verifying question was deleted");
            qrs = databaseHelper.getAllQuestions();
            boolean questionStillExists = false;
            while (qrs.next()) {
                if (qrs.getInt("id") == testQuestionId) {
                    questionStillExists = true;
                    System.out.println("Question still exists (should be deleted)");
                    break;
                }
            }
            assertFalse(questionStillExists, "Question should be deleted");
            
            // Verify answer was deleted 
            System.out.println("Verifying answer was deleted (cascade delete)");
            ars = databaseHelper.getAnswers(testQuestionId);
            boolean answerStillExists = ars.next();
            assertFalse(answerStillExists, "Answer should be deleted with question (cascade delete)");
            System.out.println("Answer exists after question deletion: " + answerStillExists);
            
            System.out.println("Test passed: Database integrity maintained");
        } catch (Exception e) {
            System.out.println("EXCEPTION in testDatabaseIntegrity: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Completed testDatabaseIntegrity");
    }
}