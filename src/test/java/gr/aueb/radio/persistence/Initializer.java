package gr.aueb.radio.persistence;


import gr.aueb.radio.domains.User;
import gr.aueb.radio.enums.RoleEnum;
import gr.aueb.radio.utils.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class Initializer {
    public void  dropData() {
        EntityManager entityManager = JPAUtil.getCurrentEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        
        Query query = entityManager.createNativeQuery("delete from users");
        query.executeUpdate();
        entityTransaction.commit();
        entityManager.close();
    }

    public void initializeData() {
        dropData();
        User producer = new User("producer", "producer", "producer@email.com", RoleEnum.PRODUCER);
        User user = new User("user", "user", "user@email.com", RoleEnum.USER);

        EntityManager entityManager = JPAUtil.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(producer);
        entityManager.persist(user);

        entityTransaction.commit();
        entityManager.close();
    }
}
