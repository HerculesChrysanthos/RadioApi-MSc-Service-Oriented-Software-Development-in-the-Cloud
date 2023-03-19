package gr.aueb.radio.services;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdMapper;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.representations.AdRepresentation;
import gr.aueb.radio.utils.DateUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class AdService {
    @Inject
    AdRepository adRepository;

    @Inject
    BroadcastService broadcastService;

    @Inject
    AdMapper adMapper;


    @Transactional
    public AdRepresentation findAd(Integer id) {
        Ad ad = adRepository.findById(id);
        if (ad == null) {
            throw new NotFoundException("Ad not found");
        }
        return adMapper.toRepresentation(ad);
    }

    @Transactional
    public List<AdRepresentation> search(ZoneEnum timezone) {
        List<Ad> adsByTimezone;
        if (timezone == null) {
            adsByTimezone = adRepository.listAll();
        } else {
            adsByTimezone = adRepository.findByTimezone(timezone);
        }
        return adMapper.toRepresentationList(adsByTimezone);
    }

    @Transactional
    public Ad create(AdRepresentation adRepresentation) {
        Ad ad = adMapper.toModel(adRepresentation);
        adRepository.persist(ad);
        return ad;
    }

    @Transactional
    public Ad update(Integer id, AdRepresentation adRepresentation) {
        Ad ad = adRepository.findById(id);
        if(ad == null){
            throw new NotFoundException("Ad not found");
        }
        if (ad.getBroadcastAds().size() != 0){
            throw new RadioException("Ad is immutable, it has scheduled broadcasts");
        }
        ad.setDuration(adRepresentation.duration);
        ad.setTimezone(adRepresentation.timezone);
        ad.setRepPerZone(adRepresentation.repPerZone);
        ad.setStartingDate(DateUtil.setDate(adRepresentation.startingDate));
        ad.setEndingDate(DateUtil.setDate(adRepresentation.endingDate));
        adRepository.getEntityManager().merge(ad);
        return ad;
    }

    @Transactional
    public void delete(Integer id) {
        Ad ad = adRepository.findById(id);
        if(ad == null){
            throw new NotFoundException("Ad not found");
        }
        List<AdBroadcast> adBroadcasts = ad.getBroadcastAds();
        while (ad.getBroadcastAds().size() != 0){
            AdBroadcast adBroadcast = ad.getBroadcastAds().get(0);
            broadcastService.removeAdBroadcast(adBroadcast.getBroadcast().getId(), adBroadcast.getId());
        }

        adRepository.deleteById(id);
    }
}

