Feature: Receive order after payment
  Scenario: I'm receiving an order after it has been paid
    Given The order has been paid
    When I receive the order on my system
    Then The order should be registered and it's status should be RECEIVED