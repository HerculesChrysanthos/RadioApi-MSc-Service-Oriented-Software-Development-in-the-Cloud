package gr.aueb.radio.representations;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AdBroadcastRepresentation {
    public Integer id;
    public String broadcastDate;
    public String broadcastTime;
    public AdRepresentation ad;
    public BroadcastRepresentation broadcast;
}
