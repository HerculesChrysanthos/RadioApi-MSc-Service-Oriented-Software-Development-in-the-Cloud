package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import gr.aueb.radio.broadcast.domain.broadcast.Zone;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdStatsDTO {
    public Map<BroadcastType, List<AdBasicRepresentation>> adsPerBroadcastZone;
    public Map<Zone, List<AdBasicRepresentation>> adsPerTimeZone;

    public int totalDailyAds;

    public AdStatsDTO(){
        adsPerTimeZone = new HashMap<>();
        adsPerBroadcastZone = new HashMap<>();
        totalDailyAds = 0;
    }
}
