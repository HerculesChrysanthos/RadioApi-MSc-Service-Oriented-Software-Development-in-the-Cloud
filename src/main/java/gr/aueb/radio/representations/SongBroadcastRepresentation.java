package gr.aueb.radio.representations;

import java.time.LocalDate;
import java.time.LocalTime;


import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
   
public class SongBroadcastRepresentation {
	public LocalDate broadcastDate;
    public LocalTime broadcastTime;
	public SongRepresentation song;	
    
}
