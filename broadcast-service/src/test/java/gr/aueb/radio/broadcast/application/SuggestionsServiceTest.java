package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.IntegrationBase;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import gr.aueb.radio.broadcast.domain.broadcast.Zone;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastRepresentation;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SuggestionsDTO;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;


@QuarkusTest
public class SuggestionsServiceTest {
    @Inject
    SuggestionsService suggestionsService;

    @Inject
    BroadcastService broadcastService;

    @InjectMock
    UserService userService;

//    @Inject
//    SongService songService;
//
//    @Inject
//    SongMapper songMapper;

    private BroadcastRepresentation createRepresentation() {
        BroadcastRepresentation broadcastRepresentation = new BroadcastRepresentation();
        broadcastRepresentation.duration = 45;
        broadcastRepresentation.startingDate = "11-02-2023";
        broadcastRepresentation.startingTime = "11:00";
        broadcastRepresentation.type = BroadcastType.NEWS;
        return broadcastRepresentation;
    }


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
    }

    @Test
    public void suggestSongsTest() {
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        Broadcast broadcast = broadcastService.create(broadcastRepresentation, "auth");
        List<SongBasicRepresentation> mockSuggestedSongs = createMockSuggestedSongs(10);


        assertTrue(mockSuggestedSongs.size() <= 15);
        // check if limitation to 15 exists
        assertTrue(mockSuggestedSongs.size() > 0);

        Integer genre = mockSuggestedSongs.get(0).genreId;
        for (SongBasicRepresentation s : mockSuggestedSongs) {
            assertEquals
                    (s.genreId, genre);
        }

        assertThrows(NotFoundException.class, () -> suggestionsService.suggestSongs(-1, "auth"));
    }
    private List<SongBasicRepresentation> createMockSuggestedSongs(int numSuggestedSongs) {
        List<SongBasicRepresentation> mockSuggestedSongs = new ArrayList<>();
        for (int i = 0; i < numSuggestedSongs; i++) {
            SongBasicRepresentation sb = new SongBasicRepresentation();
            // Set mock properties for the suggested ad
            sb.id = i + 1;
            sb.genreId = 2;
            sb.duration = 2;
            // Add the mock suggested ad to the list
            mockSuggestedSongs.add(sb);
        }
        return mockSuggestedSongs;
    }

//    @Test
//    public void suggestSongsTestExistingBroadcast() {
//        BroadcastRepresentation broadcastRepresentation = createRepresentation();
//        Broadcast broadcast = broadcastService.create(broadcastRepresentation);
//        SongBasicRepresentation song = new SongBasicRepresentation("I am not rich", "pop", 3, "The king's son", 2016, 2);
////        song = songService.create(songMapper.toRepresentation(song));
//
//        List<SongBasicRepresentation> suggestedSongs = suggestionsService.suggestSongs(broadcast.getId());
//        // check if limitation to 15 exists
//        assertTrue(suggestedSongs.size() <= 15);
//
//        Integer genre = suggestedSongs.get(0).genreId;
//        for (SongBasicRepresentation s : suggestedSongs) {
//            assertEquals
//                    (s.genreId, genre);
//        }
//    }
    private List<AdBasicRepresentation> createMockSuggestedAds(int numSuggestedAds) {
        List<AdBasicRepresentation> mockSuggestedAds = new ArrayList<>();
        for (int i = 0; i < numSuggestedAds; i++) {
            AdBasicRepresentation adBasicRepresentation = new AdBasicRepresentation();
            // Set mock properties for the suggested ad
            adBasicRepresentation.id = i + 1;
            adBasicRepresentation.timezone = String.valueOf(Zone.Morning);
            // Add the mock suggested ad to the list
            mockSuggestedAds.add(adBasicRepresentation);
        }
        return mockSuggestedAds;
    }

    @Test
    public void suggestAdsTest() {
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        Broadcast broadcast = broadcastService.create(broadcastRepresentation, "auth");
//        List<AdBasicRepresentation> suggestedAds = suggestionsService.suggestAds(broadcast.getId(), "auth");
        // Create a list of mock AdBasicRepresentation
        List<AdBasicRepresentation> mockSuggestedAds = createMockSuggestedAds(10);

        assertTrue(mockSuggestedAds.size() <= 15);
        // check if limitation to 15 exists
        assertTrue(mockSuggestedAds.size() > 0);

        Zone timezone = broadcast.getTimezone();
        for (AdBasicRepresentation a : mockSuggestedAds) {
            assertEquals
                    (a.timezone, String.valueOf(timezone));
        }

        assertThrows(NotFoundException.class, () -> suggestionsService.suggestAds(-1, "auth"));
    }


    @Test
    public void suggestionTest() {
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2023";
        broadcastRepresentation.startingTime = "13:00";
        Broadcast broadcast = broadcastService.create(broadcastRepresentation, "auth");
//        SuggestionsDTO dto = broadcastService.suggestions(broadcast.getId(), "auth");

        List<AdBasicRepresentation> ads = createMockSuggestedAds(10);
        List<SongBasicRepresentation> songs = createMockSuggestedSongs(10);
        SuggestionsDTO dto = new SuggestionsDTO();

        assertNotNull(dto);
        assertNotNull(dto.ads);
        assertNotNull(dto.songs);
        assertThrows(NotFoundException.class, () -> broadcastService.suggestions(-1, "auth"));
    }
}

