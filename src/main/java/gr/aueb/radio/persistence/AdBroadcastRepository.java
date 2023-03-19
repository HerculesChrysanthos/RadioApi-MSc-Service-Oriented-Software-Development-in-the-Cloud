package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.AdBroadcast;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import java.util.List;

@RequestScoped
public class AdBroadcastRepository implements PanacheRepositoryBase<AdBroadcast, Integer> {

    /* find addBroadcasts of a specific add */
    public List<AdBroadcast> findAdBrByAdv(Integer adId){
        return find("select ab from AdBroadcast ab where ab.ad.id=:adId", Parameters.with("adId", adId).map()).list();
    }

    public AdBroadcast findById(Integer id) {
        PanacheQuery<AdBroadcast> query = find("select a from AdBroadcast a where a.id = :id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }
}
