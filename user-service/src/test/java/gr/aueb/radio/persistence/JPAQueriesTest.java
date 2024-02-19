package gr.aueb.radio.persistence;

import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.user.domain.user.User;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
public class JPAQueriesTest extends IntegrationBase {
    @Inject
    EntityManager entityManager;

    @Test
    @TestTransaction
    public void userListTest() {
        List<User> users = entityManager.createQuery("select u from User u").getResultList();
        assertEquals(2, users.size());
    }
}