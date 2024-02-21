package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.NotFoundRadioException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdBroadcastCreationDTO;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.annotations.EmbeddableInstantiator;

import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class AdBroadcastService {
    @Inject
    BroadcastService broadcastService;

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

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }
        // call content
        AdBasicRepresentation ad = contentService.getAdId(auth, dto.adId);
        System.out.println ("adId " + ad.id);
        if (ad == null){
            throw new NotFoundException("Ad does not exist");
        }

        AdBroadcast adBroadcast = broadcastService.scheduleAd(dto.broadcastId, ad, DateUtil.setTime(dto.startingTime));
        return adBroadcast;
//        return new AdBroadcast();
    }

    @Transactional
    public AdBroadcast find(Integer id, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }

        AdBroadcast adBroadcast = adBroadcastRepository.findByIdDetails(id);
        if (adBroadcast == null){
            throw new NotFoundRadioException("Ad Broadcast does not exist");
        }
        return adBroadcast;
    }

    @Transactional
    public void delete(Integer id, String auth) {
        // verify auth
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }

        AdBroadcast adBroadcast = adBroadcastRepository.findById(id);
        if (adBroadcast == null){
            throw new NotFoundException("Ad Broadcast does not exist");
        }
        broadcastService.removeAdBroadcast(adBroadcast.getBroadcast().getId(), adBroadcast.getId());
    }


    @Transactional
    public List<AdBroadcast> search(String date, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }

        LocalDate dateToSearch;
        if (date == null){
            dateToSearch = DateUtil.dateNow();
        }else{
            dateToSearch = DateUtil.setDate(date);
        }
        return adBroadcastRepository.findByDateDetails(dateToSearch);
    }
}
