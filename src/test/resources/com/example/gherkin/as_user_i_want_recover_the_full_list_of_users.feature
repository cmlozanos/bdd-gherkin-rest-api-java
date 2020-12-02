Feature: 
	As user I Want recover the full list of users

	Scenario: Retrieve full list of users empty
		Given web service with non users
		When retrieve all users
		Then status will be "no content"
	
	Scenario: Retrieve full list of users filled with 2 users	
			Given web service with users
			 | id | name | dni | email |
			 |1 | user1| 12345678Z | user1@santander.com |
			 |2 | user2| 07509266L | user2@santander.com |
			When retrieve all users
			Then status will be "success"
			And content type will be JSON
			And all users will be returned
			And each user will have id and name and dni and email