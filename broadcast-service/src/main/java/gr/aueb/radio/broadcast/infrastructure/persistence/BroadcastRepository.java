package gr.aueb.radio.broadcast.infrastructure.persistence;



import  gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import  gr.aueb.radio.broadcast.domain.broadcast.enums.*;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class BroadcastRepository implements PanacheRepositoryBase<Broadcast, Integer> {

    @Override
    public Broadcast findById(Integer id){
        PanacheQuery<Broadcast> query = find("select b from Broadcast b left join fetch b.songBroadcasts where b.id=:id", Parameters.with("id", id).map());
        try {
            Broadcast b =  query.singleResult();
            query = find("select b from Broadcast b left join fetch b.adBroadcasts where b.id=:id", Parameters.with("id", b.getId()).map());
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public Broadcast findByIdAdDetails(Integer id){
        PanacheQuery<Broadcast> query = find("select b from Broadcast b left join fetch b.adBroadcasts where b.id=:id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public Broadcast findByIdSongDetails(Integer id){
        PanacheQuery<Broadcast> query = find("select b from Broadcast b left join fetch b.songBroadcasts where b.id=:id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public List<Broadcast> findByDate(LocalDate date){
        List<Broadcast> broadcasts = find("select b from Broadcast b left join fetch b.songBroadcasts where b.startingDate=:date", Parameters.with("date", date).map()).list();
        return find("select distinct b from Broadcast b left join fetch b.adBroadcasts where b in :broadcasts", Parameters.with("broadcasts", broadcasts).map()).list();
    }

    public List<Broadcast> findByDateExcluding(LocalDate date, Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        params.put("id", id);
        List<Broadcast> broadcasts = find("select b from Broadcast b left join fetch b.songBroadcasts where b.startingDate=:date and b.id != :id", params).list();
        return find("select distinct b from Broadcast b left join fetch b.adBroadcasts where b in :broadcasts", Parameters.with("broadcasts", broadcasts).map()).list();
    }

    public List<Broadcast> findByTimeRange(LocalTime from, LocalTime to){
        Map<String, Object> params = new HashMap<>();
        params.put("toTime", to);
        params.put("fromTime", from);
        List<Broadcast> broadcasts = find("select b from Broadcast b left join fetch b.songBroadcasts where b.startingTime>=:fromTime and b.startingTime<=:toTime", params).list();
        return find("select distinct b from Broadcast b left join fetch b.adBroadcasts where b in :broadcasts", Parameters.with("broadcasts", broadcasts).map()).list();
    }

    public List<Broadcast> findByTimeRangeDate(LocalTime from, LocalTime to, LocalDate date){
        Map<String, Object> params = new HashMap<>();
        params.put("toTime", to);
        params.put("fromTime", from);
        params.put("date", date);
        List<Broadcast> broadcasts = find("select b from Broadcast b left join fetch b.songBroadcasts where b.startingTime >=:fromTime and b.startingTime<=:toTime and b.startingDate=:date", params).list();
        return find("select distinct b from Broadcast b left join fetch b.adBroadcasts where b in :broadcasts", Parameters.with("broadcasts", broadcasts).map()).list();
    }

    public List<Broadcast> findByTimeRangeType(LocalTime from, LocalTime to, BroadcastEnum type){
        Map<String, Object> params = new HashMap<>();
        params.put("toTime", to);
        params.put("fromTime", from);
        params.put("type", type);
        List<Broadcast> broadcasts = find("select b from Broadcast b left join fetch b.songBroadcasts where b.startingTime>=:fromTime and b.startingTime<=:toTime and b.type=:type", params).list();
        return find("select distinct b from Broadcast b left join fetch b.adBroadcasts where b in :broadcasts", Parameters.with("broadcasts", broadcasts).map()).list();
    }

    public List<Broadcast> findByTimeRangeDateType(LocalTime from, LocalTime to, LocalDate date, BroadcastEnum type){
        Map<String, Object> params = new HashMap<>();
        params.put("toTime", to);
        params.put("fromTime", from);
        params.put("type", type);
        params.put("date", date);
        List<Broadcast> broadcasts = find("select b from Broadcast b left join fetch b.songBroadcasts where b.startingTime>=:fromTime and b.startingTime<=:toTime and b.startingDate=:date and b.type=:type", params).list();
        return find("select distinct b from Broadcast b left join fetch b.adBroadcasts where b in :broadcasts", Parameters.with("broadcasts", broadcasts).map()).list();
    }

}
