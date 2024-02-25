package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class AdRepository implements PanacheRepositoryBase <Ad, Integer> {

    public List<Ad> findByTimezone(Zone timeZone) {
        return find("select a from Ad a where a.timezone = :timezone", Parameters.with("timezone", timeZone).map()).list();
    }

    public List<Ad> findAdsByIds(List<Integer> ids) {
        PanacheQuery<Ad> query = find("select a from Ad a where a.id IN :ids ", Parameters.with("ids", ids).map());

        return query.list();
    }

    public List<Ad> findByFilters(LocalDate date, String timezone) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder("select ad from Ad ad left join fetch ab.broadcast where");

        if (date != null) {
            queryBuilder.append(" and ad.broadcastDate = :date");
            params.put("date", date);
        }

        if (timezone != null && !timezone.isEmpty()) {
            queryBuilder.append(" and ad.timezone = :timezone");
            params.put("timezone", timezone);
        }

        PanacheQuery<Ad> query = find(queryBuilder.toString(), params);
        return query.list();
    }

}
