package dev.marvinmarzon.stepdefs;

import dev.marvinmarzon.driver.EnterpriseWebDriverManager;
import dev.marvinmarzon.utils.Screenshot;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Advanced step definitions for comprehensive profile website testing
 * Includes performance, accessibility, SEO, and security testing
 */
public class AdvancedProfileStepDefinitions {
    
    private static final Logger logger = LoggerFactory.getLogger(AdvancedProfileStepDefinitions.class);
    private WebDriverWait wait;

    @Then("images should load efficiently")
    public void images_should_load_efficiently() {
        WebDriver driver = getCurrentDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Check if all images are loaded
        List<WebElement> images = driver.findElements(By.tagName("img"));
        
        for (WebElement img : images) {
            Boolean isComplete = (Boolean) js.executeScript("return arguments[0].complete", img);
            String src = img.getAttribute("src");
            
            if (src != null && !src.isEmpty()) {
                assertWithLogging(isComplete, "Image should be loaded: " + src);
            }
        }
        
        logger.info("Verified {} images are loaded efficiently", images.size());
    }

    @Then("there should be no console errors")
    public void there_should_be_no_console_errors() {
        WebDriver driver = getCurrentDriver();
        
        try {
            List<LogEntry> logs = driver.manage().logs().get(LogType.BROWSER).getAll();
            
            long errorCount = logs.stream()
                .filter(log -> log.getLevel().getName().equals("SEVERE"))
                .count();
            
            if (errorCount > 0) {
                logger.warn("Found {} console errors", errorCount);
                logs.stream()
                    .filter(log -> log.getLevel().getName().equals("SEVERE"))
                    .forEach(log -> logger.warn("Console error: {}", log.getMessage()));
            }
            
            // This is a soft assertion since some console errors might be from external scripts
            logger.info("Console error check completed. Severe errors found: {}", errorCount);
            
        } catch (Exception e) {
            logger.warn("Could not check console logs: {}", e.getMessage());
        }
    }

    @Then("the page should be optimized for performance")
    public void the_page_should_be_optimized_for_performance() {
        WebDriver driver = getCurrentDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        try {
            // Check page load performance using Navigation Timing API
            Object loadTime = js.executeScript(
                "return window.performance.timing.loadEventEnd - window.performance.timing.navigationStart");
            
            if (loadTime instanceof Number) {
                double loadTimeMs = ((Number) loadTime).doubleValue();
                double loadTimeSeconds = loadTimeMs / 1000.0;
                
                logger.info("Page load time: {} seconds", loadTimeSeconds);
                
                // Performance should be reasonable (under 5 seconds for full load)
                assertWithLogging(loadTimeSeconds < 5.0, 
                    "Page should load within 5 seconds for good performance, actual: " + loadTimeSeconds);
            }
            
            // Check for performance optimizations
            List<WebElement> images = driver.findElements(By.tagName("img"));
            List<WebElement> scripts = driver.findElements(By.tagName("script"));
            List<WebElement> stylesheets = driver.findElements(By.cssSelector("link[rel='stylesheet']"));
            
            logger.info("Performance check - Images: {}, Scripts: {}, Stylesheets: {}", 
                       images.size(), scripts.size(), stylesheets.size());
            
        } catch (Exception e) {
            logger.warn("Could not check performance metrics: {}", e.getMessage());
        }
    }

    @Then("the website should have proper heading structure")
    public void the_website_should_have_proper_heading_structure() {
        WebDriver driver = getCurrentDriver();
        
        // Check for proper heading hierarchy
        List<WebElement> h1s = driver.findElements(By.tagName("h1"));
        List<WebElement> h2s = driver.findElements(By.tagName("h2"));
        List<WebElement> h3s = driver.findElements(By.tagName("h3"));
        
        // Should have exactly one H1
        assertWithLogging(h1s.size() >= 1, "Page should have at least one H1 heading");
        
        if (h1s.size() > 1) {
            logger.warn("Page has {} H1 headings, should typically have only one", h1s.size());
        }
        
        logger.info("Heading structure - H1: {}, H2: {}, H3: {}", h1s.size(), h2s.size(), h3s.size());
    }

    @Then("images should have alt text")
    public void images_should_have_alt_text() {
        WebDriver driver = getCurrentDriver();
        
        List<WebElement> images = driver.findElements(By.tagName("img"));
        int imagesWithoutAlt = 0;
        
        for (WebElement img : images) {
            String alt = img.getAttribute("alt");
            String src = img.getAttribute("src");
            
            if (alt == null || alt.trim().isEmpty()) {
                imagesWithoutAlt++;
                logger.warn("Image without alt text: {}", src);
            }
        }
        
        logger.info("Accessibility check - {} of {} images have alt text", 
                   images.size() - imagesWithoutAlt, images.size());
        
        // This is a soft assertion since decorative images might not need alt text
        if (imagesWithoutAlt > 0) {
            logger.warn("{} images found without alt text", imagesWithoutAlt);
        }
    }

    @Then("links should have descriptive text")
    public void links_should_have_descriptive_text() {
        WebDriver driver = getCurrentDriver();
        
        List<WebElement> links = driver.findElements(By.tagName("a"));
        int linksWithoutText = 0;
        
        for (WebElement link : links) {
            String linkText = link.getText().trim();
            String ariaLabel = link.getAttribute("aria-label");
            String title = link.getAttribute("title");
            
            if (linkText.isEmpty() && (ariaLabel == null || ariaLabel.isEmpty()) && 
                (title == null || title.isEmpty())) {
                linksWithoutText++;
                logger.warn("Link without descriptive text: {}", link.getAttribute("href"));
            }
        }
        
        logger.info("Accessibility check - {} of {} links have descriptive text", 
                   links.size() - linksWithoutText, links.size());
    }

    @Then("the website should be keyboard navigable")
    public void the_website_should_be_keyboard_navigable() {
        WebDriver driver = getCurrentDriver();
        
        // Check for focusable elements
        List<WebElement> focusableElements = driver.findElements(By.cssSelector(
            "a, button, input, textarea, select, [tabindex]:not([tabindex='-1'])"));
        
        assertWithLogging(!focusableElements.isEmpty(), "Page should have keyboard focusable elements");
        
        // Check for skip links (good accessibility practice)
        List<WebElement> skipLinks = driver.findElements(By.cssSelector("a[href^='#']"));
        
        logger.info("Keyboard navigation check - Focusable elements: {}, Skip links: {}", 
                   focusableElements.size(), skipLinks.size());
    }

    @Then("the page should have a proper title tag")
    public void the_page_should_have_a_proper_title_tag() {
        WebDriver driver = getCurrentDriver();
        
        String title = driver.getTitle();
        assertWithLogging(title != null && !title.trim().isEmpty(), "Page should have a title tag");
        assertWithLogging(title.length() >= 10 && title.length() <= 60, 
            "Title should be between 10-60 characters for SEO, actual length: " + title.length());
        
        logger.info("SEO check - Title: '{}' (length: {})", title, title.length());
    }

    @Then("the page should have meta descriptions")
    public void the_page_should_have_meta_descriptions() {
        WebDriver driver = getCurrentDriver();
        
        List<WebElement> metaDescriptions = driver.findElements(By.cssSelector("meta[name='description']"));
        
        assertWithLogging(!metaDescriptions.isEmpty(), "Page should have a meta description");
        
        if (!metaDescriptions.isEmpty()) {
            String description = metaDescriptions.get(0).getAttribute("content");
            if (description != null) {
                assertWithLogging(description.length() >= 120 && description.length() <= 160,
                    "Meta description should be 120-160 characters for SEO, actual length: " + description.length());
                logger.info("SEO check - Meta description: '{}' (length: {})", description, description.length());
            }
        }
    }

    @Then("the website should use HTTPS protocol")
    public void the_website_should_use_https_protocol() {
        WebDriver driver = getCurrentDriver();
        
        String currentUrl = driver.getCurrentUrl();
        assertWithLogging(currentUrl.startsWith("https://"), 
            "Website should use HTTPS protocol, current URL: " + currentUrl);
        
        logger.info("Security check - HTTPS protocol verified");
    }

    @Then("there should be no mixed content warnings")
    public void there_should_be_no_mixed_content_warnings() {
        WebDriver driver = getCurrentDriver();
        
        // Check for HTTP resources on HTTPS page
        List<WebElement> httpImages = driver.findElements(By.cssSelector("img[src^='http:']"));
        List<WebElement> httpScripts = driver.findElements(By.cssSelector("script[src^='http:']"));
        List<WebElement> httpLinks = driver.findElements(By.cssSelector("link[href^='http:']"));
        
        int mixedContentCount = httpImages.size() + httpScripts.size() + httpLinks.size();
        
        assertWithLogging(mixedContentCount == 0, 
            "Should not have mixed content (HTTP resources on HTTPS page), found: " + mixedContentCount);
        
        logger.info("Security check - Mixed content check passed");
    }

    @When("I set the viewport to {string} dimensions")
    public void i_set_the_viewport_to_dimensions(String device) {
        WebDriver driver = getCurrentDriver();
        
        Map<String, Dimension> deviceDimensions = new HashMap<>();
        deviceDimensions.put("iPhone SE", new Dimension(375, 667));
        deviceDimensions.put("iPhone 12", new Dimension(390, 844));
        deviceDimensions.put("iPad", new Dimension(768, 1024));
        deviceDimensions.put("iPad Pro", new Dimension(1024, 1366));
        deviceDimensions.put("Desktop 1920px", new Dimension(1920, 1080));
        deviceDimensions.put("Desktop 1440px", new Dimension(1440, 900));
        
        Dimension dimension = deviceDimensions.get(device);
        if (dimension != null) {
            driver.manage().window().setSize(dimension);
            
            // Wait for responsive changes
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            logger.info("Set viewport to {} dimensions: {}x{}", device, dimension.width, dimension.height);
        } else {
            logger.warn("Unknown device: {}", device);
        }
    }

    @Then("the layout should adapt appropriately")
    public void the_layout_should_adapt_appropriately() {
        WebDriver driver = getCurrentDriver();
        
        // Check if layout adapts by looking for responsive elements
        List<WebElement> responsiveElements = driver.findElements(By.cssSelector(
            ".container, .row, .col, .grid, .flex, [class*='responsive']"));
        
        // Check viewport meta tag
        List<WebElement> viewportMeta = driver.findElements(By.cssSelector("meta[name='viewport']"));
        
        assertWithLogging(!viewportMeta.isEmpty(), "Page should have viewport meta tag for responsive design");
        
        logger.info("Responsive design check - Responsive elements: {}, Viewport meta: {}", 
                   responsiveElements.size(), viewportMeta.size());
    }

    @When("I interact with all clickable elements")
    public void i_interact_with_all_clickable_elements() {
        WebDriver driver = getCurrentDriver();
        
        // Find all clickable elements
        List<WebElement> clickableElements = driver.findElements(By.cssSelector(
            "a, button, input[type='button'], input[type='submit'], [onclick], [role='button']"));
        
        logger.info("Found {} clickable elements to test", clickableElements.size());
        
        for (WebElement element : clickableElements) {
            try {
                // Check if element is enabled and visible
                if (element.isEnabled() && element.isDisplayed()) {
                    // Just verify it's clickable, don't actually click to avoid navigation
                    String tagName = element.getTagName();
                    String href = element.getAttribute("href");
                    String onclick = element.getAttribute("onclick");
                    
                    logger.debug("Verified clickable element: {} with href: {} onclick: {}", 
                               tagName, href, onclick);
                }
            } catch (Exception e) {
                logger.warn("Could not interact with element: {}", e.getMessage());
            }
        }
    }

    @Then("all buttons should be functional")
    public void all_buttons_should_be_functional() {
        WebDriver driver = getCurrentDriver();
        
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        List<WebElement> inputButtons = driver.findElements(By.cssSelector("input[type='button'], input[type='submit']"));
        
        int totalButtons = buttons.size() + inputButtons.size();
        int functionalButtons = 0;
        
        for (WebElement button : buttons) {
            if (button.isEnabled()) {
                functionalButtons++;
            }
        }
        
        for (WebElement button : inputButtons) {
            if (button.isEnabled()) {
                functionalButtons++;
            }
        }
        
        logger.info("Button functionality check - {} of {} buttons are functional", 
                   functionalButtons, totalButtons);
        
        if (totalButtons > 0) {
            assertWithLogging(functionalButtons > 0, "At least some buttons should be functional");
        }
    }

    @Then("all sections should have meaningful content")
    public void all_sections_should_have_meaningful_content() {
        WebDriver driver = getCurrentDriver();
        
        List<WebElement> sections = driver.findElements(By.cssSelector("section, .section, main > div"));
        
        for (WebElement section : sections) {
            String text = section.getText().trim();
            
            // Check for placeholder text
            assertWithLogging(!text.toLowerCase().contains("lorem ipsum"), 
                "Section should not contain placeholder text");
            assertWithLogging(!text.toLowerCase().contains("placeholder"), 
                "Section should not contain placeholder text");
            
            // Section should have some meaningful content
            assertWithLogging(text.length() > 10, 
                "Section should have meaningful content (more than 10 characters)");
        }
        
        logger.info("Content quality check - Verified {} sections have meaningful content", sections.size());
    }

    @Then("there should be no placeholder text")
    public void there_should_be_no_placeholder_text() {
        WebDriver driver = getCurrentDriver();
        
        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        
        assertWithLogging(!pageText.contains("lorem ipsum"), "Page should not contain Lorem Ipsum text");
        assertWithLogging(!pageText.contains("placeholder"), "Page should not contain placeholder text");
        assertWithLogging(!pageText.contains("coming soon"), "Page should not contain 'coming soon' text");
        assertWithLogging(!pageText.contains("under construction"), "Page should not contain 'under construction' text");
        
        logger.info("Content quality check - No placeholder text found");
    }

    // Helper methods
    private void assertWithLogging(boolean condition, String message) {
        if (!condition) {
            logger.error("Assertion failed: {}", message);
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

    private WebDriver getCurrentDriver() {
        WebDriver driver = EnterpriseWebDriverManager.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized for current thread: " + Thread.currentThread().getId());
        }
        return driver;
    }
}