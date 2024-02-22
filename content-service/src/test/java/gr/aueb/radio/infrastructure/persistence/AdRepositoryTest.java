package gr.aueb.radio.infrastructure.persistence;

import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AdRepositoryTest {

    @Inject
    AdRepository adRepository;

    @Test
    @Transactional
    public void testFindByTimezone() {

        // When
        List<Ad> adsInLateNight = adRepository.findByTimezone(Zone.LateNight);


        assertEquals(2, adsInLateNight.size());
    }
}