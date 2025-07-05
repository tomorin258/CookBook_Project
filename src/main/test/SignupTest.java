package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import service.UserService;

public class SignupTest {

    private final UserService userService = new UserService();
    
    @Test
    public void testEmptyUsername() {
        String result = userService.register("", "password123", "password123");
        assertEquals("account name cannot be empty!", result);
    }
    
    @Test
    public void testPasswordMismatch() {
        String result = userService.register("newuser", "password123", "different123");
        assertEquals("the two passwords do not match!", result);
    }
    
    @Test
    public void testDuplicateUsername() {
        // First, register a user which should succeed.
        String result1 = userService.register("duplicateUser", "password123", "password123");
        // Then, try to register again with the same username.
        String result2 = userService.register("duplicateUser", "password123", "password123");
        assertEquals("account name already exists, please choose another one!", result2);
    }
    
    @Test
    public void testSuccessfulSignup() {
        // Use a unique username by appending current time.
        String uniqueUsername = "uniqueUser" + System.currentTimeMillis();
        String result = userService.register(uniqueUsername, "password123", "password123");
        assertEquals("sign up successfully, please login now!", result);
    }
}
