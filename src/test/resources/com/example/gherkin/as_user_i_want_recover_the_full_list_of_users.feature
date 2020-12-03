Feature: 
	As user I Want recover the full list of users
	
	Rule: Users empty will be response as no content
		Scenario: If I have no users at web service then I should have a No Content response
		Given web service with no users
		When retrieve all users
		Then status will be "no content"
		
	Rule: Users will be showed as a list of user
		Scenario: If list of users are two at web service then I should have a success response as JSON with two users and each user will be filled with id, name, dni and email
			Given web service with users
				 | id | name | dni | email |
				 |1 | user1| 12345678Z | user1@santander.com |
				 |2 | user2| 07509266L | user2@santander.com |
				When retrieve all users
				Then status will be "success"
				And a list of 2 users will be returned
				And each user will have id and name and dni and email
