package gr.aueb.radio.services;

import gr.aueb.radio.domains.Broadcast;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatService {
    @Transactional
    public void getDailySchedule (LocalDate startingDate){

        List<Broadcast> broadcastsOfDay = new ArrayList<>();
        /* to be continued */
    }
}
