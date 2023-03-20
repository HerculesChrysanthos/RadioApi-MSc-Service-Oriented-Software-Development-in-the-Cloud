package gr.aueb.radio.dto;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.representations.AdRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdStatsDTO {
    public Map<BroadcastEnum, List<AdRepresentation>> adsPerBroadcastZone;
    public Map<ZoneEnum, List<AdRepresentation>> adsPerTimeZone;
    public int totalDailyAds;

    public AdStatsDTO(){
        adsPerTimeZone = new HashMap<>();
        adsPerBroadcastZone = new HashMap<>();
        totalDailyAds = 0;
    }

}
