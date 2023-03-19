package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.*;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.representations.BroadcastRepresentation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class PlaylistServiceTest extends IntegrationBaseExtended {

    @Inject
    BroadcastService broadcastService;

    @Inject
    SongRepository songRepository;

    @Inject
    AdRepository adRepository;

    private BroadcastRepresentation createRepresentation(){
        BroadcastRepresentation broadcastRepresentation = new BroadcastRepresentation();
        broadcastRepresentation.duration = 45;
        broadcastRepresentation.startingDate = "11-02-2023";
        broadcastRepresentation.startingTime = "11:00";
        broadcastRepresentation.type = BroadcastEnum.PLAYLIST;
        return broadcastRepresentation;
    }

    @Test
    @Transactional
    public void createPlaylistTest(){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);

        assertNotNull(broadcast);
        List<SongBroadcast> songBroadcasts = broadcast.getSongBroadcasts();
        assertTrue(songBroadcasts.size() > 0);
        List<AdBroadcast> adBroadcasts = broadcast.getAdBroadcasts();
        assertTrue(adBroadcasts.size() <= 1);
        String genre = songBroadcasts.get(0).getSong().getGenre();
        for (SongBroadcast sb : songBroadcasts){
            assertTrue(genre.equals(sb.getSong().getGenre()));
        }
        ZoneEnum timezone = broadcast.getTimezone();
        for(AdBroadcast ab: adBroadcasts){
            assertEquals(timezone, ab.getAd().getTimezone());
        }
    }

    @Test
    @Transactional
    public void createPlaylistNoExceptionThrownTest(){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        SongRepository mockedSongRepository = Mockito.mock(SongRepository.class);
        AdRepository mockedAdRepository = Mockito.mock(AdRepository.class);
        Mockito.when(mockedSongRepository.findSongsByGenre(any())).thenReturn(new ArrayList<>());
        Mockito.when(mockedAdRepository.findByTimezone(any())).thenReturn(new ArrayList<>());
        assertDoesNotThrow(()->{
            broadcastService.create(broadcastRepresentation);
        });
    }
}
