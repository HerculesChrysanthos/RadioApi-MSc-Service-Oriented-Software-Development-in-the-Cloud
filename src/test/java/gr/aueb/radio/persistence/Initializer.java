package gr.aueb.radio.persistence;


import gr.aueb.radio.domains.Add;
import gr.aueb.radio.domains.AddBroadcast;
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
        Query query3 = entityManager.createNativeQuery("delete from add_broadcasts");

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

        Add add1 = new Add(25, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.EarlyMorning);
        Add add2 = new Add(25, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-04-2022"), ZoneEnum.Morning);

        AddBroadcast addBroadcast1 = new AddBroadcast(DateUtil.setDate("01-01-2022"), DateUtil.setTime("14:42"));
        AddBroadcast addBroadcast2 = new AddBroadcast(DateUtil.setDate("23-04-2022"), DateUtil.setTime("06:58"));

        EntityManager entityManager = JPAUtil.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(producer);
        entityManager.persist(user);



        entityManager.persist(addBroadcast1);
        entityManager.persist(addBroadcast2);

        add1.addBroadcastAdd(addBroadcast1);
        entityManager.persist(add1);

        add2.addBroadcastAdd(addBroadcast2);
        entityManager.persist(add2);

        entityTransaction.commit();
        entityManager.close();
    }
}
