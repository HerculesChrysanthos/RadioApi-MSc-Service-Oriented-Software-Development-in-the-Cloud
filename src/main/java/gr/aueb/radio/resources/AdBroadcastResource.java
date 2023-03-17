package gr.aueb.radio.resources;

import gr.aueb.radio.mappers.AdBroadcastMapper;
import gr.aueb.radio.persistence.AdBroadcastRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AdBroadcastResource {

    @Inject
    AdBroadcastMapper adBroadcastMapper;

    @Inject
    AdBroadcastRepository adBroadcastRepository;
}
