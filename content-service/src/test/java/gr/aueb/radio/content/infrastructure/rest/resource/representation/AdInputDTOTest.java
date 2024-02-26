package gr.aueb.radio.content.infrastructure.rest.resource.representation;

import gr.aueb.radio.content.infrastructure.rest.representation.AdInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdInputDTOTest {

    private AdInputDTO adInputDTOUnderTest;

    @BeforeEach
    void setUp() {
        adInputDTOUnderTest = new AdInputDTO();
    }

    @Test
    void testToRepresentation() {
        // Setup
        // Run the test
        final AdRepresentation result = adInputDTOUnderTest.toRepresentation();

        // Verify the results
    }
}
