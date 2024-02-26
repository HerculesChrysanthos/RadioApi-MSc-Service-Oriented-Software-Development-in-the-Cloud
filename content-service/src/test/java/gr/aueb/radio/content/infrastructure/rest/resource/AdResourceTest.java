package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.application.AdService;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.rest.representation.AdInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AdResourceTest {

    @Mock
    private UriInfo mockUriInfo;
    @Mock
    private AdMapper mockAdMapper;
    @Mock
    private AdService mockAdService;

    private AdResource adResourceUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        adResourceUnderTest = new AdResource();
        adResourceUnderTest.uriInfo = mockUriInfo;
        adResourceUnderTest.adMapper = mockAdMapper;
        adResourceUnderTest.adService = mockAdService;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testGetAd() {
        // Setup
        when(mockAdService.findAd(0, "auth")).thenReturn(new AdRepresentation());

        // Run the test
        final Response result = adResourceUnderTest.getAd(0, "auth");

        // Verify the results
    }

    @Test
    void testSearch() {
        // Setup
        when(mockAdService.search(Zone.EarlyMorning, List.of(0), "auth")).thenReturn(List.of(new AdRepresentation()));

        // Run the test
        final Response result = adResourceUnderTest.search(Zone.EarlyMorning, "adsIds", "auth");

        // Verify the results
    }

    @Test
    void testSearch_AdServiceReturnsNoItems() {
        // Setup
        when(mockAdService.search(Zone.EarlyMorning, List.of(0), "auth")).thenReturn(Collections.emptyList());

        // Run the test
        final Response result = adResourceUnderTest.search(Zone.EarlyMorning, "adsIds", "auth");

        // Verify the results
    }

    @Test
    void testCreateAd() {
        // Setup
        final AdInputDTO adRepresentation = new AdInputDTO();

        // Configure AdService.create(...).
        final Ad ad = new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), Zone.EarlyMorning);
        when(mockAdService.create(any(AdRepresentation.class), eq("auth"))).thenReturn(ad);

        when(mockAdMapper.toRepresentation(any(Ad.class))).thenReturn(new AdRepresentation());

        // Run the test
        final Response result = adResourceUnderTest.createAd(adRepresentation, "auth");

        // Verify the results
    }

    @Test
    void testUpdateAd() {
        // Setup
        final AdInputDTO adRepresentation = new AdInputDTO();

        // Run the test
        final Response result = adResourceUnderTest.updateAd(0, adRepresentation, "auth");

        // Verify the results
        verify(mockAdService).update(eq(0), any(AdRepresentation.class), eq("auth"));
    }

    @Test
    void testDeleteAd() {
        // Setup
        // Run the test
        final Response result = adResourceUnderTest.deleteAd(0, "auth");

        // Verify the results
        verify(mockAdService).delete(0, "auth");
    }
}
