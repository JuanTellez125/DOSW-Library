package edu.eci.dosw.tdd;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Full integration tests that boot the complete Spring context on a random port.
 * Uses TestRestTemplate to make real HTTP calls through every layer.
 *
 * The in-memory repositories are cleared before each test via @BeforeEach
 * to guarantee test isolation — each test starts with a clean state.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration Tests — Full Spring Context")
class DoswLibraryIntegrationTest {

}
