package gr.aueb.radio.services;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.dto.AdStatsDTO;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.mappers.AdMapper;
import gr.aueb.radio.mappers.BroadcastMapper;
import gr.aueb.radio.mappers.OutputBroadcastMapper;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.persistence.BroadcastRepository;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.utils.DateUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    AdMapper adMapper;

    private List<Ad> extractFromAdBroadcast(List<AdBroadcast> adBroadcasts){
        Set<Ad> ads = new HashSet<>();
        for (AdBroadcast ab : adBroadcasts){
            ads.add(ab.getAd());
        }
        return new ArrayList<>(ads);
    }
    @Transactional
    public List<BroadcastOutputRepresentation> getDailySchedule (String date) {
        LocalDate dateToSearch;
        if (date == null){
            dateToSearch = DateUtil.dateNow();
        }else {
            dateToSearch = DateUtil.setDate(date);
        }

        List<Broadcast> broadcastsOfDay = broadcastRepository.findByDate(dateToSearch);
        return broadcastMapper.toRepresentationList(broadcastsOfDay);
    }

    @Transactional
    public AdStatsDTO extractAdStats(String date){
        LocalDate dateToSearch;
        if (date == null){
            dateToSearch = DateUtil.dateNow();
        }else {
            dateToSearch = DateUtil.setDate(date);
        }
        AdStatsDTO dto = new AdStatsDTO();
        List<ZoneEnum> timezones = Arrays.asList(ZoneEnum.values());
        for (ZoneEnum zone : timezones){
            List<AdBroadcast> adBroadcasts = adBroadcastRepository.findByTimezoneDate(zone, dateToSearch);
            List<Ad> ads = extractFromAdBroadcast(adBroadcasts);
            dto.adsPerTimeZone.put(zone, adMapper.toRepresentationList(ads));
        }
        List<BroadcastEnum> broadcastEnums = Arrays.asList(BroadcastEnum.values());
        for (BroadcastEnum type : broadcastEnums){
            List<AdBroadcast> adBroadcasts = adBroadcastRepository.findByTypeDate(type, dateToSearch);
            List<Ad> ads = extractFromAdBroadcast(adBroadcasts);
            dto.adsPerBroadcastZone.put(type, adMapper.toRepresentationList(ads));
        }
        List<AdBroadcast> broadcastsOfDay = adBroadcastRepository.findByDate(dateToSearch);
        List<Ad> ads = extractFromAdBroadcast(broadcastsOfDay);
        dto.totalDailyAds = ads.size();
        return dto;
    }

}
