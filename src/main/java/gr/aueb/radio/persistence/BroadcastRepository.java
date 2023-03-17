package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.Broadcast;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class BroadcastRepository implements PanacheRepositoryBase<Broadcast, Integer> {

    public List<Broadcast> findByDate(LocalDate date){
        return find("select b from Broadcast b where b.startingDate=:date", Parameters.with("date", date).map()).list();
    }

    public List<Broadcast> findByDateExcluding(LocalDate date, Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("date", date);
        params.put("id", id);
        return find("select b from Broadcast b where b.startingDate=:date and b.id != :id", params).list();  // exclude from check when update
    }

}
