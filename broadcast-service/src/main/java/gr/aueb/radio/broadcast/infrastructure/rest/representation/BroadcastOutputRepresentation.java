package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BroadcastOutputRepresentation {

    public Integer id;
    public Integer duration;
    public String startingDate;
    public String startingTime;
    public BroadcastType type;
//    public List<SongBroadcastRepresentation> songBroadcasts;
//    public List<AdBroadcastRepresentation> adBroadcasts;
//
 }
