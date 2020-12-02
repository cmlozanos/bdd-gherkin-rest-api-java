Feature: 
	Verify requests of users operations from a web service

	Scenario: Retrieve full list of users empty
		Given web service with non users
		When retrieve all users
		Then status will be "no content"