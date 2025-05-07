package com.example.challenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = ChallengeBackendApplication.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=true",
    "spring.flyway.enabled=false",
    "spring.jpa.properties.hibernate.format_sql=true",
    "spring.main.allow-bean-definition-overriding=true"
})
class ChallengeApplicationTests {

	@Test
	void contextLoads() {
		// Verifies that the Spring application context loads successfully
	}

    @Test
    void mainMethodRuns() {
        assertDoesNotThrow(() -> ChallengeBackendApplication.main(new String[] {}));
    }

}
