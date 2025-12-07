package com.market.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class TestUtils {

    // Helper method to generate a unique username/email based on a timestamp.
    // Must be static to be called directly via TestUtils.generateUniqueString().
    public static String generateUniqueString() {
        return "user_" + System.currentTimeMillis();
    }

    // Helper method to register a user.
    public static void registerUser(WebDriver driver, String username, String email, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Ensure we navigate to the registration page
        driver.get(driver.getCurrentUrl() + "/register");

        // 1. Wait explicitly for the Register form container to appear before typing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-register")));

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("email_address")).sendKeys(email);
        driver.findElement(By.name("password1")).sendKeys(password);
        driver.findElement(By.name("password2")).sendKeys(password);
        
        // 2. FIXED LOCATOR: Target the input[type='submit'] tag
        driver.findElement(By.cssSelector(".form-register input[type='submit']")).click();
    }

    // Helper method to log in.
    public static void registerUser(WebDriver driver, String username, String email, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        driver.get(driver.getCurrentUrl() + "/register");

        // FIX: Ensure the page is loaded by waiting for the header
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-register h1"))); 

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("email_address")).sendKeys(email);
        driver.findElement(By.name("password1")).sendKeys(password);
        driver.findElement(By.name("password2")).sendKeys(password);
        
        // Final corrected selector for <input type='submit'>
        driver.findElement(By.cssSelector(".form-register input[type='submit']")).click();
        
        // CRITICAL FIX: Wait until the URL changes AWAY from /register to ensure redirect success
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/register")));
    }

    // Helper method to log in
    public static void loginUser(WebDriver driver, String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        driver.get(driver.getCurrentUrl() + "/login"); 

        // FIX: Ensure the page is loaded by waiting for the header
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form-signin h1")));

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector(".form-signin input[type='submit']")).click();
        
        // CRITICAL FIX: Wait until the URL changes AWAY from /login to ensure redirect success
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }
}