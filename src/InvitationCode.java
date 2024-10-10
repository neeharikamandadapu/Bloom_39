package asuHelloWorldJavaFX;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p> InvitationCode Class </p>
 * 
 * <p> This class handles the generation of an invitation code (OTP) and the 
 * process of storing it in the database when inviting a new user. The generated
 * OTP can be sent to the invited user via email for further verification. </p>
 * 
 * @authors Neeharika Mandadapu, Genelle Jenkins, Siddharth Sanjay, Krutarth Thakkar, Monil Patel
 * 
 * @version 1.00 2024-10-09 
 */
public class InvitationCode {

    /**
     * Generates a 6-digit random OTP (One-Time Password) using SecureRandom.
     * 
     * @return a 6-digit OTP as a String, padded with zeros if necessary.
     */
    public static String generateOTP() {
        SecureRandom random = new SecureRandom();  // SecureRandom for cryptographic safety
        int otp = random.nextInt(999999);  // Generate a random integer between 0 and 999999
        return String.format("%06d", otp);  // Return the OTP as a zero-padded 6-digit string
    }

    /**
     * Invites a user by inserting their information, including a generated OTP, 
     * into the 'Invitations' table in the database.
     */
    public static void inviteUser(String username, String email, String roles) {
        // Generate a new OTP for the invitation
        String otp = generateOTP();  

        // SQL statement to insert the invitation into the Invitations table
        String insertInvitationSQL = "INSERT INTO Invitations (username, email, invitation_code, role, is_used) VALUES (?, ?, ?, ?, 0)";

        try (Connection conn = DatabaseConnection.connect();  // Connect to the database
             PreparedStatement pstmt = conn.prepareStatement(insertInvitationSQL)) {
            // Set the values in the prepared statement
            pstmt.setString(1, username);  // Set the username
            pstmt.setString(2, email);  // Set the email address
            pstmt.setString(3, otp);  // Set the generated OTP (invitation code)
            pstmt.setString(4, roles);  // Set the roles (comma-separated if multiple)
            pstmt.executeUpdate();  // Execute the insert statement

            // Output a success message to the console
            System.out.println("Invitation created for " + username + " with OTP: " + otp);

            // Optionally: Send the OTP via email to the user here (email functionality not implemented)
        } catch (SQLException e) {
            // If there's an error with the database operation, print the stack trace
            e.printStackTrace();
        }
    }

}
