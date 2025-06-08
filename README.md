# Enterprise Test Automation Framework

A comprehensive, enterprise-grade test automation framework built with Selenium WebDriver, Cucumber BDD, and TestNG.

## Features

- **Multi-browser Support**: Chrome, Firefox, Edge, Safari
- **Headless/Headed Execution**: Configurable browser modes
- **Environment Management**: Local, Dev, Staging, Production
- **Parallel Execution**: Thread-safe parallel test execution
- **Remote Execution**: Selenium Grid support
- **Comprehensive Reporting**: Allure reports with screenshots and videos
- **Configuration Management**: Environment-specific configurations
- **Enterprise Logging**: Structured logging with SLF4J and Logback

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Chrome/Firefox/Edge browser installed

### Running Tests

#### PowerShell Users (Windows)
Use quotes around the profile parameter:

```powershell
# Local Execution (Headed)
mvn clean verify "-Plocal,headed,chrome"

# Local Execution (Headless)
mvn clean verify "-Plocal,headless,chrome"

# Or use the provided PowerShell script
.\run-tests.ps1
```

#### Command Prompt Users (Windows)
```cmd
# Local Execution (Headed)
mvn clean verify -Plocal,headed,chrome

# Local Execution (Headless)
mvn clean verify -Plocal,headless,chrome

# Or use the provided batch script
run-tests.bat
```

#### Linux/Mac Users
```bash
# Local Execution (Headed)
mvn clean verify -Plocal,headed,chrome

# Local Execution (Headless)
mvn clean verify -Plocal,headless,chrome
```

#### Environment-Specific Execution
```powershell
# Development environment
mvn clean verify "-Pdev,chrome"

# Staging environment
mvn clean verify "-Pstaging,firefox"

# Production environment
mvn clean verify "-Pprod,edge"
```

#### Parallel Execution
```powershell
mvn clean verify "-Pstaging" -Dtest.thread.count=4
```

#### Tag-Based Execution
```powershell
# Run only smoke tests
mvn clean verify "-Plocal,chrome,smoke"

# Run regression tests
mvn clean verify "-Plocal,chrome,regression"
```

## Configuration

### Browser Configuration
Set browser via Maven profiles or system properties:
```powershell
# Via profile
mvn clean verify "-Pchrome"

# Via system property
mvn clean verify -Dtest.browser=firefox
```

### Headless Mode
```powershell
# Via profile
mvn clean verify "-Pheadless"

# Via system property
mvn clean verify -Dtest.headless=true
```

### Environment Configuration
```powershell
# Via profile
mvn clean verify "-Pdev"

# Via system property
mvn clean verify -Dtest.environment=staging
```

## Project Structure

```
src/
├── main/java/dev/marvinmarzon/
│   ├── config/
│   │   ├── BrowserConfig.java
│   │   └── TestEnvironment.java
│   ├── driver/
│   │   └── EnterpriseWebDriverManager.java
│   └── utils/
│       ├── TestConfigManager.java
│       ├── Screenshot.java
│       └── VideoRecorder.java
├── test/java/dev/marvinmarzon/
│   ├── runners/
│   │   └── CucumberTestNGRunner.java
│   ├── stepdefs/
│   │   ├── BaseStepDefinitions.java
│   │   └── SampleStepDefinitions.java
│   └── pageobjects/
└── test/resources/
    ├── features/
    │   └── sample.feature
    ├── testConfig.properties
    ├── testConfig-dev.properties
    ├── testConfig-staging.properties
    └── testConfig-prod.properties
```

## Key Enterprise Features

### 1. Thread-Safe Driver Management
- ThreadLocal WebDriver instances
- Automatic cleanup and resource management
- Support for parallel execution

### 2. Environment-Specific Configuration
- Separate configuration files per environment
- Runtime property overrides
- CI/CD friendly configuration

### 3. Comprehensive Error Handling
- Detailed logging with context
- Graceful failure handling
- Resource cleanup on failures

### 4. Reporting and Monitoring
- Allure reports with rich details
- Screenshot capture on failures
- Video recording support
- Structured logging

### 5. CI/CD Integration
- Maven profiles for different environments
- System property overrides
- Parallel execution support
- Docker-ready configuration

## Advanced Usage

### Custom Configuration
Override any configuration at runtime:
```powershell
mvn clean verify `
  -Dtest.browser=chrome `
  -Dtest.headless=true `
  -Dtest.environment=staging `
  -Dtest.thread.count=4 `
  -Dtest.base.url=https://custom.example.com
```

### Remote Execution (Selenium Grid)
```powershell
mvn clean verify `
  -Dtest.remote=true `
  -Dtest.grid.url=http://selenium-hub:4444/wd/hub `
  -Dtest.headless=true
```

### Generate Allure Reports
```powershell
mvn allure:serve
```

## PowerShell Troubleshooting

### Issue: "Missing argument in parameter list"
**Problem**: PowerShell interprets commas as parameter separators

**Solutions**:
1. **Use quotes around profiles**:
   ```powershell
   mvn clean verify "-Plocal,headed,chrome"
   ```

2. **Use the provided scripts**:
   ```powershell
   .\run-tests.ps1  # Interactive PowerShell script
   .\run-tests.bat  # Batch file alternative
   ```

3. **Use Command Prompt instead**:
   ```cmd
   mvn clean verify -Plocal,headed,chrome
   ```

## Best Practices

1. **Use Page Object Model**: Implement page objects for better maintainability
2. **Environment Separation**: Use appropriate profiles for different environments
3. **Parallel Execution**: Leverage parallel execution for faster feedback
4. **Resource Management**: Always use try-with-resources or proper cleanup
5. **Configuration Management**: Use environment-specific configurations
6. **Logging**: Implement structured logging for better debugging

## Troubleshooting

### Common Issues

1. **Driver Not Found**: Ensure WebDriverManager is properly configured
2. **Timeout Issues**: Adjust timeout configurations in properties files
3. **Parallel Execution Failures**: Check thread safety of test implementations
4. **Environment Issues**: Verify environment-specific configurations
5. **PowerShell Profile Issues**: Use quotes around Maven profiles

### Debug Mode
Enable debug logging:
```powershell
mvn clean verify -Dlogback.configurationFile=logback-debug.xml
```

## Java 21 Compatibility

This framework is fully compatible with Java 21 and includes:
- Proper import statements (no import aliases)
- Modern Java features support
- Enterprise-grade thread safety
- Comprehensive error handling