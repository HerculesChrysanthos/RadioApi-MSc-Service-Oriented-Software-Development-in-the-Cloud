package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.application.ContentService;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import org.jboss.logging.Logger;
@ApplicationScoped
public class ContentServiceImpl implements ContentService {

    @Inject
    Logger logger;

    @Inject
    @RestClient
    ContentApi contentApi;

    @Override
    public AdBasicRepresentation getAdId(String auth, Integer adId) {
        try {
            AdBasicRepresentation ad=  contentApi.getAdId(auth, adId);

            return ad;
        } catch (ProcessingException error) {
            throw new RadioException("Problem on reaching content api.", 424);
    } catch (NotFoundException error) {
        throw new RadioException("not found", 404);
    }
    }

}
