package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastOutputRepresentation;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.OutputBroadcastMapper;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdStatsDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@RequestScoped
public class StatService {

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    OutputBroadcastMapper broadcastMapper;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    ContentService contentService;

    @Inject
    UserService userService;

    private List<AdBasicRepresentation> extractFromAdBroadcast(List<AdBroadcast> adBroadcasts, String auth) {
        Set<Integer> adIds = new HashSet<>();
        for (AdBroadcast ab : adBroadcasts) {
            adIds.add(ab.getAdId());
        }

        List<AdBasicRepresentation> broadcastAds = contentService.getAdsByFilters(auth, null, adIds.toString());
        return broadcastAds;
    }

    @Transactional
    public List<BroadcastOutputRepresentation> getDailySchedule(String date, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to change this.", 403);
        }

        LocalDate dateToSearch;
        if (date == null) {
            dateToSearch = DateUtil.dateNow();
        } else {
            dateToSearch = DateUtil.setDate(date);
        }

        List<Broadcast> broadcastsOfDay = broadcastRepository.findByDate(dateToSearch);
        return broadcastMapper.toRepresentationList(broadcastsOfDay);
    }

    @Transactional
    public AdStatsDTO extractAdStats(String date, String auth) {
        LocalDate dateToSearch;
        if (date == null) {
            dateToSearch = DateUtil.dateNow();
        } else {
            dateToSearch = DateUtil.setDate(date);
        }
        AdStatsDTO dto = new AdStatsDTO();
//        List<ZoneEnum> timezones = Arrays.asList(ZoneEnum.values());
//        for (ZoneEnum zone : timezones) {
//            List<AdBroadcast> adBroadcasts = adBroadcastRepository.findByTimezoneDate(zone, dateToSearch);
//            List<AdBasicRepresentation> ads = extractFromAdBroadcast(adBroadcasts);
//            dto.adsPerTimeZone.put(zone, adMapper.toRepresentationList(ads));
//        }
//        List<BroadcastEnum> broadcastEnums = Arrays.asList(BroadcastEnum.values());
//        for (BroadcastEnum type : broadcastEnums) {
//            List<AdBroadcast> adBroadcasts = adBroadcastRepository.findByTypeDate(type, dateToSearch);
//            List<Ad> ads = extractFromAdBroadcast(adBroadcasts);
//            dto.adsPerBroadcastZone.put(type, adMapper.toRepresentationList(ads));
//        }
//        List<AdBroadcast> broadcastsOfDay = adBroadcastRepository.findByDate(dateToSearch);
//        List<Ad> ads = extractFromAdBroadcast(broadcastsOfDay, auth);
//        dto.totalDailyAds = ads.size();
        return dto;
    }

}
