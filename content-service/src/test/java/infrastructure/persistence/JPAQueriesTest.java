package infrastructure.persistence;


import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class JPAQueriesTest {
    @Inject
    EntityManager entityManager;
    @Test
    @TestTransaction
    public void songListTest(){
        List<Song> songs = entityManager.createQuery("select song from Song song").getResultList();
        assertEquals(2, songs.size());

    }

    @Test
    @TestTransaction
    public void addListTest(){
        List<Ad> ads = entityManager.createQuery("select ad from Ad ad").getResultList();
        assertEquals(2, ads.size());

    }

    @Test
    @TestTransaction
    public void testFindAllAds() {
        List<Ad> ads = entityManager.createQuery("SELECT a FROM Ad a", Ad.class).getResultList();
        assertNotNull(ads);
        assertEquals(2, ads.size()); // Assuming there are 2 ads in the database
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


}
