package gr.aueb.radio.representations;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SongBroadcastRepresentation {
	public String broadcastDate;
    public String broadcastTime;
	public SongRepresentation song;	
    
}
