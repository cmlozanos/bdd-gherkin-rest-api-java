Feature: 
	As user I Want recover the full list of users
	
	Rule: Users empty will be response as no content
		Scenario: If I have no users at web service then I should have a No Content response
			Given web service with no users
			When retrieve all users
			Then status will be "no content"
	
	Rule: When I have only two users at web service then should I have a full response with two users
		Scenario: If list of users are two at web service then I should have a success response as JSON with two users and each user will be filled with id, name, dni and email	
				Given web service with users
				 | id | name | dni | email |
				 |1 | user1| 12345678Z | user1@santander.com |
				 |2 | user2| 07509266L | user2@santander.com |
				When retrieve all users
				Then status will be "success"
				And a list of 2 users will be returned
				And each user will have id and name and dni and email
			
	Rule: Users will be paged by size of 5
		Scenario: If list of users is bigger than 5 the content will be paged
		
		Given web service with users
		 | id | name | dni | email |
		 |1 | user1| 12345678Z | user1@santander.com |
		 |2 | user2| 07509266L | user2@santander.com |
		 |3 | user3| 54041218G | user3@santander.com |
		 |4 | user4| 96513250Y | user4@santander.com |
		 |5 | user5| 31348443H | user5@santander.com |
		 |6 | user6| 56839652M | user6@santander.com |
		 |7 | user7| 29231553W | user7@santander.com |
		 |8 | user8| 17783156Q | user8@santander.com |
		 |9 | user9| 53124448Z | user9@santander.com |
		When retrieve all users from page 1
		Then status will be "success"
		And a list of 5 users paginated will be returned
		And pagination will have 2 pages
		And page will be 1
		And each user will have id and name and dni and email