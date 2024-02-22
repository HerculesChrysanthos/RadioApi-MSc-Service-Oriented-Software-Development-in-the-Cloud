package gr.aueb.radio.content.infrastructure.service.broadcast;

import gr.aueb.radio.content.application.BroadcastService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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
}
