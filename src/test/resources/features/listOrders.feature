Feature: List orders feature
  Scenario: I'm trying to list orders on an nonexistent status
    Given I have a few orders on my database
    When I try to find orders on status = NON_EXISTENT
    Then The system should warn me that the status NON_EXISTENT is invalid

  Scenario: I'm trying to list all active orders
    Given I have a few orders on my database
    When I try to find orders without status filter
    Then The system should return all orders whose status aren't on the list
      | COMPLETED |
      | CANCELED  |

  Scenario: I'm trying to list orders filtering by one status
    Given I have a few orders on my database
    When I try to find orders on status = RECEIVED
    Then The system should return all orders on RECEIVED status