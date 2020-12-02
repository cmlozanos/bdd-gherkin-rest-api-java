Feature: 
	Verify requests operations from a server

	Background: Set a background for all tests
		Given server is started

  Scenario: Retrieval users from a server
    Given path is "/users"
    When retrieve all users
    Then status will be "success"
    And all users will be returned
    
  Scenario Outline: Retrieval user by id from a server
    Given path is "/users"
    And user id is <id>
    When retrieve user by id
    Then status will be <status>
    And user will be <found>
    
    Examples:
    | id | status      | found     | 
    | 1  | "success"   | found     |
    | 2  | "success"   | found     |
    | 3  | "not found" | not found |