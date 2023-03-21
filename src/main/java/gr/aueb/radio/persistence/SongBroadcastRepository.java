package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.SongBroadcast;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class SongBroadcastRepository implements PanacheRepositoryBase<SongBroadcast, Integer> {
    public List<SongBroadcast> findByDateDetails(LocalDate date){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        List<SongBroadcast> broadcasts = find("select sb from SongBroadcast sb left join fetch sb.song where sb.broadcastDate=:date", params).list();
        return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb in :song_broadcasts", Parameters.with("song_broadcasts", broadcasts).map()).list();
    }

    public SongBroadcast findByIdDetails(Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<SongBroadcast> broadcasts = find("select sb from SongBroadcast sb left join fetch sb.song where sb.id=:id", params).list();
        try {
            return find("select sb from SongBroadcast sb left join fetch sb.broadcast where sb in :song_broadcasts", Parameters.with("song_broadcasts", broadcasts).map()).singleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
