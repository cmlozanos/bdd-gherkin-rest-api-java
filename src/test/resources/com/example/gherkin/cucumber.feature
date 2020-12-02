Feature: 
	Verify requests operations from a server

	Background: Set a background for all tests
		Given users is
		 |1 | user1|
		 |2 | user2|
		And server is started
		 
  Scenario: Retrieval users from a server
    When retrieve all users
    Then status will be "success"
    And all users will be returned
    
  Scenario Outline: Retrieval user by id from a server
    Given user id is <id>
    When retrieve user by id
    Then status will be <status>
    And user will be <found>
    
    Examples:
    | id | status      | found     | 
    | 1  | "success"   | found     |
    | 2  | "success"   | found     |
    | 3  | "not found" | not found |
    
  Scenario: Add user to users on a server
    Given User with id 10 name "user10"
    When add new user
    Then status will be "created"
    And user id will be returned