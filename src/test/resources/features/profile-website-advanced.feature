Feature: Advanced Profile Website Testing
  As a QA engineer testing marvinmarzon.netlify.app
  I want to perform comprehensive testing scenarios
  So that I can ensure the website meets professional standards

  Background:
    Given I navigate to "https://marvinmarzon.netlify.app"

  @advanced @performance
  Scenario: Verify website performance metrics
    Then the page should load within 3 seconds
    And images should load efficiently
    And there should be no console errors
    And the page should be optimized for performance

  @advanced @accessibility
  Scenario: Verify accessibility compliance
    Then the website should have proper heading structure
    And images should have alt text
    And links should have descriptive text
    And the website should be keyboard navigable
    And color contrast should meet WCAG guidelines

  @advanced @seo
  Scenario: Verify SEO optimization
    Then the page should have a proper title tag
    And the page should have meta descriptions
    And the page should have proper heading hierarchy
    And the page should have Open Graph tags
    And the page should have structured data

  @advanced @security
  Scenario: Verify security best practices
    Then the website should use HTTPS protocol
    And there should be no mixed content warnings
    And external links should have proper security attributes
    And the website should have security headers

  @advanced @functionality
  Scenario: Verify interactive elements
    When I interact with all clickable elements
    Then all buttons should be functional
    And all links should work correctly
    And hover effects should work properly
    And animations should be smooth

  @advanced @content
  Scenario: Verify content quality and completeness
    Then all sections should have meaningful content
    And there should be no placeholder text
    And all images should load properly
    And text should be properly formatted
    And contact information should be accurate

  @advanced @responsive
  Scenario Outline: Verify responsive design across devices
    When I set the viewport to "<device>" dimensions
    Then the layout should adapt appropriately
    And all content should remain accessible
    And navigation should work correctly
    And text should remain readable

    Examples:
      | device          |
      | iPhone SE       |
      | iPhone 12       |
      | iPad            |
      | iPad Pro        |
      | Desktop 1920px  |
      | Desktop 1440px  |

  @advanced @cross-browser
  Scenario: Verify cross-browser compatibility
    Then the website should render correctly in all browsers
    And JavaScript functionality should work consistently
    And CSS styles should be applied correctly
    And fonts should load properly

  @advanced @error-handling
  Scenario: Verify error handling and edge cases
    When I test various error scenarios
    Then the website should handle errors gracefully
    And there should be appropriate fallbacks
    And user experience should remain positive

  @advanced @analytics
  Scenario: Verify analytics and tracking
    Then analytics tracking should be properly implemented
    And privacy policies should be accessible
    And cookie consent should be handled appropriately

  @advanced @social
  Scenario: Verify social media integration
    When I check social media links and sharing
    Then social links should be functional
    And social sharing should work correctly
    And social meta tags should be present

  @advanced @portfolio
  Scenario: Verify portfolio showcase effectiveness
    When I review the portfolio section
    Then projects should be well-presented
    And project descriptions should be clear
    And technology stacks should be highlighted
    And project links should be functional

  @advanced @contact
  Scenario: Verify contact and networking opportunities
    When I review contact options
    Then multiple contact methods should be available
    And professional networking links should work
    And contact information should be up-to-date
    And response expectations should be clear

  @advanced @branding
  Scenario: Verify personal branding consistency
    Then the website should reflect professional branding
    And color scheme should be consistent
    And typography should be professional
    And overall design should be cohesive
    And messaging should be clear and compelling