package dev.marvinmarzon.stepdefs;

import dev.marvinmarzon.driver.EnterpriseWebDriverManager;
import dev.marvinmarzon.utils.TestConfigManager;
import dev.marvinmarzon.utils.Screenshot;
import dev.marvinmarzon.utils.VideoRecorder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Base step definitions for Cucumber-TestNG with enterprise-level setup and teardown
 * Provides comprehensive test execution management with proper resource handling
 */
public class BaseStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(BaseStepDefinitions.class);
    
    @Before(order = 0)
    public void setUpEnvironment(Scenario scenario) {
        logger.info("=== Starting scenario: {} ===", scenario.getName());
        logger.info("Thread ID: {}, Tags: {}", Thread.currentThread().getId(), scenario.getSourceTagNames());
        
        try {
            // Log configuration for debugging
            logTestConfiguration();
            
            // Initialize WebDriver for this thread
            EnterpriseWebDriverManager.initializeDriver();
            
            // Start video recording if enabled
            if (TestConfigManager.isVideoRecordingEnabled()) {
                VideoRecorder.startRecording(scenario.getName());
                logger.debug("Video recording started for scenario: {}", scenario.getName());
            }
            
            // Navigate to application
            EnterpriseWebDriverManager.navigateToApplication();
            
            logger.info("Setup completed successfully for scenario: {} on thread: {}", 
                       scenario.getName(), Thread.currentThread().getId());
            
        } catch (Exception e) {
            logger.error("Setup failed for scenario: {} on thread: {}", 
                        scenario.getName(), Thread.currentThread().getId(), e);
            
            // Take screenshot of failure if possible
            try {
                if (EnterpriseWebDriverManager.isDriverInitialized()) {
                    byte[] screenshot = Screenshot.takeScreenshot();
                    if (screenshot != null) {
                        scenario.attach(screenshot, "image/png", "Setup Failure Screenshot");
                    }
                }
            } catch (Exception screenshotException) {
                logger.error("Failed to take screenshot during setup failure", screenshotException);
            }
            
            // Clean up on setup failure
            EnterpriseWebDriverManager.quitDriver();
            throw new RuntimeException("Test setup failed for scenario: " + scenario.getName(), e);
        }
    }

    @After(order = 1000)
    public void tearDownEnvironment(Scenario scenario) {
        logger.info("=== Finishing scenario: {} - Status: {} ===", 
                   scenario.getName(), scenario.getStatus());
        
        try {
            // Take screenshot on failure or if always enabled
            if (scenario.isFailed() || TestConfigManager.isScreenshotEnabled()) {
                takeAndAttachScreenshot(scenario);
            }
            
            // Handle video recording
            if (TestConfigManager.isVideoRecordingEnabled()) {
                handleVideoRecording(scenario);
            }
            
            // Log scenario completion details
            logScenarioCompletion(scenario);
            
        } catch (Exception e) {
            logger.error("Error during teardown for scenario: {} on thread: {}", 
                        scenario.getName(), Thread.currentThread().getId(), e);
        } finally {
            // Always quit driver for this thread
            EnterpriseWebDriverManager.quitDriver();
            logger.info("Teardown completed for scenario: {} on thread: {}", 
                       scenario.getName(), Thread.currentThread().getId());
        }
    }

    /**
     * Take screenshot and attach to scenario
     */
    private void takeAndAttachScreenshot(Scenario scenario) {
        try {
            if (EnterpriseWebDriverManager.isDriverInitialized()) {
                byte[] screenshot = Screenshot.takeScreenshot();
                if (screenshot != null) {
                    String screenshotName = scenario.isFailed() ? "Failure Screenshot" : "Screenshot";
                    scenario.attach(screenshot, "image/png", screenshotName);
                    logger.debug("Screenshot attached for scenario: {}", scenario.getName());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to take screenshot for scenario: {}", scenario.getName(), e);
        }
    }

    /**
     * Handle video recording completion
     */
    private void handleVideoRecording(Scenario scenario) {
        try {
            String videoPath = VideoRecorder.stopRecording();
            if (videoPath != null) {
                // Attach video path for failed scenarios or if configured
                if (scenario.isFailed()) {
                    scenario.attach(videoPath.getBytes(), "text/plain", "Video Recording Path");
                    logger.info("Video recording attached for failed scenario: {}", scenario.getName());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to handle video recording for scenario: {}", scenario.getName(), e);
        }
    }

    /**
     * Log test configuration for debugging
     */
    private void logTestConfiguration() {
        logger.debug("Test Configuration:");
        logger.debug("  Browser: {}", TestConfigManager.getBrowser());
        logger.debug("  Headless: {}", TestConfigManager.getHeadlessMode());
        logger.debug("  Environment: {}", TestConfigManager.getEnvironment());
        logger.debug("  Base URL: {}", TestConfigManager.getBaseUrl());
        logger.debug("  Remote Execution: {}", TestConfigManager.isRemoteExecution());
        logger.debug("  Thread Count: {}", TestConfigManager.getThreadCount());
        logger.debug("  Active Drivers: {}", EnterpriseWebDriverManager.getActiveDriverCount());
    }

    /**
     * Log scenario completion details
     */
    private void logScenarioCompletion(Scenario scenario) {
        logger.info("Scenario completion details:");
        logger.info("  Name: {}", scenario.getName());
        logger.info("  Status: {}", scenario.getStatus());
        logger.info("  Thread: {}", Thread.currentThread().getId());
        logger.info("  Tags: {}", scenario.getSourceTagNames());
        
        if (scenario.isFailed()) {
            logger.error("Scenario FAILED: {}", scenario.getName());
        } else {
            logger.info("Scenario PASSED: {}", scenario.getName());
        }
    }
}