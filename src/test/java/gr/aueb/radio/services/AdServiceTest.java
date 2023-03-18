package gr.aueb.radio.services;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.representations.AdRepresentation;
import io.quarkus.test.TestTransaction;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdServiceTest {
    @Inject
    AdService adService;

    @Inject
    AdRepository adRepository;

    @Test
    @TestTransaction
    public void findAdTest(){
        List<Ad> ad = adRepository.listAll();
        Integer validId = ad.get(0).getId();

        AdRepresentation foundUser = adService.findAd(validId);
        assertNotNull(foundUser);
    }
}
