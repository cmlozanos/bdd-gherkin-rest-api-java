Feature: 
	Verify requests operations from a server

  Scenario: Retrieval users from a server
    Given server is started
    And path is "/users"
    When perform GET operation to retrieve users
    Then status will be "success"
    And all users will be returned
    And server will be stoped
    
  Scenario Outline: Retrieval user by id from a server
    Given server is started
    And path is "/users"
    And user id is <id>
    When perform GET operation to retrieve user by id
    Then status will be <status>
    And user will be <found>
    And server will be stoped
    
    Examples:
    | id | status      | found     | 
    | 1  | "success"   | found     |
    | 2  | "success"   | found     |
    | 3  | "not found" | not found |