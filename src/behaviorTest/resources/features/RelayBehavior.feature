Feature: Relay specific behavior

  Scenario: Node returns an idea entity
    Given an idea exists in the repo
    When node is called with the idea id
    Then the idea is returned
