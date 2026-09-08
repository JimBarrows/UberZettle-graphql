Feature: Idea Functionality

Scenario: I can create an idea
  Given an idea of "This is an idea"
  When I create the idea
  Then the idea is in the database

Scenario: If I create an idea that is empty, I get an error message
  Given an idea of ""
  When I create the idea
  Then the idea is not in the database
  And I have an error message
