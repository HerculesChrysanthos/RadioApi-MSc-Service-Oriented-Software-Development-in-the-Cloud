package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SongBroadcastRepresentation {
    public Integer id;
    public String broadcastDate;
    public String broadcastTime;
    public Integer songId;
    public BroadcastRepresentation broadcast;
}