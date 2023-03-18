package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.AdBroadcast;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class AdBroadcastRepository implements PanacheRepositoryBase<AdBroadcast, Integer> {

    /* find addBroadcasts of a specific add */
    public List<AdBroadcast> findAdBrByAdv(Integer adId){
        return find("select ab from AdBroadcasts ab where ab.ad.id=:adId", Parameters.with("adId", adId).map()).list();
    }
}
