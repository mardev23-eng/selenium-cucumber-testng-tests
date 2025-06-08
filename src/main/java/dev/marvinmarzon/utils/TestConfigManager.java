package dev.marvinmarzon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Enhanced configuration manager for enterprise Cucumber-TestNG execution
 * Supports environment-specific configurations and runtime overrides
 */
public class TestConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(TestConfigManager.class);
    private static Properties properties;
    private static final String DEFAULT_CONFIG_FILE = "testConfig.properties";
    
    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        properties = new Properties();
        
        // Load default configuration
        loadPropertiesFile(DEFAULT_CONFIG_FILE);
        
        // Load environment-specific configuration if exists
        String environment = getEnvironment();
        String envConfigFile = String.format("testConfig-%s.properties", environment);
        loadPropertiesFile(envConfigFile);
        
        // Override with system properties (for Maven/CI integration)
        overrideWithSystemProperties();
        
        logger.info("Configuration loaded for environment: {}", environment);
        logger.debug("Final configuration properties count: {}", properties.size());
    }

    private static void loadPropertiesFile(String fileName) {
        try (InputStream inputStream = TestConfigManager.class
                .getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                Properties fileProperties = new Properties();
                fileProperties.load(inputStream);
                properties.putAll(fileProperties);
                logger.debug("Loaded {} properties from: {}", fileProperties.size(), fileName);
            } else {
                logger.debug("Configuration file not found: {}", fileName);
            }
        } catch (IOException e) {
            logger.warn("Could not load configuration file: {}", fileName, e);
        }
    }

    private static void overrideWithSystemProperties() {
        int overrideCount = 0;
        // Override with system properties for CI/CD flexibility
        System.getProperties().forEach((key, value) -> {
            String keyStr = key.toString();
            if (keyStr.startsWith("test.")) {
                properties.setProperty(keyStr, value.toString());
                logger.debug("Override property: {} = {}", keyStr, value);
            }
        });
        
        if (overrideCount > 0) {
            logger.info("Applied {} system property overrides", overrideCount);
        }
    }

    // Browser Configuration
    public static String getBrowser() {
        return getProperty("test.browser", "chrome");
    }

    public static boolean getHeadlessMode() {
        return Boolean.parseBoolean(getProperty("test.headless", "false"));
    }

    public static boolean isRemoteExecution() {
        return Boolean.parseBoolean(getProperty("test.remote", "false"));
    }

    public static String getGridUrl() {
        return getProperty("test.grid.url", "http://localhost:4444/wd/hub");
    }

    // Environment Configuration
    public static String getEnvironment() {
        return getProperty("test.environment", "local");
    }

    public static String getBaseUrl() {
        String customUrl = getProperty("test.base.url");
        if (customUrl != null && !customUrl.isEmpty()) {
            return customUrl;
        }
        
        // Fallback to environment-specific URLs
        String environment = getEnvironment();
        switch (environment.toLowerCase()) {
            case "dev":
                return "https://dev.example.com";
            case "staging":
                return "https://staging.example.com";
            case "prod":
                return "https://prod.example.com";
            default:
                return "http://localhost:3000";
        }
    }

    // Timeout Configuration
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("test.timeout.implicit", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("test.timeout.explicit", "30"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("test.timeout.pageload", "60"));
    }

    // Reporting Configuration
    public static boolean isScreenshotEnabled() {
        return Boolean.parseBoolean(getProperty("test.screenshot.enabled", "true"));
    }

    public static boolean isVideoRecordingEnabled() {
        return Boolean.parseBoolean(getProperty("test.video.enabled", "false"));
    }

    public static String getReportPath() {
        return getProperty("test.report.path", "target/reports");
    }

    // Parallel Execution Configuration
    public static int getThreadCount() {
        return Integer.parseInt(getProperty("test.thread.count", "1"));
    }

    public static boolean isParallelExecution() {
        return getThreadCount() > 1;
    }

    // TestNG Configuration
    public static String getTestNGSuite() {
        return getProperty("test.testng.suite", "testng.xml");
    }

    public static String getCucumberTags() {
        return getProperty("test.cucumber.tags", "");
    }

    // Database Configuration (if needed)
    public static String getDatabaseUrl() {
        return getProperty("test.database.url", "");
    }

    public static String getDatabaseUsername() {
        return getProperty("test.database.username", "");
    }

    public static String getDatabasePassword() {
        return getProperty("test.database.password", "");
    }

    // API Configuration
    public static String getApiBaseUrl() {
        return getProperty("test.api.base.url", "");
    }

    public static String getApiKey() {
        return getProperty("test.api.key", "");
    }

    // Utility Methods
    private static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        logger.debug("Property set: {} = {}", key, value);
    }

    public static Properties getAllProperties() {
        return new Properties(properties);
    }

    // Runtime configuration updates
    public static void updateBrowserConfig(String browser, boolean headless) {
        setProperty("test.browser", browser);
        setProperty("test.headless", String.valueOf(headless));
        logger.info("Updated browser configuration: {} (headless: {})", browser, headless);
    }

    public static void updateEnvironmentConfig(String environment, String baseUrl) {
        setProperty("test.environment", environment);
        if (baseUrl != null && !baseUrl.isEmpty()) {
            setProperty("test.base.url", baseUrl);
        }
        logger.info("Updated environment configuration: {} ({})", environment, baseUrl);
    }

    // Validation methods
    public static boolean isValidBrowser(String browser) {
        return browser != null && 
               (browser.equalsIgnoreCase("chrome") || 
                browser.equalsIgnoreCase("firefox") || 
                browser.equalsIgnoreCase("edge") || 
                browser.equalsIgnoreCase("safari"));
    }

    public static boolean isValidEnvironment(String environment) {
        return environment != null && 
               (environment.equalsIgnoreCase("local") || 
                environment.equalsIgnoreCase("dev") || 
                environment.equalsIgnoreCase("staging") || 
                environment.equalsIgnoreCase("prod"));
    }

    // Debug method to log all configuration
    public static void logAllConfiguration() {
        logger.info("=== Current Test Configuration ===");
        properties.entrySet().stream()
            .filter(entry -> entry.getKey().toString().startsWith("test."))
            .forEach(entry -> logger.info("  {} = {}", entry.getKey(), entry.getValue()));
        logger.info("=== End Configuration ===");
    }
}