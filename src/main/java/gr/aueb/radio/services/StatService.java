package gr.aueb.radio.services;

import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.mappers.BroadcastMapper;
import gr.aueb.radio.persistence.BroadcastRepository;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.utils.DateUtil;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;


public class StatService {

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    BroadcastMapper broadcastMapper;

    @Transactional
    public  List<BroadcastRepresentation> getDailySchedule (LocalDate date) {

        if (date == null){
            date = DateUtil.dateNow();
        }

        List<Broadcast> broadcastsOfDay = broadcastRepository.findByDate(date);
        return broadcastMapper.toRepresentationList(broadcastsOfDay);
    }

}
