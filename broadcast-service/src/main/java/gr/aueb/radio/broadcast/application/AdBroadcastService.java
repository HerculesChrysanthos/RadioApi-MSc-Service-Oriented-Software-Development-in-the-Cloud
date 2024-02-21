package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.NotFoundRadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdBroadcastCreationDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class AdBroadcastService {
    @Inject
    BroadcastService broadcastService;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

//    @Inject
//    AdRepository adRepository;

//    @Transactional
//    public AdBroadcast create(AdBroadcastCreationDTO dto) {
//       // Ad ad = adRepository.findById(dto.addId);
////        if (ad == null){
////            throw new NotFoundException("Ad does not exist");
////        }
//        AdBroadcast adBroadcast = broadcastService.scheduleAd(dto.broadcastId, ad, DateUtil.setTime(dto.startingTime));
//        return adBroadcast;
//    }

    @Transactional
    public AdBroadcast find(Integer id) {
        AdBroadcast adBroadcast = adBroadcastRepository.findByIdDetails(id);
        if (adBroadcast == null){
            throw new NotFoundRadioException("Ad Broadcast does not exist");
        }
        return adBroadcast;
    }

//    @Transactional
//    public void delete(Integer id) {
//        AdBroadcast adBroadcast = adBroadcastRepository.findById(id);
//        if (adBroadcast == null){
//            throw new NotFoundException("Ad Broadcast does not exist");
//        }
//        broadcastService.removeAdBroadcast(adBroadcast.getBroadcast().getId(), adBroadcast.getId());
//    }


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
