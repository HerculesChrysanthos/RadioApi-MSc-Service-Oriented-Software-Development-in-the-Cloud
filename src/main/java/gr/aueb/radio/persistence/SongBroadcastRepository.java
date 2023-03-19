package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.SongBroadcast;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;

@RequestScoped
public class SongBroadcastRepository implements PanacheRepositoryBase<SongBroadcast, Integer> {

    public SongBroadcast findById(Integer id) {
    PanacheQuery<SongBroadcast> query = find("select a from SongBroadcast a where a.id = :id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }
}
