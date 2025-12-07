package com.market.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    // Reads URL from Jenkins passed property, defaults to localhost if not set
    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:80");

    @BeforeEach
    public void setUp() {
        // Initialize ChromeOptions for Headless execution
        ChromeOptions options = new ChromeOptions();
        // Required for Jenkins/Docker environment
        options.addArguments("--no-sandbox"); 
        options.addArguments("--headless=new"); 
        options.addArguments("--disable-dev-shm-usage"); // Required for markhobson/maven-chrome

        // Instantiate WebDriver
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Navigate to the base URL (which is the EC2 IP:80)
        driver.get(BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}