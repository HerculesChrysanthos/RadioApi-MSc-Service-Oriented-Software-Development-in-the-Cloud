package gr.aueb.radio.content.application;

import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.common.NotFoundException;
import gr.aueb.radio.content.common.DateUtil;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.*;


import java.util.List;
@RequestScoped
public class AdService {
    @Inject
    AdRepository adRepository;

//    @Inject
//    BroadcastService broadcastService;

    @Inject
    AdMapper adMapper;

    @Inject
    UserService userService;


    @Transactional
    public AdRepresentation findAd(Integer id, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to change this.", 403);
        }

        Ad ad = adRepository.findById(id);
        if (ad == null) {
            throw new NotFoundException("Ad not found");
        }
        return adMapper.toRepresentation(ad);
    }

    @Transactional
    public List<AdRepresentation> search(Zone timezone, String auth) {
        List<Ad> adsByTimezone;
        // verify user role - producer
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to change this.", 403);
        }

        if (timezone == null) {
            adsByTimezone = adRepository.listAll();
        } else {
            adsByTimezone = adRepository.findByTimezone(timezone);
        }
        return adMapper.toRepresentationList(adsByTimezone);
    }

    @Transactional
    public Ad create(AdRepresentation adRepresentation, String auth) {
        Ad ad = adMapper.toModel(adRepresentation);

//        userService.verifyAuth(auth);

        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to change this.", 403);
        }

        adRepository.persist(ad);
        return ad;
    }

    @Transactional
    public Ad update(Integer id, AdRepresentation adRepresentation, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to change this.", 403);
        }

        Ad ad = adRepository.findById(id);
        if(ad == null){
            throw new NotFoundException("Ad not found");
        }
        if (ad.getBroadcastAds() != null && ad.getBroadcastAds().size() != 0){
            throw new RadioException("Ad is immutable, it has scheduled broadcasts");
        }
        ad.setDuration(adRepresentation.duration);
        ad.setTimezone(adRepresentation.timezone);
        ad.setRepPerZone(adRepresentation.repPerZone);

        if(adRepresentation.startingDate != null){
            ad.setStartingDate(DateUtil.setDate(adRepresentation.startingDate));
        }

        if(adRepresentation.endingDate !=null){
            ad.setEndingDate(DateUtil.setDate(adRepresentation.endingDate));
        }

        adRepository.getEntityManager().merge(ad);
        return ad;
    }

    @Transactional
    public void delete(Integer id, String auth ) {
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to change this.", 403);
        }

        Ad ad = adRepository.findById(id);
        if(ad == null){
            throw new NotFoundException("Ad not found");
        }
//        List<AdBroadcast> adBroadcasts = ad.getBroadcastAds();
//        while (ad.getBroadcastAds().size() != 0){
//            AdBroadcast adBroadcast = ad.getBroadcastAds().get(0);
//            broadcastService.removeAdBroadcast(adBroadcast.getBroadcast().getId(), adBroadcast.getId());
//        }

        adRepository.deleteById(id);
    }

}
