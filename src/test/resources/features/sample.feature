Feature: Sample Feature for Enterprise Testing
  As a test automation engineer
  I want to demonstrate enterprise testing patterns
  So that I can ensure robust test execution

  Background:
    Given I am on the home page

  @smoke @regression
  Scenario: Verify home page loads correctly
    Then the page title should contain "Example"
    And I should see "Welcome" on the page

  @regression
  Scenario: Navigate through application
    When I click on the "Get Started" button
    Then I should see "Getting Started" on the page

  @smoke
  Scenario: Verify page elements
    Then I should see "Home" on the page
    And I should see "About" on the page

  @parallel
  Scenario Outline: Test multiple browsers
    When I click on the "<button>" button
    Then I should see "<expected_text>" on the page

    Examples:
      | button      | expected_text |
      | Home        | Welcome       |
      | About       | About Us      |
      | Contact     | Contact       |