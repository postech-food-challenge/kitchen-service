Feature: Update order status
  Scenario: I'm trying to update the status from a order that doesn't exist on the database
    Given I'm trying to update the status of a nonexistent order with id = c9cb4304-9173-4d85-aafb-978a4b4ca7ea
    When I try to change it's status to CANCELED
    Then The system should warn me that there's no such order

  Scenario: I'm trying to update the status from an existing order, but passing an invalid status
    Given I created an order and I'm trying to update it's status
    When I try to change it's status to COMPLETED
    Then I should receive the order with it's status set to COMPLETED

  Scenario: I'm trying to update the status from an existing order and I'm passing a valid status
    Given I created an order and I'm trying to update it's status
    When I try to change it's status to CANCELED
    Then I should receive the order with it's status set to CANCELED
#
#  Scenario: I'm trying to update the status from an existing order and I'm passing a valid status
#    Given I'm trying to update the status of an existing order with id = c9cb4304-9173-4d85-aafb-978a4b4ca7ea
#    When I try to change it's status to CANCELED
#    Then I should receive an error stating that the id I passed is invalid