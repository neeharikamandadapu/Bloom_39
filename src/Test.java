package projectTestingAutomation;

import java.sql.Connection;
import java.sql.SQLException;
import database.DatabaseConnection;
import user.UserManager;
import admin.AdminManager;

/**
 * <p> TestAutomation Class </p>
 * 
 * <p> Description: This class provides automated testing for the core functionalities of the project. 
 * It runs multiple test cases to verify that user authentication, role management, and invitation 
 * code handling are working as expected. </p>
 * 
 * <p> Copyright: Janelle Jenkins © 2024 </p>
 * 
 * @author Janelle Jenkins
 * @version 1.00		2024-10-10 The initial implementation of the project testing automation
 */
public class TestAutomation {
	
	/**********************************************************************************************

	Attributes
	
	**********************************************************************************************/
	
	/** Counter for passed tests */
	static int numPassed = 0;
	
	/** Counter for failed tests */
	static int numFailed = 0;

	/**********************************************************************************************

	Main Method
	
	**********************************************************************************************/

	/**
	 * The main method that runs all the test cases.
	 * 
	 * @param args Command line arguments (not used)
	 */
	public static void main(String[] args) {
	    System.out.println("____________________________________________________________________________");
	    System.out.println("\nProject Testing Automation");

	    // First, ensure that the first user becomes Admin before proceeding to test regular users
	    performTestCase(1, "First User Login as Admin", true, testFirstUserAdmin());

	    // Proceed with other tests
	    performTestCase(2, "User Login with Valid Credentials", true, testValidUserLogin());
	    performTestCase(3, "User Login with Invalid Credentials", false, testInvalidUserLogin());
	    performTestCase(4, "Invite User with Valid Invitation Code", true, testInviteUserWithValidCode());
	    performTestCase(5, "Use Invalid Invitation Code", false, testInvalidInvitationCode());
	    performTestCase(6, "Delete Existing User", true, testDeleteUser());
	    performTestCase(7, "Reset User Password", true, testResetPassword());
	    performTestCase(8, "Assign Role to User", true, testAssignRoleToUser());

	    System.out.println("____________________________________________________________________________");
	    System.out.println();
	    System.out.println("Number of tests passed: " + numPassed);
	    System.out.println("Number of tests failed: " + numFailed);
	}

	/**********************************************************************************************

	Helper Methods
	
	**********************************************************************************************/

	/**
	 * Performs a single test case and updates the pass/fail counters.
	 * 
	 * @param testCase The test case number
	 * @param description A description of the test case
	 * @param expectedPass Whether the test is expected to pass
	 * @param actualPass Whether the test actually passed
	 */
	private static void performTestCase(int testCase, String description, boolean expectedPass, boolean actualPass) {
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Description: " + description);
		System.out.println("Expected: " + (expectedPass ? "Pass" : "Fail"));
		System.out.println("Actual: " + (actualPass ? "Pass" : "Fail"));

		if (actualPass == expectedPass) {
			System.out.println("***Success***");
			numPassed++;
		} else {
			System.out.println("***Failure***");
			numFailed++;
		}
	}

	/**********************************************************************************************

	Test Methods
	
	**********************************************************************************************/

	/**
	 * Test first user login automatically assigns Admin role.
	 */
	private static boolean testFirstUserAdmin() {
		try {
			InitialLogin login = new InitialLogin();
			login.createUser("firstUser", "adminPassword");
			// Ensure the first user becomes Admin
			return login.getRole("firstUser").equals("Admin");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test user login with valid credentials.
	 */
	private static boolean testValidUserLogin() {
		try {
			InitialLogin login = new InitialLogin();
			// Assuming authenticate is the correct method for login
			return login.authenticate("validUser", "validPassword");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test user login with invalid credentials.
	 */
	private static boolean testInvalidUserLogin() {
		try {
			InitialLogin login = new InitialLogin();
			return !login.authenticate("invalidUser", "invalidPassword");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test inviting a user with a valid invitation code.
	 */
	private static boolean testInviteUserWithValidCode() {
		try {
			InvitationCode invite = new InvitationCode();
			String invitationCode = invite.generateInvitationCode("newUser@example.com", "Student");
			// Assuming acceptInvitation handles the process
			return invite.acceptInvitation("newUser", invitationCode);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test handling invalid invitation code.
	 */
	private static boolean testInvalidInvitationCode() {
		try {
			InvitationCode invite = new InvitationCode();
			return !invite.acceptInvitation("newUser", "invalidCode");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test deleting a user.
	 */
	private static boolean testDeleteUser() {
		try {
			DeleteUserPage deleteUser = new DeleteUserPage();
			// Assuming deleteUserFromDatabase handles user deletion
			deleteUser.createUser("userToDelete", "password123");
			deleteUser.deleteUserFromDatabase("userToDelete");
			return deleteUser.getUser("userToDelete") == null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test resetting a user's password.
	 */
	private static boolean testResetPassword() {
		try {
			AdminDashboard admin = new AdminDashboard();
			admin.resetPassword("userToReset", "newPassword123");
			// Assuming authenticate verifies the new password
			InitialLogin login = new InitialLogin();
			return login.authenticate("userToReset", "newPassword123");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Test assigning a new role to a user.
	 */
	private static boolean testAssignRoleToUser() {
		try {
			AddRemoveRolePage rolePage = new AddRemoveRolePage();
			rolePage.assignRole("existingUser", "Instructor");
			return rolePage.getRole("existingUser").equals("Instructor");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
