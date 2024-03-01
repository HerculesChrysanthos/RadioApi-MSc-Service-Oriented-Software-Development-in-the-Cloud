package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class BroadcastOutputRepresentation {

    public Integer id;
    public Integer duration;
    public String startingDate;
    public String startingTime;
    public BroadcastType type;
    public Integer userId;
    public Integer genreId;
    public List<SongBroadcastRepresentation> songBroadcasts;
    public List<AdBroadcastRepresentation> adBroadcasts;
 }
