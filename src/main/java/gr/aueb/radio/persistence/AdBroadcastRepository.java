package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.AddBroadcast;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class AdBroadcastRepository implements PanacheRepositoryBase<AddBroadcast, Integer> {

    /* find addBroadcasts of a specific add */
    public List<AddBroadcast> findAddBrByAdd(Integer addId){
        return find("select ab from AddBroadcasts ab where ab.add.id=:addId", Parameters.with("addId", addId).map()).list();
    }
}
