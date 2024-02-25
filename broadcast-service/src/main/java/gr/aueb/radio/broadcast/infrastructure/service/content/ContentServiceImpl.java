package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.application.AdBroadcastService;
import gr.aueb.radio.broadcast.application.ContentService;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ContentServiceImpl implements ContentService {

    @Inject
    Logger logger;

    @Inject
    @RestClient
    ContentApi contentApi;

    @Override
    public AdBasicRepresentation getAd(String auth, Integer adId) {
        try {
            return contentApi.getAd(auth, adId);

        } catch (ProcessingException error) {
            throw new ExternalServiceException("Problem on reaching content api.");
        } catch (WebApplicationException webApplicationException){
            throw new RadioException("Ad not found", webApplicationException.getResponse().getStatus());
        }
    }

    @Override
    public SongBasicRepresentation getSong(String auth, Integer songId) {
        try {
            return contentApi.getSongId(auth, songId);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching content api.", 424);
        } catch (NotFoundException error) {
            throw new RadioException("not found", 404);
        }
    }

    @Override
    public List<SongBasicRepresentation> getSongsByFilters(String auth, String artist, String genre, String title, String songsIds) {
        try {
            return contentApi.getSongsByFilters(auth, artist, genre, title, songsIds);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching content api.", 424);
        } catch (NotFoundException error) {
            throw new RadioException("not found", 404);
        }
    }

    @Override
    public List<AdBasicRepresentation> getAdsByIds(String auth, String adsIds) {
        try {
            return contentApi.getAdsByIds(auth, adsIds);
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching content api.", 424);
        } catch (NotFoundException error) {
            throw new RadioException("not found", 404);
        }
    }

}
