# Profile Website Test Suite

Comprehensive test automation suite for **marvinmarzon.netlify.app** using Selenium WebDriver, Cucumber BDD, and TestNG.

## Test Coverage

### 🚀 Smoke Tests (`@smoke @profile`)
- Homepage loading verification
- Main navigation functionality
- Hero section presence
- Basic page structure

### 📝 Content Tests (`@content @profile`)
- Profile name and professional title
- About section information
- Skills and technologies display
- Projects/portfolio showcase
- Experience/work history
- Contact information accuracy

### ⚡ Functionality Tests (`@functionality @profile`)
- Navigation menu interactions
- Project links and external references
- Contact form submission
- Download functionality (resume, etc.)
- Search functionality (if available)

### 📱 Responsive Tests (`@responsive @profile`)
- Mobile device compatibility (iPhone SE, iPhone 12)
- Tablet compatibility (iPad, iPad Pro)
- Desktop compatibility (1440px, 1920px)
- Layout adaptation verification

### 🔍 Advanced Tests (`@advanced @profile`)
- **Performance**: Page load times, image optimization
- **SEO**: Meta tags, heading structure, Open Graph
- **Accessibility**: WCAG compliance, keyboard navigation
- **Security**: HTTPS, mixed content, security headers
- **Cross-browser**: Chrome, Firefox, Edge compatibility

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Chrome browser installed

### Running Profile Tests

#### PowerShell (Recommended for Windows)
```powershell
# Quick smoke test
.\run-profile-tests.ps1

# Or run specific test types
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@smoke and @profile"
```

#### Command Prompt (Windows)
```cmd
# Quick smoke test
run-profile-tests.bat

# Or run specific test types
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@smoke and @profile"
```

#### Linux/Mac
```bash
# Run smoke tests
mvn clean verify -Plocal,chrome -Dtest.cucumber.tags="@smoke and @profile"
```

## Test Execution Options

### 1. Smoke Tests (Fastest - ~2 minutes)
```powershell
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@smoke and @profile"
```
Verifies basic functionality and page loading.

### 2. Content Tests (~5 minutes)
```powershell
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@content and @profile"
```
Validates all content sections and information accuracy.

### 3. Functionality Tests (~7 minutes)
```powershell
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@functionality and @profile"
```
Tests interactive elements and user workflows.

### 4. Responsive Tests (~10 minutes)
```powershell
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@responsive and @profile"
```
Verifies mobile, tablet, and desktop compatibility.

### 5. Advanced Tests (~15 minutes)
```powershell
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@advanced and @profile"
```
Comprehensive performance, SEO, accessibility, and security testing.

### 6. Full Test Suite (~20 minutes)
```powershell
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@profile"
```
Runs all profile website tests.

## Test Configuration

### Profile-Specific Settings
```properties
# testConfig-profile.properties
test.profile.url=https://marvinmarzon.netlify.app
test.profile.name=Marvin Marzon
test.performance.page.load.max=5
test.accessibility.wcag.level=AA
```

### Browser Options
```powershell
# Chrome (default)
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@profile"

# Firefox
mvn clean verify "-Plocal,firefox" "-Dtest.cucumber.tags=@profile"

# Headless mode
mvn clean verify "-Plocal,headless,chrome" "-Dtest.cucumber.tags=@profile"
```

## Test Reports

After test execution, reports are available at:

- **HTML Report**: `target/reports/cucumber-html-report/index.html`
- **JSON Report**: `target/reports/cucumber-json-report.json`
- **Allure Report**: Run `mvn allure:serve` to view interactive report
- **Screenshots**: `target/screenshots/` (on failures)

## Test Scenarios

### Core Functionality
- ✅ Homepage loads within 3 seconds
- ✅ Navigation menu is present and functional
- ✅ Profile name "Marvin Marzon" is displayed
- ✅ Professional title/tagline is visible
- ✅ Contact information is accessible

### Content Verification
- ✅ About section has meaningful content
- ✅ Skills/technologies are properly displayed
- ✅ Projects showcase is comprehensive
- ✅ Experience/work history is detailed
- ✅ No placeholder text (Lorem Ipsum)

### Technical Quality
- ✅ HTTPS protocol is used
- ✅ No console errors
- ✅ Images have alt text
- ✅ Proper heading structure (H1, H2, H3)
- ✅ Meta descriptions are present

### Responsive Design
- ✅ Mobile layout (375px width)
- ✅ Tablet layout (768px width)
- ✅ Desktop layout (1920px width)
- ✅ Content remains accessible across devices

### Performance
- ✅ Page load time < 5 seconds
- ✅ Images load efficiently
- ✅ No mixed content warnings
- ✅ Optimized for performance

## Troubleshooting

### Common Issues

1. **Test fails on navigation**: Ensure the website is accessible and navigation elements are present
2. **Performance tests fail**: Check internet connection and website performance
3. **Responsive tests fail**: Verify the website has responsive design implementation
4. **Content tests fail**: Update expected content in test configuration

### Debug Mode
```powershell
mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@smoke and @profile" -X
```

### Screenshots on Failure
Screenshots are automatically captured on test failures and saved to `target/screenshots/`.

## Continuous Integration

### GitHub Actions Example
```yaml
name: Profile Website Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '21'
      - run: mvn clean verify -Plocal,headless,chrome -Dtest.cucumber.tags="@smoke and @profile"
```

## Best Practices

1. **Run smoke tests frequently** for quick feedback
2. **Use headless mode in CI/CD** for faster execution
3. **Review reports** after each test run
4. **Update test data** when website content changes
5. **Monitor performance metrics** over time

## Contributing

When adding new tests for the profile website:

1. Use appropriate tags (`@profile` + specific category)
2. Follow the existing step definition patterns
3. Add meaningful assertions with proper logging
4. Update this README with new test scenarios
5. Ensure tests are independent and can run in parallel

## Support

For issues with the test suite:
1. Check the console output for detailed error messages
2. Review the HTML report for test execution details
3. Examine screenshots for visual verification
4. Run tests in debug mode for additional logging