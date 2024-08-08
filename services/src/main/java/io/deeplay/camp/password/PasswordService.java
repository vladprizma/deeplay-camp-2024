package io.deeplay.camp.password;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides services for hashing and checking passwords.
 * <p>
 * This class uses the BCrypt hashing algorithm to securely hash passwords and verify them.
 * It provides methods to hash a plain text password and to check a plain text password against a hashed password.
 * </p>
 */
public class PasswordService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    /**
     * Hashes a plain text password using BCrypt.
     * <p>
     * This method generates a salt and hashes the given plain text password.
     * </p>
     *
     * @param plainTextPassword The plain text password to be hashed.
     * @return The hashed password.
     */
    public static String hashPassword(String plainTextPassword) {
        logger.info("Hashing password");
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Checks a plain text password against a hashed password using BCrypt.
     * <p>
     * This method verifies if the given plain text password matches the hashed password.
     * </p>
     *
     * @param plainTextPassword The plain text password to be checked.
     * @param hashedPassword    The hashed password to check against.
     * @return True if the passwords match, false otherwise.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        logger.info("Checking password");
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}