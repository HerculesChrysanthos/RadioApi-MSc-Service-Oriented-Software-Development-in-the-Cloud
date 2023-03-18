package gr.aueb.radio.persistence;

import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.domains.*;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class JPAQueriesTest extends IntegrationBase {
    @Inject
    EntityManager entityManager;

    @Test
    @TestTransaction
    public void userListTest(){
        List<User> users = entityManager.createQuery("select u from User u").getResultList();
        assertEquals(2, users.size());
    }

    @Test
    @TestTransaction
    public void songListTest(){
        List<Song> songs = entityManager.createQuery("select song from Song song").getResultList();
        assertEquals(2, songs.size());
        for (Song song : songs) {
            List<SongBroadcast> songBroadcasts = song.getSongBroadcasts();
            assertEquals(1, songBroadcasts.size());
        }
    }

    @Test
    @TestTransaction
    public void addListTest(){
        List<Ad> ads = entityManager.createQuery("select add from Add add").getResultList();
        assertEquals(2, ads.size());
        for (Ad add : ads) {
            List<AdBroadcast> adBroadcasts = add.getBroadcastAds();
            assertEquals(1, adBroadcasts.size());
        }
    }

    @Test
    @TestTransaction
    public void addBroadcastListTest(){
        List<AdBroadcast> adBroadcasts = entityManager.createQuery("select ab from AddBroadcast ab").getResultList();
        assertEquals(2, adBroadcasts.size());
        for (AdBroadcast adBroadcast : adBroadcasts){
            assertNotNull(adBroadcast.getAd());
        }
    }

    @Test
    @TestTransaction
    public void songBroadcastListTest(){
        List<SongBroadcast> songBroadcasts = entityManager.createQuery("select sb from SongBroadcast sb").getResultList();
        assertEquals(2, songBroadcasts.size());
        for (SongBroadcast songBroadcast: songBroadcasts){
            assertNotNull(songBroadcast.getSong());
        }
    }

    @Test
    @TestTransaction
    public void broadcastListTest(){
        List<Broadcast> broadcasts = entityManager.createQuery("select broadcast from Broadcast broadcast").getResultList();
        assertEquals(1, broadcasts.size());
        Broadcast broadcast = broadcasts.get(0);
        List<AdBroadcast> adBroadcasts = broadcast.getAdBroadcasts();
        assertEquals(2, adBroadcasts.size());
        List<SongBroadcast> songBroadcasts = broadcast.getSongBroadcasts();
        assertEquals(2, songBroadcasts.size());
        assertTrue(broadcast.getAllocatedTime() > 0);
    }

    @Test
    @TestTransaction
    public void restrictSongBroadcastCreation(){
        List<Broadcast> broadcasts = entityManager.createQuery("select b from Broadcast b").getResultList();
        Broadcast broadcast = broadcasts.get(0);
        List<SongBroadcast> songBroadcasts = broadcast.getSongBroadcasts();
        Integer listOfSongs = songBroadcasts.size();
        // total duration restriction
        // no broadcast is added
        Song invalidSong1 = new Song("title", "genre", 190, "artist", 2023);
        broadcast.createSongBroadcast(invalidSong1, DateUtil.setTime("02:40"));
        assertEquals(listOfSongs, broadcast.getSongBroadcasts().size());
        // repeated song restriction
        // no broadcast is added
        Song repeatedSong = songBroadcasts.get(1).getSong();
        broadcast.createSongBroadcast(repeatedSong, DateUtil.setTime("02:00"));
        assertEquals(listOfSongs, broadcast.getSongBroadcasts().size());
        // occurrence song restriction
        // no broadcast is added
        Song invalidSong2 = new Song("title", "genre", 5, "artist", 2023);
        broadcast.createSongBroadcast(invalidSong2, DateUtil.setTime("00:45"));
        assertEquals(listOfSongs, broadcast.getSongBroadcasts().size());
        // exceed limit restriction
        // no broadcast is added
        Song invalidSong3 = new Song("title", "genre", 5, "artist", 2023);
        broadcast.createSongBroadcast(invalidSong3, DateUtil.setTime("13:15"));
        assertEquals(listOfSongs, broadcast.getAdBroadcasts().size());
        // exceed limit restriction
        // no broadcast is added
        LocalDateTime broadcastEndingTime = broadcast.getBroadcastEndingDateTime();
        broadcast.createSongBroadcast(invalidSong3, broadcastEndingTime.minusMinutes(invalidSong3.getDuration()/2).toLocalTime());
        assertEquals(listOfSongs, broadcast.getAdBroadcasts().size());
    }

    @Test
    @TestTransaction
    public void restrictAddBroadcastCreation(){
        List<Broadcast> broadcasts = entityManager.createQuery("select b from Broadcast b").getResultList();
        Broadcast broadcast = broadcasts.get(0);
        List<AdBroadcast> adBroadcasts = broadcast.getAdBroadcasts();
        Integer listOfAdds = adBroadcasts.size();
        // total duration restriction
        // no broadcast is added
        Ad invalidAdd1 = new Ad(broadcast.getDuration() + 100, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.LateNight);
        broadcast.createAdBroadcast(invalidAdd1, DateUtil.setTime("02:40"));
        assertEquals(listOfAdds, broadcast.getAdBroadcasts().size());
        // invalid timezone restriction
        // no broadcast is added
        Ad invalidAdd2 = new Ad(5, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.Afternoon);
        broadcast.createAdBroadcast(invalidAdd2, DateUtil.setTime("02:00"));
        assertEquals(listOfAdds, broadcast.getAdBroadcasts().size());
        // occurrence song restriction
        // no broadcast is added
        Ad invalidAdd3 = new Ad(5, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.LateNight);
        broadcast.createAdBroadcast(invalidAdd3, DateUtil.setTime("00:15"));
        assertEquals(listOfAdds, broadcast.getAdBroadcasts().size());
        // exceed limit restriction
        // no broadcast is added
        Ad invalidAdd4 = new Ad(5, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.LateNight);
        broadcast.createAdBroadcast(invalidAdd4, DateUtil.setTime("13:15"));
        assertEquals(listOfAdds, broadcast.getAdBroadcasts().size());
        // exceed limit restriction
        // no broadcast is added
        LocalDateTime broadcastEndingTime = broadcast.getBroadcastEndingDateTime();
        broadcast.createAdBroadcast(invalidAdd4, broadcastEndingTime.minusMinutes(invalidAdd4.getDuration()/2).toLocalTime());
        assertEquals(listOfAdds, broadcast.getAdBroadcasts().size());
        // Add rep_per_zone restriction
        // no broadcast is added
        Ad invalidAdd5 = new Ad(10, 0, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.LateNight);
        broadcast.createAdBroadcast(invalidAdd5, DateUtil.setTime("02:40"));
        assertEquals(listOfAdds, broadcast.getAdBroadcasts().size());
    }
}
