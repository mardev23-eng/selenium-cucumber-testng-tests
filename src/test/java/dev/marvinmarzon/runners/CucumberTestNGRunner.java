package dev.marvinmarzon.runners;

import dev.marvinmarzon.driver.EnterpriseWebDriverManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

/**
 * Enterprise Cucumber-TestNG Runner with parallel execution support
 * Provides comprehensive test execution management and reporting
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"dev.marvinmarzon.stepdefs"},
    plugin = {
        "pretty",
        "html:target/reports/cucumber-html-report",
        "json:target/reports/cucumber-json-report.json",
        "junit:target/reports/cucumber-junit-report.xml",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "timeline:target/reports/timeline"
    },
    monochrome = true,
    dryRun = false,
    tags = "not @ignore"
)
public class CucumberTestNGRunner extends AbstractTestNGCucumberTests {
    
    private static final Logger logger = LoggerFactory.getLogger(CucumberTestNGRunner.class);

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        logger.info("=== Starting Cucumber-TestNG Test Suite ===");
        logger.info("Test execution started at: {}", java.time.LocalDateTime.now());
        
        // Log system information
        logger.info("Java Version: {}", System.getProperty("java.version"));
        logger.info("OS: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
        logger.info("User: {}", System.getProperty("user.name"));
        
        // Log test configuration
        logTestConfiguration();
        
        // Initialize any suite-level resources if needed
        logger.info("Suite setup completed successfully");
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        logger.info("=== Finishing Cucumber-TestNG Test Suite ===");
        
        try {
            // Ensure all drivers are properly closed
            EnterpriseWebDriverManager.quitAllDrivers();
            logger.info("All WebDriver instances cleaned up");
            
            // Log suite completion
            logger.info("Test execution completed at: {}", java.time.LocalDateTime.now());
            logger.info("Suite teardown completed successfully");
            
        } catch (Exception e) {
            logger.error("Error during suite teardown", e);
        }
    }

    /**
     * Enable parallel execution at scenario level
     * Thread count is controlled by TestNG configuration or Maven properties
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    /**
     * Log test configuration for debugging
     */
    private void logTestConfiguration() {
        logger.info("Test Configuration:");
        logger.info("  Browser: {}", System.getProperty("test.browser", "chrome"));
        logger.info("  Headless: {}", System.getProperty("test.headless", "false"));
        logger.info("  Environment: {}", System.getProperty("test.environment", "local"));
        logger.info("  Thread Count: {}", System.getProperty("test.thread.count", "1"));
        logger.info("  Remote Execution: {}", System.getProperty("test.remote", "false"));
        
        if ("true".equals(System.getProperty("test.remote"))) {
            logger.info("  Grid URL: {}", System.getProperty("test.grid.url", "http://localhost:4444/wd/hub"));
        }
    }
}