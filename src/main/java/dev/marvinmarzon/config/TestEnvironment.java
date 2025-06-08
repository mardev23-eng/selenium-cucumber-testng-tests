package dev.marvinmarzon.config;

/**
 * Test environment configuration for enterprise deployments
 */
public enum TestEnvironment {
    LOCAL("http://localhost:3000", "local"),
    DEV("https://dev.example.com", "development"),
    STAGING("https://staging.example.com", "staging"),
    PROD("https://prod.example.com", "production");

    private final String baseUrl;
    private final String environmentName;

    TestEnvironment(String baseUrl, String environmentName) {
        this.baseUrl = baseUrl;
        this.environmentName = environmentName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public static TestEnvironment fromString(String env) {
        for (TestEnvironment environment : values()) {
            if (environment.environmentName.equalsIgnoreCase(env)) {
                return environment;
            }
        }
        return LOCAL;
    }
}