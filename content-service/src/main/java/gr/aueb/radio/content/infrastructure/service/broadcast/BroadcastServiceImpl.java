package gr.aueb.radio.content.infrastructure.service.broadcast;

import gr.aueb.radio.content.application.BroadcastService;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.AdBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BroadcastServiceImpl implements BroadcastService {

    @Inject
    @RestClient
    BroadcastApi broadcastApi;

    @Override
    public void deleteSongBroadcastsBySongId(String auth, Integer songId) {
        try {
            broadcastApi.deleteSongBroadcastsBySongId(auth, songId);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching content api.", 424);
        }
    }

    @Override
    public void deleteAdBroadcastsByAdId(String auth, Integer adId) {
        try {
            broadcastApi.deleteAdBroadcastsByAdId(auth, adId);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching content api.", 424);
        }
    }

    @Override
    public List<SongBroadcastBasicRepresentation> getSongBroadcastsBySongId(String auth, Integer id) {
        try {
            return broadcastApi.getSongBroadcastsBySongId(auth, id);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching broadcast api.", 424);
        } catch (ResteasyWebApplicationException error) {
            Log.info("Can't reach broadcast api due to timeout exception");
            throw new RadioException("Timeout exception.", 400);
        }
    }


    @Override
    public List<AdBroadcastBasicRepresentation> getAdBroadcastsByAdId(String auth, Integer id) {
        try {
            return broadcastApi.getAdBroadcastsByAdId(auth, id);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching broadcast api.", 424);
        } catch (ResteasyWebApplicationException error) {
            Log.info("Can't reach broadcast api due to timeout exception");
            throw new RadioException("Timeout exception.", 400);
        }
    }
}
