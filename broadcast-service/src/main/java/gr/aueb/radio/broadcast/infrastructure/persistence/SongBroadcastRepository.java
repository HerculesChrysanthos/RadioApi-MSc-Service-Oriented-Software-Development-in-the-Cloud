package gr.aueb.radio.broadcast.infrastructure.persistence;

import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SongBroadcastRepository implements PanacheRepositoryBase<SongBroadcast, Integer> {
    public List<SongBroadcast> findByDateDetails(LocalDate date){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
//        List<SongBroadcast> broadcasts = find("select sb from SongBroadcast sb left join fetch sb.song where sb.broadcastDate=:date", params).list();
//        return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb in :song_broadcasts", Parameters.with("song_broadcasts", broadcasts).map()).list();

        return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb.broadcastDate=:date", params).list();
    }

    public List<SongBroadcast> searchBySongId(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb.songId=:id", params).list();
    }

    public SongBroadcast findByIdDetails(Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb.id=:id",params).singleResult();
//        List<SongBroadcast> broadcasts = find("select sb from SongBroadcast sb left join fetch sb.song where sb.id=:id", params).list();
//        try {
//            return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb in :song_broadcasts", Parameters.with("song_broadcasts", broadcasts).map()).singleResult();
//        }catch (NoResultException e){
//            return null;
//        }
    }

    public List<SongBroadcast> findBySongIdDate(Integer id, LocalDate date) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("date", date);

        return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb.broadcastDate=:date and sb.id=:id", params).list();
    }

    public List<SongBroadcast> findByFilters(LocalDate date, Integer songId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder("select sb from SongBroadcast sb left join fetch sb.broadcast where 1=1");

        if (date != null) {
            queryBuilder.append(" and sb.broadcastDate = :date");
            params.put("date", date);
        }

        if (songId != null) {
            queryBuilder.append(" and sb.songId = :songId");
            params.put("songId", songId);
        }

        PanacheQuery<SongBroadcast> query = find(queryBuilder.toString(), params);
        return query.list();
    }

    public void deleteBySongId(Integer songId) {
        delete("songId", songId);
    }

}
