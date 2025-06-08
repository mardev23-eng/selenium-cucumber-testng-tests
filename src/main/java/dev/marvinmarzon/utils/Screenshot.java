package dev.marvinmarzon.utils;

import dev.marvinmarzon.driver.EnterpriseWebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enterprise screenshot utility for Cucumber-TestNG framework
 * Provides comprehensive screenshot capture with proper error handling
 */
public class Screenshot {
    private static final Logger logger = LoggerFactory.getLogger(Screenshot.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    static {
        // Create screenshot directory if it doesn't exist
        try {
            Path screenshotPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotPath)) {
                Files.createDirectories(screenshotPath);
                logger.debug("Created screenshot directory: {}", SCREENSHOT_DIR);
            }
        } catch (IOException e) {
            logger.error("Failed to create screenshot directory", e);
        }
    }

    /**
     * Take screenshot and return as byte array for Cucumber reports
     */
    public static byte[] takeScreenshot() {
        try {
            WebDriver driver = EnterpriseWebDriverManager.getDriver();
            if (driver == null) {
                logger.warn("Cannot take screenshot - WebDriver not initialized for thread: {}", 
                           Thread.currentThread().getId());
                return null;
            }

            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                logger.debug("Screenshot captured successfully for thread: {}", Thread.currentThread().getId());
                return screenshot;
            } else {
                logger.warn("Driver does not support screenshot capture: {}", driver.getClass().getSimpleName());
                return null;
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for thread: {}", Thread.currentThread().getId(), e);
            return null;
        }
    }

    /**
     * Take screenshot and save to file
     */
    public static String takeScreenshotToFile(String scenarioName) {
        try {
            byte[] screenshot = takeScreenshot();
            if (screenshot == null) {
                return null;
            }

            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String sanitizedScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");
            String fileName = String.format("%s_%s_%d.png", 
                                           sanitizedScenarioName, 
                                           timestamp, 
                                           Thread.currentThread().getId());
            
            Path filePath = Paths.get(SCREENSHOT_DIR, fileName);
            Files.write(filePath, screenshot);
            
            String absolutePath = filePath.toAbsolutePath().toString();
            logger.info("Screenshot saved: {}", absolutePath);
            return absolutePath;
            
        } catch (Exception e) {
            logger.error("Failed to save screenshot to file for scenario: {}", scenarioName, e);
            return null;
        }
    }
}