Feature: Testing a REST API
  Users should be able to test requests from a web service

  Scenario: Data retrieval from a web service
    When users want to get information on the 'Cucumber' project
    Then the requested data is returned