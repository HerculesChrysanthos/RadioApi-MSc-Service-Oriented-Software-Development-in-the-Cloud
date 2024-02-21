package gr.aueb.radio.broadcast.infrastructure.rest.representation;

//import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AdBroadcastRepresentation {
    public Integer id;
    public String broadcastDate;
    public String broadcastTime;
//    public AdRepresentation ad;
    public BroadcastRepresentation broadcast;
}