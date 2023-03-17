package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.Add;
import gr.aueb.radio.enums.ZoneEnum;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import java.util.List;

@RequestScoped

/* get add by timezone */
public class AdRepository implements PanacheRepositoryBase<Add, Integer> {

    public List<Add> findByTimezone(ZoneEnum timeZone){
        return find("select a from Add a where a.timezone = :timezone", Parameters.with("timezone", timeZone).map()).list();
    }

    public Add findById(Integer id) {
        PanacheQuery<Add> query = find("select a from Add a where a.id = :id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }
}

