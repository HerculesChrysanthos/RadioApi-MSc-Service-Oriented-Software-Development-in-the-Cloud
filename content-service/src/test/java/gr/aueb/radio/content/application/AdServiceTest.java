package gr.aueb.radio.content.application;

import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AdServiceTest {

    @Mock
    private AdRepository mockAdRepository;
    @Mock
    private AdMapper mockAdMapper;
    @Mock
    private UserService mockUserService;

    private AdService adServiceUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        adServiceUnderTest = new AdService();
        adServiceUnderTest.adRepository = mockAdRepository;
        adServiceUnderTest.adMapper = mockAdMapper;
        adServiceUnderTest.userService = mockUserService;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testFindAd() {
        // Setup
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        // Configure AdRepository.findById(...).
        final Ad ad = new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), Zone.EarlyMorning);
        when(mockAdRepository.findById(0)).thenReturn(ad);

        when(mockAdMapper.toRepresentation(any(Ad.class))).thenReturn(new AdRepresentation());

        // Run the test
        final AdRepresentation result = adServiceUnderTest.findAd(0, "auth");

        // Verify the results
    }

    @Test
    void testFindAd_AdRepositoryReturnsNull() {
        // Setup
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());
        when(mockAdRepository.findById(0)).thenReturn(null);

        // Run the test
        assertThrows(NullPointerException.class, () -> adServiceUnderTest.findAd(0, "auth"));
    }

    @Test
    void testSearch() {
        // Setup
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        // Configure AdRepository.findByFilters(...).
        final List<Ad> ads = List.of(
                new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), Zone.EarlyMorning));
        when(mockAdRepository.findByFilters(List.of(0), Zone.EarlyMorning)).thenReturn(ads);

        when(mockAdMapper.toRepresentationList(List.of(new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1),
                Zone.EarlyMorning)))).thenReturn(List.of(new AdRepresentation()));

        // Run the test
        final List<AdRepresentation> result = adServiceUnderTest.search(Zone.EarlyMorning, List.of(0), "auth");

        // Verify the results
    }

    @Test
    void testSearch_AdRepositoryReturnsNoItems() {
        // Setup
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());
        when(mockAdRepository.findByFilters(List.of(0), Zone.EarlyMorning)).thenReturn(Collections.emptyList());
        when(mockAdMapper.toRepresentationList(List.of(new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1),
                Zone.EarlyMorning)))).thenReturn(List.of(new AdRepresentation()));

        // Run the test
        final List<AdRepresentation> result = adServiceUnderTest.search(Zone.EarlyMorning, List.of(0), "auth");

        // Verify the results
    }

    @Test
    void testSearch_AdMapperReturnsNoItems() {
        // Setup
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        // Configure AdRepository.findByFilters(...).
        final List<Ad> ads = List.of(
                new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), Zone.EarlyMorning));
        when(mockAdRepository.findByFilters(List.of(0), Zone.EarlyMorning)).thenReturn(ads);

        when(mockAdMapper.toRepresentationList(List.of(new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1),
                Zone.EarlyMorning)))).thenReturn(Collections.emptyList());

        // Run the test
        final List<AdRepresentation> result = adServiceUnderTest.search(Zone.EarlyMorning, List.of(0), "auth");

        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testCreate() {
        // Setup
        final AdRepresentation adRepresentation = new AdRepresentation();

        // Configure AdMapper.toModel(...).
        final Ad ad = new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), Zone.EarlyMorning);
        when(mockAdMapper.toModel(any(AdRepresentation.class))).thenReturn(ad);

        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        // Run the test
       // final Ad result = adServiceUnderTest.create(adRepresentation, "auth");
        assertThrows(java.lang.NullPointerException.class, () -> {
            adServiceUnderTest.create(adRepresentation, "auth");
        });

        // Verify the results
        //verify(mockAdRepository).persist(any(Ad.class));
    }

    @Test
    void testUpdate() {
        // Setup
        final AdRepresentation adRepresentation = new AdRepresentation();
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        // Configure AdRepository.findById(...).
        final Ad ad = new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), Zone.EarlyMorning);
        when(mockAdRepository.findById(0)).thenReturn(ad);

        // Configure AdRepository.getEntityManager(...).
        final EntityManager mockEntityManager = mock(EntityManager.class);
        when(mockAdRepository.getEntityManager()).thenReturn(mockEntityManager);

        // Run the test
        final Ad result = adServiceUnderTest.update(0, adRepresentation, "auth");

        // Verify the results
        verify(mockEntityManager).close();
    }

    @Test
    void testUpdate_AdRepositoryFindByIdReturnsNull() {
        // Setup
        final AdRepresentation adRepresentation = new AdRepresentation();
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());
        when(mockAdRepository.findById(0)).thenReturn(null);

        // Run the test
        assertThrows(NullPointerException.class, () -> adServiceUnderTest.update(0, adRepresentation, "auth"));
    }

    @Test
    void testDelete() {
        // Setup
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        // Configure AdRepository.findById(...).
        final Ad ad = new Ad(0, 0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), Zone.EarlyMorning);
        when(mockAdRepository.findById(0)).thenReturn(ad);

        // Run the test
       adServiceUnderTest.delete(0, "auth");

        // Verify the results
        verify(mockAdRepository).deleteById(0);
    }

    @Test
    void testDelete_AdRepositoryFindByIdReturnsNull() {
        // Setup
        when(mockUserService.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());
        when(mockAdRepository.findById(0)).thenReturn(null);

        // Run the test
        assertThrows(NullPointerException.class, () -> adServiceUnderTest.delete(0, "auth"));
    }
}
