package gr.aueb.radio.content.infrastructure.persistence;


import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class JPAQueriesTest {
    @Inject
    EntityManager entityManager;
    @Test
    @TestTransaction
    public void songListTest(){
        List<Song> songs = entityManager.createQuery("select song from Song song").getResultList();
        assertEquals(5, songs.size());

    }

    @Test
    @TestTransaction
    public void findSongsByGenre() {
        List<Song> songs = entityManager.createQuery("select s from Song s where s.genre = :genre")
                .setParameter("genre",null)
                .getResultList();
        assertNotNull(songs);
        //assertEquals(1, songs.size());
    }


    @Test
    @TestTransaction
    public void addListTest(){
        List<Ad> ads = entityManager.createQuery("select ad from Ad ad").getResultList();
        assertEquals(5, ads.size());

    }

    @Test
    @TestTransaction
    public void testFindAllAds() {
        List<Ad> ads = entityManager.createQuery("SELECT a FROM Ad a", Ad.class).getResultList();
        assertNotNull(ads);
        assertEquals(5, ads.size()); // Assuming there are 2 ads in the database
    }

    @Test
    @TestTransaction
    public void testFindAdById() {
        Ad ad = entityManager.find(Ad.class, 1001); //
        assertNotNull(ad);
        assertEquals(1001, ad.getId());
    }

    @Test
    @TestTransaction
    public void testFindAdsByZone() {
        List<Ad> ads = entityManager.createQuery("SELECT a FROM Ad a WHERE a.timezone = :zone", Ad.class)
                .setParameter("zone", Zone.LateNight)
                .getResultList();
        assertNotNull(ads);
        assertEquals(2, ads.size());
    }

    @Test
    @TestTransaction
    public void testFindAdsByIds() {
        List<Ad> ads = entityManager.createQuery("select a from Ad a where a.id IN :ids", Ad.class)
                .setParameter("ids",1002)
                .getResultList();
        assertNotNull(ads);
        assertEquals(1, ads.size());
    }



}
