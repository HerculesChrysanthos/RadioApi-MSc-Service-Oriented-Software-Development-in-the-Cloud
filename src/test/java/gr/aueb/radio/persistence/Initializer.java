package gr.aueb.radio.persistence;


import gr.aueb.radio.domains.Add;
import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.domains.Song;
import gr.aueb.radio.domains.User;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.RoleEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import gr.aueb.radio.utils.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class Initializer {
    public void  dropData() {
        EntityManager entityManager = JPAUtil.getCurrentEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.createNativeQuery("delete from users").executeUpdate();
        entityManager.createNativeQuery("delete from add_broadcasts").executeUpdate();
        entityManager.createNativeQuery("delete from song_broadcasts").executeUpdate();
        entityManager.createNativeQuery("delete from adds").executeUpdate();
        entityManager.createNativeQuery("delete from songs").executeUpdate();
        entityManager.createNativeQuery("delete from broadcasts").executeUpdate();
        entityTransaction.commit();
        entityManager.close();
    }

    public void initializeData() {
        dropData();
        User producer = new User("producer", "producer", "producer@email.com", RoleEnum.PRODUCER);
        User user = new User("user", "user", "user@email.com", RoleEnum.USER);

        Add add1 = new Add(25, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-03-2022") , ZoneEnum.EarlyMorning);
        Add add2 = new Add(25, 1, DateUtil.setDate("01-01-2022"),  DateUtil.setDate("01-04-2022"), ZoneEnum.Morning);

        Song song1 = new Song("title", "genre", 45, "artist", 2023);
        Song song2 = new Song("title1", "genre2", 160, "artist1", 2023);

        Broadcast broadcast = new Broadcast(500, DateUtil.setDate("01-02-2022") , DateUtil.setTime("03:00"), BroadcastEnum.PLAYLIST);
        broadcast.createSongBroadcast(song2, DateUtil.setTime("03:00"));
        broadcast.createAddBroadcast(add1, DateUtil.setTime("05:40"));
        broadcast.createSongBroadcast(song1, DateUtil.setTime("06:10"));
        broadcast.createAddBroadcast(add2, DateUtil.setTime("07:00"));


        EntityManager entityManager = JPAUtil.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(producer);
        entityManager.persist(user);

        entityManager.persist(add1);
        entityManager.persist(add2);

        entityManager.persist(song1);
        entityManager.persist(song2);

        entityManager.persist(broadcast);

        entityTransaction.commit();
        entityManager.close();
    }
}
