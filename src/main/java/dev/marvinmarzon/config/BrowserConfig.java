package dev.marvinmarzon.config;

import java.util.Arrays;
import java.util.List;

/**
 * Browser configuration enum for enterprise test execution
 * Supports multiple browser types with standardized configurations
 */
public enum BrowserConfig {
    CHROME("chrome", Arrays.asList(
        "--disable-blink-features=AutomationControlled",
        "--disable-extensions",
        "--no-sandbox",
        "--disable-dev-shm-usage",
        "--remote-allow-origins=*"
    )),
    FIREFOX("firefox", Arrays.asList(
        "--width=1920",
        "--height=1080"
    )),
    EDGE("edge", Arrays.asList(
        "--disable-blink-features=AutomationControlled",
        "--disable-extensions"
    )),
    SAFARI("safari", Arrays.asList());

    private final String browserName;
    private final List<String> defaultOptions;

    BrowserConfig(String browserName, List<String> defaultOptions) {
        this.browserName = browserName;
        this.defaultOptions = defaultOptions;
    }

    public String getBrowserName() {
        return browserName;
    }

    public List<String> getDefaultOptions() {
        return defaultOptions;
    }

    public static BrowserConfig fromString(String browser) {
        return Arrays.stream(values())
            .filter(config -> config.browserName.equalsIgnoreCase(browser))
            .findFirst()
            .orElse(CHROME);
    }
}