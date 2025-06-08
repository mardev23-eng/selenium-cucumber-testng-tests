package dev.marvinmarzon.driver;

import dev.marvinmarzon.config.BrowserConfig;
import dev.marvinmarzon.config.TestEnvironment;
import dev.marvinmarzon.utils.TestConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enterprise WebDriver Manager for Cucumber-TestNG with thread-safe driver management
 * Supports local and remote execution with comprehensive configuration
 * Compatible with Java 21
 */
public class EnterpriseWebDriverManager {
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseWebDriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConcurrentHashMap<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
    
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DEFAULT_IMPLICIT_WAIT = Duration.ofSeconds(10);

    private EnterpriseWebDriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize WebDriver based on configuration for Cucumber-TestNG
     */
    public static void initializeDriver() {
        if (getDriver() != null) {
            logger.warn("Driver already initialized for thread: {}", Thread.currentThread().getId());
            return;
        }

        BrowserConfig browserConfig = getBrowserConfig();
        boolean isHeadless = TestConfigManager.getHeadlessMode();
        boolean isRemote = TestConfigManager.isRemoteExecution();

        WebDriver driver;
        
        try {
            if (isRemote) {
                driver = createRemoteDriver(browserConfig, isHeadless);
            } else {
                driver = createLocalDriver(browserConfig, isHeadless);
            }

            configureDriver(driver);
            setDriver(driver);
            
            logger.info("Driver initialized successfully - Browser: {}, Headless: {}, Remote: {}, Thread: {}", 
                       browserConfig.getBrowserName(), isHeadless, isRemote, Thread.currentThread().getId());

        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for thread: {}", Thread.currentThread().getId(), e);
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }

    /**
     * Create local WebDriver instance with enterprise configurations
     */
    private static WebDriver createLocalDriver(BrowserConfig browserConfig, boolean isHeadless) {
        switch (browserConfig) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = createChromeOptions(browserConfig, isHeadless);
                logger.debug("Chrome options: {}", chromeOptions.asMap());
                return new ChromeDriver(chromeOptions);
            
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = createFirefoxOptions(browserConfig, isHeadless);
                logger.debug("Firefox options: {}", firefoxOptions.asMap());
                return new FirefoxDriver(firefoxOptions);
            
            case EDGE:
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = createEdgeOptions(browserConfig, isHeadless);
                logger.debug("Edge options: {}", edgeOptions.asMap());
                return new EdgeDriver(edgeOptions);
            
            case SAFARI:
                logger.info("Initializing Safari driver (headless not supported)");
                return new SafariDriver();
            
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserConfig.getBrowserName());
        }
    }

    /**
     * Create remote WebDriver instance for grid execution
     */
    private static WebDriver createRemoteDriver(BrowserConfig browserConfig, boolean isHeadless) 
            throws MalformedURLException {
        String gridUrl = TestConfigManager.getGridUrl();
        URL hubUrl = new URL(gridUrl);
        logger.info("Creating remote driver for grid: {}", gridUrl);

        switch (browserConfig) {
            case CHROME:
                return new RemoteWebDriver(hubUrl, createChromeOptions(browserConfig, isHeadless));
            
            case FIREFOX:
                return new RemoteWebDriver(hubUrl, createFirefoxOptions(browserConfig, isHeadless));
            
            case EDGE:
                return new RemoteWebDriver(hubUrl, createEdgeOptions(browserConfig, isHeadless));
            
            default:
                throw new IllegalArgumentException("Remote execution not supported for: " + browserConfig.getBrowserName());
        }
    }

    /**
     * Create Chrome options with enterprise configurations
     */
    private static ChromeOptions createChromeOptions(BrowserConfig browserConfig, boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();
        
        // Add default options from config
        browserConfig.getDefaultOptions().forEach(options::addArguments);
        
        // Enterprise-specific options
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        
        // Headless configuration
        if (isHeadless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            logger.info("Chrome configured for headless execution");
        } else {
            logger.info("Chrome configured for headed execution");
        }

        // Performance optimizations
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--remote-allow-origins=*");

        // Set user agent to avoid detection
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        return options;
    }

    /**
     * Create Firefox options with enterprise configurations
     */
    private static FirefoxOptions createFirefoxOptions(BrowserConfig browserConfig, boolean isHeadless) {
        FirefoxOptions options = new FirefoxOptions();
        
        if (isHeadless) {
            options.addArguments("--headless");
            logger.info("Firefox configured for headless execution");
        } else {
            logger.info("Firefox configured for headed execution");
        }
        
        // Performance preferences
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
        
        return options;
    }

    /**
     * Create Edge options with enterprise configurations
     */
    private static EdgeOptions createEdgeOptions(BrowserConfig browserConfig, boolean isHeadless) {
        EdgeOptions options = new EdgeOptions();
        
        // Add default options from config
        browserConfig.getDefaultOptions().forEach(options::addArguments);
        
        if (isHeadless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            logger.info("Edge configured for headless execution");
        } else {
            logger.info("Edge configured for headed execution");
        }
        
        // Enterprise-specific options
        options.addArguments("--disable-web-security");
        options.addArguments("--remote-allow-origins=*");
        
        return options;
    }

    /**
     * Configure driver with timeouts and settings
     */
    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT);
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_TIMEOUT);
        driver.manage().timeouts().scriptTimeout(DEFAULT_TIMEOUT);
        
        if (!TestConfigManager.getHeadlessMode()) {
            driver.manage().window().maximize();
            logger.debug("Browser window maximized");
        }
    }

    /**
     * Get browser configuration from properties
     */
    private static BrowserConfig getBrowserConfig() {
        String browser = TestConfigManager.getBrowser();
        return BrowserConfig.fromString(browser);
    }

    /**
     * Get current WebDriver instance for current thread
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Set WebDriver instance for current thread
     */
    private static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
        driverMap.put(Thread.currentThread().getId(), driver);
    }

    /**
     * Quit WebDriver and clean up resources for current thread
     */
    public static void quitDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully for thread: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                logger.error("Error quitting driver for thread: {}", Thread.currentThread().getId(), e);
            } finally {
                driverThreadLocal.remove();
                driverMap.remove(Thread.currentThread().getId());
            }
        }
    }

    /**
     * Quit all drivers (for cleanup in TestNG hooks)
     */
    public static void quitAllDrivers() {
        logger.info("Quitting all drivers. Active drivers: {}", driverMap.size());
        driverMap.values().forEach(driver -> {
            try {
                if (driver != null) {
                    driver.quit();
                }
            } catch (Exception e) {
                logger.error("Error quitting driver during cleanup", e);
            }
        });
        driverMap.clear();
        driverThreadLocal.remove();
        logger.info("All drivers quit successfully");
    }

    /**
     * Check if driver is initialized for current thread
     */
    public static boolean isDriverInitialized() {
        return getDriver() != null;
    }

    /**
     * Navigate to application URL
     */
    public static void navigateToApplication() {
        TestEnvironment environment = TestEnvironment.fromString(TestConfigManager.getEnvironment());
        String baseUrl = TestConfigManager.getBaseUrl();
        
        // Use custom base URL if provided, otherwise use environment default
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = environment.getBaseUrl();
        }
        
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.get(baseUrl);
            logger.info("Navigated to: {} for thread: {}", baseUrl, Thread.currentThread().getId());
        } else {
            throw new IllegalStateException("Driver not initialized for thread: " + Thread.currentThread().getId());
        }
    }

    /**
     * Get active driver count (for monitoring)
     */
    public static int getActiveDriverCount() {
        return driverMap.size();
    }
}