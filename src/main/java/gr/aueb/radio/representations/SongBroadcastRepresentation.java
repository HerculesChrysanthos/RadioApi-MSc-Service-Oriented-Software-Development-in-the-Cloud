package gr.aueb.radio.representations;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SongBroadcastRepresentation {
	public Integer id;
	public String broadcastDate;
    public String broadcastTime;
	public SongRepresentation song;
	public BroadcastRepresentation broadcast;
}
