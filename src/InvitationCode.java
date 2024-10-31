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

    // Method to generate a random OTP (invitation code)
    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(999999);  // Generate a 6-digit random number
        return String.format("%06d", otp);
    }

    // Method to insert an invitation with a generated OTP into the Invitations table
    public static void inviteUser(String username, String email, String roles) {
        String otp = generateOTP();  // Generate OTP

        String insertInvitationSQL = "INSERT INTO Invitations (username, email, invitation_code, role, is_used) VALUES (?, ?, ?, ?, 0)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertInvitationSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, otp);
            pstmt.setString(4, roles);  // Storing multiple roles as comma-separated string
            pstmt.executeUpdate();
            System.out.println("Invitation created for " + username + " with OTP: " + otp);

            // Optionally: Send OTP via email to the user here (not implemented)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}