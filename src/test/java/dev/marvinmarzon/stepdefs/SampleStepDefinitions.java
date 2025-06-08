package dev.marvinmarzon.stepdefs;

import dev.marvinmarzon.driver.EnterpriseWebDriverManager;
import dev.marvinmarzon.utils.Screenshot;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;

/**
 * Sample step definitions demonstrating enterprise patterns
 * Uses composition instead of inheritance to avoid Cucumber restrictions
 */
public class SampleStepDefinitions {
    
    private static final Logger logger = LoggerFactory.getLogger(SampleStepDefinitions.class);
    private WebDriverWait wait;

    @Given("I am on the home page")
    public void i_am_on_the_home_page() {
        WebDriver driver = getCurrentDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        logger.info("Verifying home page is loaded on thread: {}", Thread.currentThread().getId());
        
        // Wait for page to load completely
        wait.until(ExpectedConditions.titleContains(""));
        
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle();
        
        logger.info("Current URL: {}", currentUrl);
        logger.info("Page Title: {}", pageTitle);
        
        // Verify we're on the correct page
        assertWithLogging(driver.getCurrentUrl().contains("localhost") || 
                         driver.getCurrentUrl().contains("example.com"), 
                         "Should be on the application home page");
    }

    @When("I click on the {string} button")
    public void i_click_on_the_button(String buttonText) {
        WebDriver driver = getCurrentDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        logger.info("Clicking on button: {} on thread: {}", buttonText, Thread.currentThread().getId());
        
        try {
            // Try multiple selectors for the button
            WebElement button = null;
            
            // Try by button text
            try {
                button = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'" + buttonText + "')]")));
            } catch (Exception e) {
                logger.debug("Button not found by text, trying other selectors");
            }
            
            // Try by input value
            if (button == null) {
                try {
                    button = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@value='" + buttonText + "']")));
                } catch (Exception e) {
                    logger.debug("Button not found by input value, trying link text");
                }
            }
            
            // Try by link text
            if (button == null) {
                try {
                    button = wait.until(ExpectedConditions.elementToBeClickable(
                        By.linkText(buttonText)));
                } catch (Exception e) {
                    logger.debug("Button not found by link text");
                }
            }
            
            assertWithLogging(button != null, "Button '" + buttonText + "' should be found and clickable");
            
            // Scroll to element if needed
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", button);
            
            // Click the button
            button.click();
            logger.info("Successfully clicked button: {}", buttonText);
            
        } catch (Exception e) {
            logger.error("Failed to click button: {} on thread: {}", buttonText, Thread.currentThread().getId(), e);
            throw new RuntimeException("Failed to click button: " + buttonText, e);
        }
    }

    @Then("I should see {string} on the page")
    public void i_should_see_on_the_page(String expectedText) {
        WebDriver driver = getCurrentDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        logger.info("Verifying text '{}' is present on page on thread: {}", expectedText, Thread.currentThread().getId());
        
        try {
            // Wait for text to be present
            boolean textPresent = wait.until(ExpectedConditions.textToBePresentInElement(
                driver.findElement(By.tagName("body")), expectedText));
            
            assertWithLogging(textPresent, "Text '" + expectedText + "' should be present on the page");
            
            logger.info("Successfully verified text '{}' is present on page", expectedText);
            
        } catch (Exception e) {
            logger.error("Failed to find text '{}' on page on thread: {}", expectedText, Thread.currentThread().getId(), e);
            
            // Log page source for debugging
            logger.debug("Current page source: {}", driver.getPageSource());
            
            throw new RuntimeException("Text '" + expectedText + "' not found on page", e);
        }
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedTitleText) {
        WebDriver driver = getCurrentDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        logger.info("Verifying page title contains '{}' on thread: {}", expectedTitleText, Thread.currentThread().getId());
        
        try {
            // Wait for title to contain expected text
            boolean titleContains = wait.until(ExpectedConditions.titleContains(expectedTitleText));
            
            String actualTitle = driver.getTitle();
            logger.info("Actual page title: {}", actualTitle);
            
            assertWithLogging(titleContains, 
                "Page title should contain '" + expectedTitleText + "' but was '" + actualTitle + "'");
            
            logger.info("Successfully verified page title contains '{}'", expectedTitleText);
            
        } catch (Exception e) {
            logger.error("Failed to verify page title contains '{}' on thread: {}", expectedTitleText, Thread.currentThread().getId(), e);
            throw new RuntimeException("Page title verification failed", e);
        }
    }

    /**
     * Utility method for step definitions to assert with proper logging
     */
    private void assertWithLogging(boolean condition, String message) {
        if (!condition) {
            logger.error("Assertion failed: {}", message);
            // Take screenshot on assertion failure
            try {
                byte[] screenshot = Screenshot.takeScreenshot();
                if (screenshot != null) {
                    logger.info("Screenshot taken for assertion failure");
                }
            } catch (Exception e) {
                logger.error("Failed to take screenshot on assertion failure", e);
            }
        }
        Assert.assertTrue(condition, message);
    }

    /**
     * Utility method to get current driver with null check
     */
    private WebDriver getCurrentDriver() {
        WebDriver driver = EnterpriseWebDriverManager.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized for current thread: " + Thread.currentThread().getId());
        }
        return driver;
    }
}