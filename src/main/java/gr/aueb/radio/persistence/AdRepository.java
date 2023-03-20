package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.enums.ZoneEnum;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped

/* get ad by timezone */
public class AdRepository implements PanacheRepositoryBase<Ad, Integer> {

    public List<Ad> findByTimezone(ZoneEnum timeZone){
        return find("select a from Ad a where a.timezone = :timezone", Parameters.with("timezone", timeZone).map()).list();
    }
}

