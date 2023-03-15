package gr.aueb.radio.mappers;
import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.representations.SongBroadcastRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;


@Mapper(componentModel = "cdi",
injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SongBroadcastMapper {
	
	public abstract SongBroadcastRepresentation toRepresentation(SongBroadcast songbroadcast);

}

