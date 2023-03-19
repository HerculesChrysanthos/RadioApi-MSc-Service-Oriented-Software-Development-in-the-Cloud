package gr.aueb.radio.services;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.dto.AdBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.utils.DateUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class AdBroadcastService {
    @Inject
    BroadcastService broadcastService;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    AdRepository adRepository;

    @Transactional
    public AdBroadcast create(AdBroadcastCreationDTO dto) {
        Ad ad = adRepository.findById(dto.addId);
        if (ad == null){
            throw new NotFoundException("Ad does not exist");
        }
        AdBroadcast adBroadcast = broadcastService.scheduleAd(dto.broadcastId, ad, DateUtil.setTime(dto.startingTime));
        return adBroadcast;
    }

    @Transactional
    public AdBroadcast find(Integer id) {
        AdBroadcast adBroadcast = adBroadcastRepository.findByIdDetails(id);
        if (adBroadcast == null){
            throw new NotFoundException("Ad Broadcast does not exist");
        }
        return adBroadcast;
    }

    @Transactional
    public void delete(Integer id) {
        AdBroadcast adBroadcast = adBroadcastRepository.findById(id);
        if (adBroadcast == null){
            throw new NotFoundException("Ad Broadcast does not exist");
        }
        broadcastService.removeAdBroadcast(adBroadcast.getBroadcast().getId(), adBroadcast.getId());
    }


    @Transactional
    public List<AdBroadcast> search(String date) {
        LocalDate dateToSearch;
        if (date == null){
            dateToSearch = DateUtil.dateNow();
        }else{
            dateToSearch = DateUtil.setDate(date);
        }
        return adBroadcastRepository.findByDateDetails(dateToSearch);
    }
}
