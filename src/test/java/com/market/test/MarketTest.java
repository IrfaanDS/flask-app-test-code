package com.market.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement; // <-- CRITICAL MISSING IMPORT
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

// ... (Rest of the class code)
// Extends BaseTest to get the WebDriver setup/teardown logic
public class MarketTest extends BaseTest {

    private final String TEST_PASSWORD = "testpassword123";

    // --- TEST CASES 1 & 2: Navigation and Page Load ---

    @Test
    @DisplayName("Test 1: Verify Login Page Loads Successfully")
    public void testLoginPageLoad() {
        // BaseTest.setUp() goes to the root URL, which redirects to login if unauthenticated
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        // 1. Check for the Login form header
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-signin h1")));
        assertEquals("Please Login", header.getText(), "Login page header verification failed.");
        
        // 2. Check for the Register link
        WebElement registerLink = driver.findElement(By.linkText("Register"));
        assertTrue(registerLink.isDisplayed(), "Register link is not displayed on the login page.");
    }

    @Test
    @DisplayName("Test 2: Navigate to Register Page from Login")
    public void testNavigateToRegisterPage() {
        // Start on Login Page (from T1 verification)
        driver.get(driver.getCurrentUrl() + "/login"); 
        
        // Click the 'Register' link on the Login page
        driver.findElement(By.linkText("Register")).click();

        // Verify the browser is now on the Register page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-register h1")));
        
        assertEquals("Please Create your Account", header.getText(), "Navigation to Register page failed.");
        assertTrue(driver.getCurrentUrl().contains("/register"), "URL does not contain /register.");
    }

    // --- TEST CASE 3 & 4: Registration and Validation ---
    
    @Test
    @DisplayName("Test 3: Successful User Registration")
    public void testSuccessfulUserRegistration() {
        String uniqueUser = TestUtils.generateUniqueString();

        // Use helper function to register
        TestUtils.registerUser(driver, uniqueUser, uniqueUser + "@test.com", TEST_PASSWORD);
        
        // Verify success flash message (must check for alert/flash message implementation)
        // Flash messages usually appear in a div with class 'alert' or similar
        // We'll rely on the redirect to /market and a quick title check for simplicity
        
        // After successful registration, the user is logged in and redirected to /market
        assertTrue(driver.getCurrentUrl().contains("/market"), "Successful registration did not redirect to /market.");
        
        // Ensure the success message is visible (assuming it's a dismissible alert/flash)
        // This is highly dependent on your 'base.html' structure. Assuming it uses a div.
        WebElement flashMessage = driver.findElement(By.cssSelector(".alert-success")); 
        assertTrue(flashMessage.getText().contains("Account created!"), "Success flash message not found.");
    }

    @Test
    @DisplayName("Test 4: Registration with Existing Username")
    public void testRegistrationWithExistingUsername() {
        // This test requires the target username to exist. Use a known unique user name: 'testuser'
        final String EXISTING_USER = "testuser_existing";
        
        // 1. Ensure the user exists (Register first time)
        TestUtils.registerUser(driver, EXISTING_USER, EXISTING_USER + "@temp.com", TEST_PASSWORD);
        TestUtils.loginUser(driver, "non-existent", "wrongpass"); // Log out or fail login to ensure starting fresh
        
        // 2. Attempt registration with the same username
        driver.get(driver.getCurrentUrl() + "/register"); 
        driver.findElement(By.name("username")).sendKeys(EXISTING_USER); // Existing username
        driver.findElement(By.name("email_address")).sendKeys("new_unique@email.com"); 
        driver.findElement(By.name("password1")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.name("password2")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.cssSelector(".form-register button[type='submit']")).click();
        
        // 3. Verify the validation error message appears (username already exists!)
        WebElement validationError = driver.findElement(By.cssSelector(".alert-danger"));
        assertTrue(validationError.getText().contains("Username already exists!"), "Username validation error not displayed.");
    }

    // --- TEST CASE 5, 6 & 7: Login and Validation ---

    @Test
    @DisplayName("Test 5: Registration with Mismatched Passwords")
    public void testRegistrationWithMismatchedPasswords() {
        driver.get(driver.getCurrentUrl() + "/register"); 
        
        driver.findElement(By.name("username")).sendKeys("mismatch_user");
        driver.findElement(By.name("email_address")).sendKeys("mismatch@test.com");
        driver.findElement(By.name("password1")).sendKeys("PasswordA");
        driver.findElement(By.name("password2")).sendKeys("PasswordB"); // Mismatch
        driver.findElement(By.cssSelector(".form-register button[type='submit']")).click();
        
        // The WTForms EqualTo validator usually prevents the POST request if done client-side, 
        // but if validation is server-side, it will submit and return to the form.
        // Check if the current URL is still /register (failed to redirect)
        assertTrue(driver.getCurrentUrl().contains("/register"), "Mismatched passwords allowed redirect.");
        
        // Optionally, check for the specific WTForm error text if accessible by Selenium.
    }

    @Test
    @DisplayName("Test 6: Successful User Login")
    public void testSuccessfulUserLogin() {
        final String LOGIN_USER = "login_test_user";
        
        // 1. Ensure a user exists to log in with
        TestUtils.registerUser(driver, LOGIN_USER, LOGIN_USER + "@login.com", TEST_PASSWORD);
        TestUtils.loginUser(driver, "not_a_user", "wrong"); // Log out or fail login to clear session

        // 2. Perform login
        TestUtils.loginUser(driver, LOGIN_USER, TEST_PASSWORD);
        
        // 3. Verify success (redirect to /market)
        assertTrue(driver.getCurrentUrl().contains("/market"), "Successful login failed to redirect to /market.");
        
        // 4. Verify the success flash message
        WebElement flashMessage = driver.findElement(By.cssSelector(".alert-success"));
        assertTrue(flashMessage.getText().contains("Logged in as " + LOGIN_USER), "Login success message not displayed.");
    }

    @Test
    @DisplayName("Test 7: Login with Invalid Password")
    public void testLoginWithInvalidPassword() {
        final String VALID_USER = "valid_user_for_fail_test";
        
        // 1. Ensure a valid user exists
        TestUtils.registerUser(driver, VALID_USER, VALID_USER + "@fail.com", TEST_PASSWORD);
        
        // 2. Attempt login with wrong password
        TestUtils.loginUser(driver, VALID_USER, "WRONG_PASSWORD_123");
        
        // 3. Verify the danger flash message
        WebElement flashMessage = driver.findElement(By.cssSelector(".alert-danger"));
        assertTrue(flashMessage.getText().contains("Wrong username or password"), "Invalid password warning not displayed.");
    }

    // --- TEST CASE 8, 9 & 10: Market Functionality and Security ---

    @Test
    @DisplayName("Test 8: Market Page Load and Item Availability Check")
    public void testMarketPageLoadAndItemsAvailable() {
        // ... setup and login ...
        
        // 2. CRITICAL FIX: The page should be /market by now. Wait for the market page to load.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/market")); 
        
        // FIX: The locator for the header is likely correct, but let's re-verify its visibility
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".col-8 h2")));
        assertEquals("Available items on the Market", header.getText(), "Market page header verification failed.");
    
        // 3. Verify the dummy item 'iPhone X' is listed (assuming you inserted this data)
        WebElement itemRow = driver.findElement(By.xpath("//td[text()='iPhone X']/..")); 
        assertTrue(itemRow.isDisplayed(), "Dummy item 'iPhone X' is not displayed in the market table.");
    }
    
    @Test
    @DisplayName("Test 9: Logout Functionality")
    public void testLogoutFunctionality() {
        // ... login setup ...
        
        // 2. Navigate to /logout
        driver.get(driver.getCurrentUrl().replace("/market", "/logout"));
        
        // 3. CRITICAL FIX: Wait for the expected final URL (home page)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/home")); 
        
        assertTrue(driver.getCurrentUrl().contains("/home"), "Logout failed to redirect.");
        // ...
    }
    
    @Test
    @DisplayName("Test 10: Attempt to Access Market Unauthenticated (Security)")
    public void testAttemptToAccessMarketUnauthenticated() {
        // ... code to go to /logout ...
        
        // 2. Attempt to go directly to /market 
        driver.get(driver.getCurrentUrl() + "/market"); 
    
        // 3. CRITICAL FIX: Wait for the URL to contain the required redirect to /login
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/login"));
        
        assertTrue(driver.getCurrentUrl().contains("/login"), "Unauthenticated access did not redirect to login.");
        // ...
    }
}