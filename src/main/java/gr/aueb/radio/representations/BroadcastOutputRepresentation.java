package gr.aueb.radio.representations;

import gr.aueb.radio.enums.BroadcastEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class BroadcastOutputRepresentation {

    public Integer id;
    public Integer duration;
    public String startingDate;
    public String startingTime;
    public BroadcastEnum type;
    public List<SongBroadcastRepresentation> songBroadcasts;
    public List<AdBroadcastRepresentation> adBroadcasts;
}
