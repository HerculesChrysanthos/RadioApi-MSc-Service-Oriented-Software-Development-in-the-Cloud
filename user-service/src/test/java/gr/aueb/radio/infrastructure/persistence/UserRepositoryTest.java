package gr.aueb.radio.infrastructure.persistence;

import gr.aueb.radio.common.IntegrationBase;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserRepositoryTest extends IntegrationBase {

    @Inject
    UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = userRepository.findByUsername("producer");
        assertEquals(6001, user.getId());
    }

    @Test
    public void testFindByEmail() {
        User user = userRepository.findByEmail("producer@email.com");
        assertEquals(6001, user.getId());
    }
}
