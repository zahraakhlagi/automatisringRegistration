Feature: User registration



  Scenario: create a account , everything is valid and an account is created
    Given I am on the registration page
    When I select date of birth "2000-02-01"
    And I fill in valid registration field
    And I accept the Terms and Conditions
    And I accept the I am over 18 or parently responsibility
    And I accept that I have read
    And I submit the registration form
    Then I should see the account creation success message


  Scenario: Create a user account when lastname is missing
    Given I am on the registration page
    When I select date of birth "2000-02-01"
    And I fill in all required fields except lastname
    And I accept the Terms and Conditions
    And I accept the I am over 18 or parently responsibility
    And I accept that I have read
    And I submit the registration form
    Then I should see error message "Last Name is required"


  Scenario: Create a user account when password miss matcher
    Given I am on the registration page
    When I select date of birth "2000-02-01"
    And I fill in all required fields with mismatch password
    And I accept the Terms and Conditions
    And I accept the I am over 18 or parently responsibility
    And I accept that I have read
    And I submit the registration form
    Then I should see error message for password "Password did not match"

  Scenario: Create user – terms and conditions are not approved
    Given I am on the registration page
    When I select date of birth "2000-02-01"
    And I fill in valid registration field
    And I don Not accept the Terms and Conditions
    And I accept the I am over 18 or parently responsibility
    And I accept that I have read
    And I submit the registration form
    Then I should see error message for Terms and Conditions "You must confirm that you have read and accepted our Terms and Conditions"

  @outline
  Scenario Outline: create user- validation errors
    Given I am on the registration page in "<browser>"
    When I select date of birth "<dob>"
    And I fill in valid registration field with firstName "<firstName>" lastname "<lastName>" email "<email>" confirmEmail "<confirmEmail>" password "<password>" confirmPassword "<confirmPassword>"
    And I accept the Terms and Conditions to "<acceptTerms>"
    And I submit the registration form
    Then I should see the field error for "<errorFor>" with message "<errorMessage>"

    Examples:
      | browser | dob        | firstName | lastName | email             | confirmEmail    | password    | confirmPassword | acceptTerms | errorFor                          |  errorMessage             |
      | chrome  | 2000-02-01 | zahra     |          | test1@gmail.com   | test1@gmail.com | Test123     | Test123         | true        | member_lastname                   |  Last Name is required    |
      | edge    | 2000-02-01 | zahra     |          | test1@gmail.com   | test1@gmail.com | Test123     | Test123         | true        | member_lastname                   |  Last Name is required   |

