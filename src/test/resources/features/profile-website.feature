Feature: Marvin Marzon Profile Website Testing
  As a visitor to marvinmarzon.netlify.app
  I want to verify all website functionality works correctly
  So that I can have confidence in the professional presentation

  Background:
    Given I navigate to "https://marvinmarzon.netlify.app"

  @smoke @profile
  Scenario: Verify homepage loads correctly
    Then the page title should contain "Marvin"
    And I should see the main navigation menu
    And I should see the hero section
    And the page should load within 5 seconds

  @smoke @profile
  Scenario: Verify main navigation functionality
    When I click on each navigation menu item
    Then each section should be accessible
    And the navigation should highlight the active section

  @profile @content
  Scenario: Verify hero section content
    Then I should see the profile name "Marvin Marzon"
    And I should see a professional title or tagline
    And I should see a profile image or avatar
    And I should see call-to-action buttons

  @profile @content
  Scenario: Verify about section information
    When I navigate to the about section
    Then I should see professional background information
    And I should see skills or expertise areas
    And I should see educational background
    And the content should be well-formatted and readable

  @profile @content
  Scenario: Verify skills and technologies section
    When I navigate to the skills section
    Then I should see technical skills listed
    And I should see programming languages
    And I should see frameworks and tools
    And skills should be visually organized

  @profile @content
  Scenario: Verify projects or portfolio section
    When I navigate to the projects section
    Then I should see project showcases
    And each project should have a title
    And each project should have a description
    And projects should have technology tags

  @profile @functionality
  Scenario: Verify project links and interactions
    When I navigate to the projects section
    And I click on project links
    Then external links should open in new tabs
    And demo links should be functional
    And GitHub links should be accessible

  @profile @content
  Scenario: Verify experience or work history section
    When I navigate to the experience section
    Then I should see work experience entries
    And each entry should have company information
    And each entry should have role descriptions
    And dates should be properly formatted

  @profile @contact
  Scenario: Verify contact information and social links
    When I navigate to the contact section
    Then I should see contact information
    And I should see social media links
    And social links should be functional
    And email links should work correctly

  @profile @contact
  Scenario: Verify contact form functionality
    When I navigate to the contact section
    And I fill out the contact form with valid information
    And I submit the contact form
    Then I should see a success confirmation
    Or I should see appropriate error handling

  @responsive @mobile
  Scenario: Verify mobile responsiveness
    When I resize the browser to mobile dimensions
    Then the website should adapt to mobile layout
    And navigation should work on mobile
    And content should be readable on small screens
    And images should scale appropriately

  @responsive @tablet
  Scenario: Verify tablet responsiveness
    When I resize the browser to tablet dimensions
    Then the website should adapt to tablet layout
    And all functionality should remain accessible
    And content should be properly organized

  @performance @profile
  Scenario: Verify website performance
    Then the page should load within 3 seconds
    And images should load efficiently
    And there should be no broken links
    And there should be no JavaScript errors

  @accessibility @profile
  Scenario: Verify basic accessibility features
    Then the website should have proper heading structure
    And images should have alt text
    And links should have descriptive text
    And the website should be keyboard navigable

  @seo @profile
  Scenario: Verify SEO elements
    Then the page should have a proper title tag
    And the page should have meta descriptions
    And the page should have proper heading hierarchy
    And the page should have structured data if applicable

  @profile @visual
  Scenario: Verify visual design and branding
    Then the website should have consistent color scheme
    And fonts should be readable and professional
    And spacing and layout should be consistent
    And the overall design should look professional

  @profile @functionality
  Scenario: Verify download functionality
    When I look for downloadable content like resume
    And I click on download links
    Then files should download successfully
    And downloaded files should be valid

  @profile @content
  Scenario: Verify blog or articles section
    When I navigate to the blog section if available
    Then I should see blog posts or articles
    And posts should have proper formatting
    And post navigation should work correctly

  @profile @functionality
  Scenario: Verify search functionality
    When I use the search feature if available
    And I search for relevant terms
    Then search results should be relevant
    And search should handle empty queries gracefully

  @profile @security
  Scenario: Verify HTTPS and security
    Then the website should use HTTPS protocol
    And there should be no mixed content warnings
    And external links should be secure

  @profile @cross-browser
  Scenario Outline: Verify cross-browser compatibility
    When I access the website using "<browser>"
    Then all functionality should work correctly
    And the layout should render properly
    And interactive elements should be functional

    Examples:
      | browser |
      | Chrome  |
      | Firefox |
      | Edge    |

  @profile @error-handling
  Scenario: Verify 404 error handling
    When I navigate to a non-existent page
    Then I should see a custom 404 error page
    And there should be navigation back to main site
    And the error page should maintain site branding