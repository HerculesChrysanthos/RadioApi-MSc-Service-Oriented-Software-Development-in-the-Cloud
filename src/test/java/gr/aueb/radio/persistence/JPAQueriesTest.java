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
        List<Add> adds = entityManager.createQuery("select add from Add add").getResultList();
        assertEquals(2, adds.size());
        for (Add add : adds) {
            List<AddBroadcast> addBroadcasts = add.getBroadcastAdds();
            assertEquals(1, addBroadcasts.size());
        }
    }

    @Test
    @TestTransaction
    public void addBroadcastListTest(){
        List<AddBroadcast> addBroadcasts = entityManager.createQuery("select ab from AddBroadcast ab").getResultList();
        assertEquals(2, addBroadcasts.size());
        for (AddBroadcast addBroadcast: addBroadcasts){
            assertNotNull(addBroadcast.getAdd());
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
        List<AddBroadcast> addBroadcasts = broadcast.getAddBroadcasts();
        assertEquals(2, addBroadcasts.size());
        List<SongBroadcast> songBroadcasts = broadcast.getSongBroadcasts();
        assertEquals(2, songBroadcasts.size());
        assertTrue(broadcast.getAllocatedTime() > 0);
    }

    @Test
    @TestTransaction
    public void restrictSongBroadcastCreation(){
        List<Broadcast> broadcasts = entityManager.createQuery("select broadcast from Broadcast broadcast").getResultList();
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
        broadcast.createSongBroadcast(repeatedSong, DateUtil.setTime("00:45"));
        assertEquals(listOfSongs, broadcast.getSongBroadcasts().size());
    }

    @Test
    @TestTransaction
    public void restrictAddBroadcastCreation(){
        List<Broadcast> broadcasts = entityManager.createQuery("select broadcast from Broadcast broadcast").getResultList();
        Broadcast broadcast = broadcasts.get(0);
        List<AddBroadcast> addBroadcasts = broadcast.getAddBroadcasts();
        Integer listOfAdds = addBroadcasts.size();
        // total duration restriction
        // no broadcast is added
        Add invalidAdd1 = new Add(190, 0, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.LateNight);
        broadcast.createAddBroadcast(invalidAdd1, DateUtil.setTime("02:40"));
        assertEquals(listOfAdds, broadcast.getAddBroadcasts().size());
        // invalid timezone restriction
        // no broadcast is added
        Add invalidAdd2 = new Add(5, 0, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.Afternoon);
        broadcast.createAddBroadcast(invalidAdd2, DateUtil.setTime("02:40"));
        assertEquals(listOfAdds, broadcast.getAddBroadcasts().size());
        // occurrence song restriction
        // no broadcast is added
        Add invalidAdd3 = new Add(5, 0, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.LateNight);
        broadcast.createAddBroadcast(invalidAdd3, DateUtil.setTime("00:15"));
        assertEquals(listOfAdds, broadcast.getAddBroadcasts().size());
    }
}
