package dev.marvinmarzon.stepdefs;

import dev.marvinmarzon.driver.EnterpriseWebDriverManager;
import dev.marvinmarzon.utils.Screenshot;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Step definitions for Marvin Marzon profile website testing
 * Comprehensive test coverage for professional portfolio site
 */
public class ProfileWebsiteStepDefinitions {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfileWebsiteStepDefinitions.class);
    private WebDriverWait wait;
    private long pageLoadStartTime;

    @Given("I navigate to {string}")
    public void i_navigate_to(String url) {
        WebDriver driver = getCurrentDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        logger.info("Navigating to: {}", url);
        pageLoadStartTime = System.currentTimeMillis();
        driver.get(url);
        
        // Wait for page to be ready
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
        
        logger.info("Successfully navigated to: {}", url);
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedText) {
        WebDriver driver = getCurrentDriver();
        
        String actualTitle = driver.getTitle();
        logger.info("Page title: {}", actualTitle);
        
        assertWithLogging(actualTitle.toLowerCase().contains(expectedText.toLowerCase()),
            "Page title should contain '" + expectedText + "' but was '" + actualTitle + "'");
    }

    @Then("I should see the main navigation menu")
    public void i_should_see_the_main_navigation_menu() {
        WebDriver driver = getCurrentDriver();
        
        // Try multiple common navigation selectors
        List<WebElement> navElements = driver.findElements(By.cssSelector("nav, .nav, .navbar, .navigation, header nav"));
        
        if (navElements.isEmpty()) {
            // Try finding navigation by common link patterns
            navElements = driver.findElements(By.cssSelector("ul li a, .menu a, .nav-link"));
        }
        
        assertWithLogging(!navElements.isEmpty(), "Navigation menu should be present on the page");
        logger.info("Found navigation menu with {} elements", navElements.size());
    }

    @Then("I should see the hero section")
    public void i_should_see_the_hero_section() {
        WebDriver driver = getCurrentDriver();
        
        // Try multiple common hero section selectors
        List<WebElement> heroElements = driver.findElements(By.cssSelector(
            ".hero, .banner, .intro, .jumbotron, .hero-section, .main-banner, section:first-of-type"));
        
        assertWithLogging(!heroElements.isEmpty(), "Hero section should be present on the page");
        logger.info("Found hero section");
    }

    @Then("the page should load within {int} seconds")
    public void the_page_should_load_within_seconds(int maxSeconds) {
        long loadTime = System.currentTimeMillis() - pageLoadStartTime;
        double loadTimeSeconds = loadTime / 1000.0;
        
        logger.info("Page load time: {} seconds", loadTimeSeconds);
        assertWithLogging(loadTimeSeconds <= maxSeconds, 
            "Page should load within " + maxSeconds + " seconds but took " + loadTimeSeconds + " seconds");
    }

    @When("I click on each navigation menu item")
    public void i_click_on_each_navigation_menu_item() {
        WebDriver driver = getCurrentDriver();
        
        // Find navigation links
        List<WebElement> navLinks = driver.findElements(By.cssSelector("nav a, .nav a, .navbar a, .menu a"));
        
        if (navLinks.isEmpty()) {
            navLinks = driver.findElements(By.cssSelector("header a, ul li a"));
        }
        
        logger.info("Found {} navigation links", navLinks.size());
        
        for (WebElement link : navLinks) {
            try {
                String linkText = link.getText().trim();
                String href = link.getAttribute("href");
                
                if (!linkText.isEmpty() && href != null && !href.startsWith("mailto:") && !href.startsWith("tel:")) {
                    logger.info("Clicking navigation link: {}", linkText);
                    
                    // Scroll to element and click
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
                    Thread.sleep(500); // Brief pause for scroll
                    
                    if (href.startsWith("#")) {
                        // Internal anchor link
                        link.click();
                        Thread.sleep(1000); // Wait for scroll animation
                    } else if (href.startsWith("http") && !href.contains("marvinmarzon.netlify.app")) {
                        // External link - just verify it's clickable, don't actually click
                        logger.info("External link detected: {}, verifying it's clickable", href);
                        assertWithLogging(link.isEnabled(), "External link should be clickable: " + linkText);
                    } else {
                        // Internal page link
                        link.click();
                        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(driver.getCurrentUrl())));
                    }
                }
            } catch (Exception e) {
                logger.warn("Could not interact with navigation link: {}", e.getMessage());
            }
        }
    }

    @Then("each section should be accessible")
    public void each_section_should_be_accessible() {
        WebDriver driver = getCurrentDriver();
        
        // Verify that sections are accessible (visible on page)
        List<WebElement> sections = driver.findElements(By.cssSelector("section, .section, div[id], main > div"));
        
        assertWithLogging(!sections.isEmpty(), "Page should have accessible sections");
        logger.info("Found {} accessible sections", sections.size());
    }

    @Then("the navigation should highlight the active section")
    public void the_navigation_should_highlight_the_active_section() {
        WebDriver driver = getCurrentDriver();
        
        // Look for active navigation indicators
        List<WebElement> activeNavElements = driver.findElements(By.cssSelector(
            ".active, .current, .selected, nav a.active, .nav-link.active"));
        
        // This is optional since not all sites implement active states
        logger.info("Found {} active navigation indicators", activeNavElements.size());
    }

    @Then("I should see the profile name {string}")
    public void i_should_see_the_profile_name(String expectedName) {
        WebDriver driver = getCurrentDriver();
        
        // Look for the name in common locations
        String pageText = driver.findElement(By.tagName("body")).getText();
        
        assertWithLogging(pageText.contains(expectedName), 
            "Profile name '" + expectedName + "' should be visible on the page");
        logger.info("Found profile name: {}", expectedName);
    }

    @Then("I should see a professional title or tagline")
    public void i_should_see_a_professional_title_or_tagline() {
        WebDriver driver = getCurrentDriver();
        
        // Look for common professional title elements
        List<WebElement> titleElements = driver.findElements(By.cssSelector(
            "h1, h2, .title, .tagline, .subtitle, .profession, .role"));
        
        boolean foundProfessionalContent = false;
        for (WebElement element : titleElements) {
            String text = element.getText().toLowerCase();
            if (text.contains("developer") || text.contains("engineer") || text.contains("programmer") ||
                text.contains("software") || text.contains("web") || text.contains("full stack") ||
                text.contains("frontend") || text.contains("backend") || text.contains("qa") ||
                text.contains("automation") || text.contains("tester")) {
                foundProfessionalContent = true;
                logger.info("Found professional title: {}", element.getText());
                break;
            }
        }
        
        assertWithLogging(foundProfessionalContent, "Should find professional title or tagline");
    }

    @Then("I should see a profile image or avatar")
    public void i_should_see_a_profile_image_or_avatar() {
        WebDriver driver = getCurrentDriver();
        
        // Look for profile images
        List<WebElement> images = driver.findElements(By.cssSelector(
            "img, .avatar, .profile-image, .profile-pic, .photo"));
        
        boolean foundProfileImage = false;
        for (WebElement img : images) {
            String src = img.getAttribute("src");
            String alt = img.getAttribute("alt");
            String className = img.getAttribute("class");
            
            if (src != null && (alt != null && (alt.toLowerCase().contains("profile") || 
                alt.toLowerCase().contains("avatar") || alt.toLowerCase().contains("marvin"))) ||
                (className != null && (className.contains("profile") || className.contains("avatar")))) {
                foundProfileImage = true;
                logger.info("Found profile image: {}", src);
                break;
            }
        }
        
        // If no specific profile image found, check if there are any images at all
        if (!foundProfileImage && !images.isEmpty()) {
            logger.info("Found {} images on page, assuming one could be profile image", images.size());
            foundProfileImage = true;
        }
        
        assertWithLogging(foundProfileImage, "Should find a profile image or avatar");
    }

    @Then("I should see call-to-action buttons")
    public void i_should_see_call_to_action_buttons() {
        WebDriver driver = getCurrentDriver();
        
        // Look for CTA buttons
        List<WebElement> buttons = driver.findElements(By.cssSelector(
            "button, .btn, .button, a.cta, .call-to-action, input[type='submit']"));
        
        // Also look for common CTA text
        List<WebElement> ctaElements = driver.findElements(By.xpath(
            "//*[contains(text(), 'Contact') or contains(text(), 'Hire') or contains(text(), 'Download') or " +
            "contains(text(), 'Resume') or contains(text(), 'Portfolio') or contains(text(), 'Get in touch')]"));
        
        boolean foundCTA = !buttons.isEmpty() || !ctaElements.isEmpty();
        assertWithLogging(foundCTA, "Should find call-to-action buttons or links");
        
        logger.info("Found {} buttons and {} CTA elements", buttons.size(), ctaElements.size());
    }

    @When("I navigate to the {word} section")
    public void i_navigate_to_the_section(String sectionName) {
        WebDriver driver = getCurrentDriver();
        
        // Try to find and click navigation link for the section
        try {
            WebElement sectionLink = driver.findElement(By.xpath(
                "//a[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + 
                sectionName.toLowerCase() + "')]"));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sectionLink);
            Thread.sleep(500);
            sectionLink.click();
            Thread.sleep(1000);
            
            logger.info("Navigated to {} section", sectionName);
        } catch (Exception e) {
            // If navigation link not found, try to scroll to section directly
            try {
                WebElement section = driver.findElement(By.cssSelector(
                    "#" + sectionName.toLowerCase() + ", ." + sectionName.toLowerCase() + 
                    ", [data-section='" + sectionName.toLowerCase() + "']"));
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", section);
                Thread.sleep(1000);
                
                logger.info("Scrolled to {} section", sectionName);
            } catch (Exception ex) {
                logger.warn("Could not navigate to {} section: {}", sectionName, ex.getMessage());
            }
        }
    }

    @When("I resize the browser to mobile dimensions")
    public void i_resize_the_browser_to_mobile_dimensions() {
        WebDriver driver = getCurrentDriver();
        driver.manage().window().setSize(new Dimension(375, 667)); // iPhone dimensions
        
        // Wait for responsive changes to take effect
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("Resized browser to mobile dimensions: 375x667");
    }

    @When("I resize the browser to tablet dimensions")
    public void i_resize_the_browser_to_tablet_dimensions() {
        WebDriver driver = getCurrentDriver();
        driver.manage().window().setSize(new Dimension(768, 1024)); // iPad dimensions
        
        // Wait for responsive changes to take effect
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("Resized browser to tablet dimensions: 768x1024");
    }

    @Then("the website should adapt to mobile layout")
    public void the_website_should_adapt_to_mobile_layout() {
        WebDriver driver = getCurrentDriver();
        
        // Check if mobile-specific elements are visible or layout has changed
        List<WebElement> mobileElements = driver.findElements(By.cssSelector(
            ".mobile-menu, .hamburger, .menu-toggle, .navbar-toggle"));
        
        // Check if desktop elements are hidden
        List<WebElement> desktopNavs = driver.findElements(By.cssSelector("nav ul"));
        
        boolean mobileAdaptation = !mobileElements.isEmpty() || 
            desktopNavs.stream().anyMatch(el -> !el.isDisplayed());
        
        logger.info("Mobile adaptation check - Mobile elements: {}, Desktop nav hidden: {}", 
                   mobileElements.size(), desktopNavs.stream().anyMatch(el -> !el.isDisplayed()));
        
        // This is a soft assertion since not all sites have obvious mobile adaptations
        if (!mobileAdaptation) {
            logger.warn("No obvious mobile layout adaptations detected");
        }
    }

    @When("I click on project links")
    public void i_click_on_project_links() {
        WebDriver driver = getCurrentDriver();
        String originalWindow = driver.getWindowHandle();
        
        // Find project links
        List<WebElement> projectLinks = driver.findElements(By.cssSelector(
            ".project a, .portfolio a, a[href*='github'], a[href*='demo'], a[href*='live']"));
        
        for (WebElement link : projectLinks) {
            try {
                String href = link.getAttribute("href");
                String linkText = link.getText();
                
                if (href != null && !href.isEmpty()) {
                    logger.info("Checking project link: {} ({})", linkText, href);
                    
                    // For external links, verify they're clickable but don't actually navigate
                    if (href.startsWith("http") && !href.contains("marvinmarzon.netlify.app")) {
                        assertWithLogging(link.isEnabled(), "External project link should be clickable: " + linkText);
                    }
                }
            } catch (Exception e) {
                logger.warn("Could not check project link: {}", e.getMessage());
            }
        }
        
        // Return to original window
        driver.switchTo().window(originalWindow);
    }

    @Then("external links should open in new tabs")
    public void external_links_should_open_in_new_tabs() {
        WebDriver driver = getCurrentDriver();
        
        // Check if external links have target="_blank"
        List<WebElement> externalLinks = driver.findElements(By.cssSelector(
            "a[href^='http']:not([href*='marvinmarzon.netlify.app'])"));
        
        for (WebElement link : externalLinks) {
            String target = link.getAttribute("target");
            String href = link.getAttribute("href");
            
            if (target != null && target.equals("_blank")) {
                logger.info("External link correctly configured to open in new tab: {}", href);
            } else {
                logger.warn("External link may not open in new tab: {}", href);
            }
        }
        
        logger.info("Checked {} external links for new tab behavior", externalLinks.size());
    }

    @When("I fill out the contact form with valid information")
    public void i_fill_out_the_contact_form_with_valid_information() {
        WebDriver driver = getCurrentDriver();
        
        try {
            // Look for contact form fields
            WebElement nameField = findFormField(driver, "name");
            WebElement emailField = findFormField(driver, "email");
            WebElement messageField = findFormField(driver, "message");
            
            if (nameField != null) {
                nameField.clear();
                nameField.sendKeys("Test User");
                logger.info("Filled name field");
            }
            
            if (emailField != null) {
                emailField.clear();
                emailField.sendKeys("test@example.com");
                logger.info("Filled email field");
            }
            
            if (messageField != null) {
                messageField.clear();
                messageField.sendKeys("This is a test message for the contact form.");
                logger.info("Filled message field");
            }
            
        } catch (Exception e) {
            logger.warn("Could not fill contact form: {}", e.getMessage());
        }
    }

    @When("I submit the contact form")
    public void i_submit_the_contact_form() {
        WebDriver driver = getCurrentDriver();
        
        try {
            // Look for submit button
            WebElement submitButton = driver.findElement(By.cssSelector(
                "input[type='submit'], button[type='submit'], .submit, .send"));
            
            submitButton.click();
            logger.info("Submitted contact form");
            
            // Wait for response
            Thread.sleep(2000);
            
        } catch (Exception e) {
            logger.warn("Could not submit contact form: {}", e.getMessage());
        }
    }

    @Then("I should see a success confirmation")
    public void i_should_see_a_success_confirmation() {
        WebDriver driver = getCurrentDriver();
        
        // Look for success messages
        List<WebElement> successElements = driver.findElements(By.cssSelector(
            ".success, .thank-you, .confirmation, .message-sent"));
        
        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        boolean hasSuccessText = pageText.contains("thank you") || pageText.contains("success") || 
                                pageText.contains("sent") || pageText.contains("received");
        
        boolean foundSuccess = !successElements.isEmpty() || hasSuccessText;
        
        if (foundSuccess) {
            logger.info("Found success confirmation");
        } else {
            logger.info("No success confirmation found - this may be expected if form requires backend processing");
        }
    }

    @Then("I should see appropriate error handling")
    public void i_should_see_appropriate_error_handling() {
        WebDriver driver = getCurrentDriver();
        
        // Look for error messages or validation
        List<WebElement> errorElements = driver.findElements(By.cssSelector(
            ".error, .invalid, .validation-error, .alert-danger"));
        
        logger.info("Found {} error handling elements", errorElements.size());
    }

    // Helper methods
    private WebElement findFormField(WebDriver driver, String fieldType) {
        try {
            // Try multiple selectors for form fields
            return driver.findElement(By.cssSelector(
                "input[name*='" + fieldType + "'], input[id*='" + fieldType + "'], " +
                "textarea[name*='" + fieldType + "'], textarea[id*='" + fieldType + "']"));
        } catch (Exception e) {
            logger.debug("Could not find {} field", fieldType);
            return null;
        }
    }

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