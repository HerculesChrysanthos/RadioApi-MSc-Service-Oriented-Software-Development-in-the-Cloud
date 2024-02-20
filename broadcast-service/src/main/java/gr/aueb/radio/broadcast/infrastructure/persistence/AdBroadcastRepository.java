package gr.aueb.radio.broadcast.infrastructure.persistence;

import gr.aueb.radio.broadcast.domain.broadcast.AdBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.enums.BroadcastEnum;
import gr.aueb.radio.broadcast.domain.broadcast.enums.ZoneEnum;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AdBroadcastRepository implements PanacheRepositoryBase<AdBroadcast, Integer> {
    public List<AdBroadcast> findByTimezoneDate(ZoneEnum timezone, LocalDate date){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        params.put("timezone", timezone);
        return find("select ab from AdBroadcast ab where ab.ad.timezone = :timezone and ab.broadcastDate= :date", params).list();
    }

    public List<AdBroadcast> findByTypeDate(BroadcastEnum type, LocalDate date){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        params.put("type", type);
        return find("select ab from AdBroadcast ab where ab.broadcast.type = :type and ab.broadcastDate= :date", params).list();
    }

    public List<AdBroadcast> findByDate(LocalDate date){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        return find("select ab from AdBroadcast ab where ab.broadcastDate= :date", params).list();
    }

    public List<AdBroadcast> findByDateDetails(LocalDate date){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        List<AdBroadcast> broadcasts = find("select ab from AdBroadcast ab left join fetch ab.ad where ab.broadcastDate= :date", params).list();
        return find("select ab from AdBroadcast ab left join fetch ab.broadcast where ab in :ad_broadcasts", Parameters.with("ad_broadcasts", broadcasts).map()).list();
    }

    public AdBroadcast findByIdDetails(Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<AdBroadcast> broadcasts = find("select ab from AdBroadcast ab left join fetch ab.ad where ab.id=:id", params).list();
        try {
            return find("select ab from AdBroadcast ab left join fetch ab.broadcast where ab in :ad_broadcasts", Parameters.with("ad_broadcasts", broadcasts).map()).singleResult();
        }
        catch (NoResultException nr){
            return null;
        }
    }

}

