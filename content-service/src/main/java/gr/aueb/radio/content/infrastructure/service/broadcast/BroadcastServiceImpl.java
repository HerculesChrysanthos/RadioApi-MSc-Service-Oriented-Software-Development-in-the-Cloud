package gr.aueb.radio.content.infrastructure.service.broadcast;

import gr.aueb.radio.content.application.BroadcastService;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.infrastructure.rest.resource.AdResource;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.AdBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class BroadcastServiceImpl implements BroadcastService {

    @Inject
    @RestClient
    BroadcastApi broadcastApi;

    private static final Logger LOGGER = Logger.getLogger(BroadcastServiceImpl.class);
    AtomicLong counter = new AtomicLong(1);

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

    @Fallback(fallbackMethod = "zeroAdBroadcastsFallback")
    @Override
    public List<AdBroadcastBasicRepresentation> getAdBroadcastsByAdId(String auth, Integer id) {
        final Long invocationNumber = counter.getAndIncrement();
        try {
            LOGGER.infof("Calling Broadcast Api getAdBroadcastsByAdId");
            return broadcastApi.getAdBroadcastsByAdId(auth, id);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching broadcast api.", 424);
        } catch (ResteasyWebApplicationException error) {
            LOGGER.infof("Can't reach broadcast api due to timeout exception");
            throw new RadioException("Timeout exception.", 408);
        }
    }

    public List<AdBroadcastBasicRepresentation> zeroAdBroadcastsFallback (String auth, Integer adId) {
        LOGGER.infof("Falling back to zeroAdBroadcastsFallback() returning an empty list");
        List<AdBroadcastBasicRepresentation> ab = new ArrayList<>();
        return ab ;
    }
}
