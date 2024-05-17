Feature: Update order status
  Scenario: I'm trying to update the status from a order that doesn't exist on the database
    Given I'm trying to update the status of an order with id = 443
    When I try to change it's status to CANCELED
    Then The system should warn me that there's no such order

  Scenario: I'm trying to update the status from an existing order, but passing an invalid status
    Given I'm trying to update the status of an order with id = 1
    When I try to change it's status to NON_EXISTING
    Then The system should warn me that the status is invalid

  Scenario: I'm trying to update the status from an existing order and I'm passing a valid status
    Given I'm trying to update the status of an order with id = 1
    When I try to change it's status to CANCELED
    Then I should receive the order with it's status set to CANCELED