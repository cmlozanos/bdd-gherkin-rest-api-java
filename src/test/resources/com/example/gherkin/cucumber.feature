Feature: 
	Verify requests operations from a server

  Scenario: Retrieval users from a server
    Given server is started
    When perform GET operation to retrieve users from "/users"
    Then all users will be returned
    And server will be stoped