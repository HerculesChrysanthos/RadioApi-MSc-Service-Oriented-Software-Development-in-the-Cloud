package gr.aueb.radio.content.infrastructure.service.broadcast;

import gr.aueb.radio.content.application.BroadcastService;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.AdBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BroadcastServiceImpl implements BroadcastService {

    @Inject
    @RestClient
    BroadcastApi broadcastApi;

    @Override
    public Optional deleteSongBroadcastsBySongId(String auth, Integer songId) {
        return Optional.empty();
    }

    @Override
    public Optional deleteAdBroadcastsByAdId(String auth, Integer adId) {
        return Optional.empty();
    }

    @Override
    public List<SongBroadcastBasicRepresentation> getSongBroadcastsBySongId(String auth, Integer id) {
        try {
            return broadcastApi.getSongBroadcastsBySongId(auth, id);
        }  catch (ProcessingException error) {
            throw new RadioException("Problem on reaching content api.", 424);
        }
    }

    @Override
    public List<AdBroadcastBasicRepresentation> getAdBroadcastsByAdId(String auth, Integer id) {
        return null;
    }
}
