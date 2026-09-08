Feature: Idea Functionality
  
Scenario: I can create an idea
  Given an idea of "This is an idea"
  When I create the idea
  Then the idea is in the database


