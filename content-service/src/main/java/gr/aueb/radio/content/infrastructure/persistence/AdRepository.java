package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;

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

    public List<Ad> findByFilters(List<Integer> ids, Zone timezone) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder("select ad from Ad ad where 1=1");


        if (ids != null && !ids.isEmpty()) {
            queryBuilder.append(" and ad.id in :ids");
            params.put("ids", ids);
        }

        if (timezone != null) {
            queryBuilder.append(" and ad.timezone = :timezone");
            params.put("timezone", timezone);
        }

        PanacheQuery<Ad> query = find(queryBuilder.toString(), params);
        return query.list();
    }

}
