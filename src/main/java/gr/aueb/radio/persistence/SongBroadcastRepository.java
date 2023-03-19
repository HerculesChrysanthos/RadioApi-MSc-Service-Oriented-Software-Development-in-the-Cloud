package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.SongBroadcast;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class SongBroadcastRepository implements PanacheRepositoryBase<SongBroadcast, Integer> {
}
