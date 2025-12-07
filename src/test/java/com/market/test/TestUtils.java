package com.market.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions; // Import for explicit wait
import org.openqa.selenium.support.ui.WebDriverWait;     // Import for explicit wait
import java.time.Duration;

public class TestUtils {
    
    // ... generateUniqueString method ...

    // Helper method to register a user
    public static void registerUser(WebDriver driver, String username, String email, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(driver.getCurrentUrl() + "/register");

        // FIX 2: Wait explicitly for the Register form to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-register")));

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("email_address")).sendKeys(email);
        driver.findElement(By.name("password1")).sendKeys(password);
        driver.findElement(By.name("password2")).sendKeys(password);
        
        // FIX 1: Corrected selector to target <input type="submit">
        driver.findElement(By.cssSelector(".form-register input[type='submit']")).click(); 
    }

    // Helper method to log in
    public static void loginUser(WebDriver driver, String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(driver.getCurrentUrl() + "/login"); 

        // FIX 2: Wait explicitly for the Login form to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-signin")));

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector(".form-signin input[type='submit']")).click(); // Also fix the login submit button
    }
}