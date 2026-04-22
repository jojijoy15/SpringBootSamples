Feature: Retrieve Book Details

  #Scenario 1
  Scenario: Invoke Get Book endpoint with an existing book ID
    When the Get Book endpoint is invoked with book ID 1
    Then the status code of 200 is returned
    And the book details are returned

  #Scenario 2
  Scenario: Invoke Get Book endpoint with a non existing Book ID
    When the Get Book endpoint is invoked with book ID 10
    Then the status code of 404 is returned
    And no book details are returned

  # Example Scenario Outline combining Scenario 1 and Scenario 2
  Scenario Outline: Invoke Get Book endpoint
    When the Get Book endpoint is invoked with book ID <id>
    Then the status code of <status> is returned

    Examples:
      | id | status |
      | 1  | 200    |
      | 10 | 404    |