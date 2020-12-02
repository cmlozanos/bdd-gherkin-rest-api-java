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
    Then status will be "success"
    And user will be returned
    And server will be stoped
    
    Examples:
    | id |
    | 1  |
    | 2  |