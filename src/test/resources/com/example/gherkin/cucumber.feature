Feature: 
	Verify requests operations from a server

  Scenario: Retrieval users from a server
    Given server is started
    And path is "/users"
    When perform GET operation to retrieve users
    Then all users will be returned
    And server will be stoped
    
  Scenario: Retrieval user 1 from a server
    Given server is started
    And path is "/users"
    And user id is 1
    When perform GET operation to retrieve user by id
    Then user will be returned
    And server will be stoped