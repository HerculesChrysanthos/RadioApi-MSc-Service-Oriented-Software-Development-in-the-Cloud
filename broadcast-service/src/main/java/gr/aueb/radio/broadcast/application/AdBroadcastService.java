package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.NotFoundRadioException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdBroadcastCreationDTO;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.hibernate.annotations.EmbeddableInstantiator;

import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class AdBroadcastService {
    @Inject
    BroadcastService broadcastService;

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    UserService userService;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    ContentService contentService;

//    @Inject
//    AdRepository adRepository;

    @Transactional
    public AdBroadcast create(AdBroadcastCreationDTO dto, String auth) {
        // verify auth
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }
        // call content
        AdBasicRepresentation ad = contentService.getAd(auth, dto.adId);
        System.out.println("adId " + ad.id);
        System.out.println("adId " + ad.timezone);
        if (ad == null) {
            throw new NotFoundException("Ad does not exist");
        }

        Broadcast broadcast = broadcastRepository.findById(dto.broadcastId);
        StringBuilder adsIds = new StringBuilder();
        for (int i = 0; i < broadcast.getAdBroadcasts().size(); i++) {
            int adId = broadcast.getAdBroadcasts().get(i).getAdId();

            adsIds.append(adId);
            if (i != broadcast.getAdBroadcasts().size() - 1) {
                adsIds.append(",");
            }
        }
        List<AdBasicRepresentation> broadcastAds = contentService.getAdsByFilters(auth, null, adsIds.toString());


        return broadcastService.scheduleAd(dto.broadcastId, ad, DateUtil.setTime(dto.startingTime), broadcastAds);
    }

    @Transactional
    public AdBroadcast find(Integer id, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        AdBroadcast adBroadcast = adBroadcastRepository.findByIdDetails(id);
        if (adBroadcast == null) {
            throw new NotFoundException("Ad Broadcast does not exist");
        }
        return adBroadcast;
    }

    @Transactional
    public void delete(Integer id, String auth) {
        // verify auth
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        AdBroadcast adBroadcast = adBroadcastRepository.findById(id);
        if (adBroadcast == null) {
            throw new NotFoundException("Ad Broadcast does not exist");
        }
        broadcastService.removeAdBroadcast(adBroadcast.getBroadcast().getId(), adBroadcast.getId());
    }

    @Timeout(1000)
    @Transactional
    public List<AdBroadcast> search(String date, Integer adId, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        LocalDate dateToSearch;
        if (date == null) {
            dateToSearch = null;
        } else {
            dateToSearch = DateUtil.setDate(date);
        }
        return adBroadcastRepository.findByFilters(dateToSearch, adId);
    }

    @Transactional
    public void deleteByFilters(String auth, Integer adId) {
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        List<AdBroadcast> ab = adBroadcastRepository.findByAdId(adId);
        if (ab.size() == 0) {
            throw new NotFoundException("Ad Broadcast does not exist");
        }
        adBroadcastRepository.deleteByAdId(adId);
    }
}
