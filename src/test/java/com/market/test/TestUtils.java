package com.market.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestUtils {
    
    // Generates a unique username/email based on a timestamp
    public static String generateUniqueString() {
        return "user_" + System.currentTimeMillis();
    }

    // Helper method to register a user (used by Test Case 3 & 6)
    public static void registerUser(WebDriver driver, String username, String email, String password) {
        driver.get(driver.getCurrentUrl() + "/register"); // Navigate explicitly

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("email_address")).sendKeys(email);
        driver.findElement(By.name("password1")).sendKeys(password);
        driver.findElement(By.name("password2")).sendKeys(password);
        driver.findElement(By.cssSelector(".form-register button[type='submit']")).click();
    }

    // Helper method to log in (used by Test Case 6 & 8)
    public static void loginUser(WebDriver driver, String username, String password) {
        // Assume starting on a page that links to login, or explicitly go to /login
        driver.get(driver.getCurrentUrl() + "/login"); 

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector(".form-signin button[type='submit']")).click();
    }
}