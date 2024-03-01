package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Zone;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@QuarkusTest
public class PlaylistServiceTest {
    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    PlaylistService playlistService;

    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    BroadcastRepository broadcastRepository;
    @InjectMock
    ContentService contentService;

    @InjectMock
    UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
    }

    @Test
    public void testPopulateBr() {
        Broadcast broadcast = broadcastRepository.findByIdSongDetails(3001);
        Zone timezone = broadcast.getTimezone();

        assertNotNull(broadcast);
//        assertThrows(NotFoundException.class, () -> broadcastRepository.findByIdSongDetails(100));

        int[] ids = {1001, 1002};
        StringBuilder adsIds = new StringBuilder();
        for (int i = 0; ids.length > i; i++) {
            int songId = ids[i];

            adsIds.append(songId);
            if (i != ids.length - 1) {
                adsIds.append(",");
            }
        }
        List<AdBasicRepresentation> mockSuggestedAds = new ArrayList<>();
        AdBasicRepresentation ad1 = new AdBasicRepresentation();
        ad1.id = 7001;
        ad1.duration = 12;
        ad1.timezone = "LateNight";

        AdBasicRepresentation ad2 = new AdBasicRepresentation();
        ad2.id = 7002;
        ad2.duration = 1;
        ad2.timezone = "LateNight";
        // Add the AdBasicRepresentation objects to the list
        mockSuggestedAds.add(ad1);
        mockSuggestedAds.add(ad2);
        Mockito.when(contentService.getAdsByFilters("PRODUCER", null, adsIds.toString())).thenReturn(mockSuggestedAds);


        Integer genre = broadcast.getGenreId();
        assertNotNull(broadcast);
//        assertThrows(NotFoundException.class, () -> broadcastRepository.findByIdSongDetails(100));

        int[] ids2 = {7001, 7002};
        StringBuilder songsIds = new StringBuilder();
        for (int i = 0; ids2.length > i; i++) {
            int songId = ids2[i];

            songsIds.append(songId);
            if (i != ids2.length - 1) {
                songsIds.append(",");
            }
        }
        List<SongBasicRepresentation> mockSuggestedSongs = new ArrayList<>();
        SongBasicRepresentation song1 = new SongBasicRepresentation();
        song1.id = 7001;
        song1.duration = 180; // Example duration in seconds
        song1.genreId = 1; // Example genre ID

        SongBasicRepresentation song2 = new SongBasicRepresentation();
        song2.id = 7002;
        song2.duration = 210; // Example duration in seconds
        song2.genreId = 1; // Example genre ID
        // Add the SongBasicRepresentation objects to the list
        mockSuggestedSongs.add(song1);
        mockSuggestedSongs.add(song2);
        Mockito.when(contentService.getSongsByFilters("PRODUCER", null, null, null, null, songsIds.toString())).thenReturn(mockSuggestedSongs);

        assertTrue(mockSuggestedSongs.size() <= 15);
        // check if limitation to 15 exists
        assertTrue(mockSuggestedSongs.size() > 0);

//        Integer genre = mockSuggestedSongs.get(0).genreId;
        for (SongBasicRepresentation s : mockSuggestedSongs) {
            assertEquals
                    (s.genreId, genre);
        }
        LocalTime trackedTime = broadcast.getStartingTime();

        playlistService.populateBroadcast(broadcast, "auth");
    }
}
