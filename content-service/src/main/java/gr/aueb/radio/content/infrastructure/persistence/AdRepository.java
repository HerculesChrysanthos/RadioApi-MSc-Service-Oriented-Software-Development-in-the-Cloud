package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;
@RequestScoped
public class AdRepository implements PanacheRepositoryBase <Ad, Integer> {

    public List<Ad> findByTimezone(Zone timeZone) {
        return find("select a from Ad a where a.timezone = :timezone", Parameters.with("timezone", timeZone).map()).list();
    }

    public List<Ad> findAdsByIds(List<Integer> ids) {
        PanacheQuery<Ad> query = find("select a from Ad a where a.id IN :ids ", Parameters.with("ids", ids).map());

        return query.list();
    }

}
