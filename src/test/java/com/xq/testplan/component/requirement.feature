Feature: Requirement API Tests

  Background:
    * url baseUrl
    * def resourcePath = 'api/requirement'

  @progression
  Scenario: Create a requirement
    * def title = randomTitle()
    Given path resourcePath + '/create'
    And request { title: '#(title)', description: 'Test Description', scopes: 'scope1,scope2', tags: 'tag1,tag2', references: 'http://example.com' }
    When method post
    Then status 201
    And match response == { statusCode: '201', statusMsg: 'Requirement created', uuid: '#notnull' }

    # Verify the created requirement
    Given path resourcePath + '/' + response.uuid
    When method get
    Then status 200
    And match response.title == title

  @progression
  Scenario: Update a requirement
    # First, create a requirement
    Given path resourcePath + '/create'
    And request { title: '#(randomTitle())', description: 'Desc', scopes: 'scope1', tags: 'tag1', references: 'ref1' }
    When method post
    Then status 201
    * def uuid = response.uuid
    # Update the requirement
    * def updatedTitle = randomStringPrefix(10, "Updated")
    Given path resourcePath + '/update'
    And param uuid = uuid
    And request { title: '#(updatedTitle)', description: 'Updated Desc', scopes: 'scope2', tags: 'tag2', references: 'ref2' }
    When method put
    Then status 200
    And match response.statusCode == '200'
    # Fetch and verify update
    Given path resourcePath + '/' + uuid
    When method get
    Then status 200
    And match response.title == updatedTitle

  @progression
  Scenario: Get all requirements
    Given path resourcePath + '/all'
    When method get
    Then status 200
    And assert response.total >= 0
    And match response.requirements == '#[]'

  @progression
  Scenario: Delete a requirement
    # Create a requirement to delete
    Given path resourcePath + '/create'
    And request { title: 'To Delete', description: 'Desc', scopes: 'scope1', tags: 'tag1', references: 'ref1' }
    When method post
    Then status 201
    * def uuid = response.uuid
    # Delete the requirement
    Given path resourcePath + '/delete'
    And param uuid = uuid
    When method delete
    Then status 200
    And match response.statusCode == '200'
    # Verify deletion
    Given path resourcePath + '/' + uuid
    When method get
    Then status 404

  @progression
  Scenario: Delete all requirements
    Given path resourcePath + '/delete/all'
    When method delete
    Then status 200
    And match response.statusCode == '200'

  @negative
  Scenario: Update with invalid UUID
    Given path resourcePath + '/update'
    And param uuid = 'invalid-uuid'
    And request { title: 'X', description: 'X', scopes: 'X', tags: 'X', references: 'X' }
    When method put
    Then status 417
    And match response.errorCode == '417 EXPECTATION_FAILED'

  @negative
  Scenario: Delete with invalid UUID
    Given path resourcePath + '/delete'
    And param uuid = 'invalid-uuid'
    When method delete
    Then status 417
    And match response.errorCode == '417 EXPECTATION_FAILED'

  @negative
  Scenario: Create with missing fields
    Given path resourcePath + '/create'
    And request { title: '', description: '', scopes: '', tags: '', references: '' }
    When method post
    Then status 400

