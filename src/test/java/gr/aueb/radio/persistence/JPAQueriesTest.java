package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.domains.User;
import gr.aueb.radio.utils.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;

public class JPAQueriesTest {
    EntityManager entityManager;

    @BeforeEach
    public void setUp(){
        Initializer initializer = new Initializer();
        initializer.initializeData();
        entityManager = JPAUtil.getCurrentEntityManager();
    }

//    @AfterEach
//    public void tearDown(){
////        entityManager.close();
//    }

    @Test
    public void userListTest(){
        List<User> users = entityManager.createQuery("select u from User u").getResultList();
        Assertions.assertEquals(2, users.size());
    }
}
