package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.domains.Song;
import gr.aueb.radio.dto.SuggestionsDTO;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.mappers.SongMapper;
import gr.aueb.radio.representations.BroadcastRepresentation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SuggestionsServiceTest extends IntegrationBaseExtended {
    @Inject
    SuggestionsService suggestionsService;

    @Inject
    BroadcastService broadcastService;

    @Inject
    SongService songService;

    @Inject
    SongMapper songMapper;

    private BroadcastRepresentation createRepresentation(){
        BroadcastRepresentation broadcastRepresentation = new BroadcastRepresentation();
        broadcastRepresentation.duration = 45;
        broadcastRepresentation.startingDate = "11-02-2023";
        broadcastRepresentation.startingTime = "11:00";
        broadcastRepresentation.type = BroadcastEnum.NEWS;
        return broadcastRepresentation;
    }

    @Test
    @Transactional
    public void suggestSongsTest(){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);
        List<Song> suggestedSongs = suggestionsService.suggestSongs(broadcast.getId());
        assertTrue(suggestedSongs.size() <= 15);
        // check if limitation to 15 exists
        assertTrue(suggestedSongs.size() > 0);

        String genre = suggestedSongs.get(0).getGenre();
        for (Song s : suggestedSongs){
            assertTrue(genre.equals(s.getGenre()));
        }

        assertThrows(NotFoundException.class, ()-> suggestionsService.suggestSongs(-1));
    }

    @Test
    @Transactional
    public void suggestSongsTestExistingBroadcast(){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);
        Song song = new Song("I am not rich", "pop", 3, "The king's son", 2016);
        song = songService.create(songMapper.toRepresentation(song));
        broadcastService.scheduleSong(broadcast.getId(), song, broadcast.getStartingTime());
        List<Song> suggestedSongs = suggestionsService.suggestSongs(broadcast.getId());
        // check if limitation to 15 exists
        assertTrue(suggestedSongs.size() <= 15);

        String genre = suggestedSongs.get(0).getGenre();
        for (Song s : suggestedSongs){
            assertTrue(genre.equals(s.getGenre()));
        }
    }

    @Test
    @Transactional
    public void suggestAdsTest(){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);
        List<Ad> suggestedAds = suggestionsService.suggestAds(broadcast.getId());
        assertTrue(suggestedAds.size() <= 15);
        // check if limitation to 15 exists
        assertTrue(suggestedAds.size() > 0);

        ZoneEnum timezone = broadcast.getTimezone();
        for (Ad a : suggestedAds){
            assertEquals(timezone, a.getTimezone());
        }

        assertThrows(NotFoundException.class, ()-> suggestionsService.suggestAds(-1));
    }


    @Test
    @Transactional
    public void suggestionTest(){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2023";
        broadcastRepresentation.startingTime = "13:00";
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);
        SuggestionsDTO dto = broadcastService.suggestions(broadcast.getId());
        assertNotNull(dto);
        assertNotNull(dto.ads);
        assertNotNull(dto.songs);
        assertThrows(NotFoundException.class, ()-> broadcastService.suggestions(-1));
    }
}
