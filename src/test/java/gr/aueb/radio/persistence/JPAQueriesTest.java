package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.*;
import gr.aueb.radio.utils.JPAUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JPAQueriesTest {
    EntityManager entityManager;

    @BeforeEach
    public void setUp(){
        Initializer initializer = new Initializer();
        initializer.initializeData();
        entityManager = JPAUtil.getCurrentEntityManager();
    }

    @Test
    public void userListTest(){
        List<User> users = entityManager.createQuery("select u from User u").getResultList();
        assertEquals(2, users.size());
    }

    @Test
    public void songListTest(){
        List<Song> songs = entityManager.createQuery("select song from Song song").getResultList();
        assertEquals(2, songs.size());
        for (Song song : songs) {
            List<SongBroadcast> songBroadcasts = song.getSongBroadcasts();
            assertEquals(1, songBroadcasts.size());
        }
    }

    @Test
    public void addListTest(){
        List<Add> adds = entityManager.createQuery("select add from Add add").getResultList();
        assertEquals(2, adds.size());
        for (Add add : adds) {
            List<AddBroadcast> addBroadcasts = add.getBroadcastAdds();
            assertEquals(1, addBroadcasts.size());
        }
    }

    @Test
    public void addBroadcastListTest(){
        List<AddBroadcast> addBroadcasts = entityManager.createQuery("select ab from AddBroadcast ab").getResultList();
        assertEquals(2, addBroadcasts.size());
        for (AddBroadcast addBroadcast: addBroadcasts){
            assertNotNull(addBroadcast.getAdd());
        }
    }

    @Test
    public void songBroadcastListTest(){
        List<SongBroadcast> songBroadcasts = entityManager.createQuery("select sb from SongBroadcast sb").getResultList();
        assertEquals(2, songBroadcasts.size());
        for (SongBroadcast songBroadcast: songBroadcasts){
            assertNotNull(songBroadcast.getSong());
        }
    }

    @Test
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
}
