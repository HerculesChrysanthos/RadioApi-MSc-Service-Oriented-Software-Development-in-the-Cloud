package gr.aueb.radio.persistence;


import gr.aueb.radio.domains.Add;
import gr.aueb.radio.domains.BroadcastAdd;
import gr.aueb.radio.domains.User;
import gr.aueb.radio.enums.RoleEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
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
        Query query2 = entityManager.createNativeQuery("delete from adds");
        Query query3 = entityManager.createNativeQuery("delete from broadcastadd");

        query.executeUpdate();
        query2.executeUpdate();
        query3.executeUpdate();
        entityTransaction.commit();
        entityManager.close();
    }

    public void initializeData() {
        dropData();
        User producer = new User("producer", "producer", "producer@email.com", RoleEnum.PRODUCER);
        User user = new User("user", "user", "user@email.com", RoleEnum.USER);

        Add add1 = new Add(25, 1, DateUtil.setDate("01-01-22"),  DateUtil.setDate("01-03-22") , ZoneEnum.EarlyMorning);
        Add add2 = new Add(25, 1, DateUtil.setDate("01-01-22"),  DateUtil.setDate("01-04-22"), ZoneEnum.Morning);

        BroadcastAdd broadcastAdd1 = new BroadcastAdd(DateUtil.setDate("01-01-22"), DateUtil.setTime("14:42"), add1);
        BroadcastAdd broadcastAdd2 = new BroadcastAdd(DateUtil.setDate("23-04-22"), DateUtil.setTime("06:58"), add2);

        EntityManager entityManager = JPAUtil.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(producer);
        entityManager.persist(user);

        entityManager.persist(add1);
        entityManager.persist(add2);

        entityManager.persist(broadcastAdd1);
        entityManager.persist(broadcastAdd2);

        entityTransaction.commit();
        entityManager.close();
    }
}
