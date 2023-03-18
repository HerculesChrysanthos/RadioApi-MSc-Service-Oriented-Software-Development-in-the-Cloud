package gr.aueb.radio.mappers;

import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.representations.SongBroadcastRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "cdi",
		injectionStrategy = InjectionStrategy.CONSTRUCTOR,
		uses = { SongMapper.class, BroadcastMapper.class}
)
public abstract class SongBroadcastMapper {
	@Mapping(target = "broadcastTime", source = "broadcastTime", qualifiedByName = "stringTimeFormatter")
	@Mapping(target = "broadcastDate", source = "broadcastDate", qualifiedByName = "stringDateFormatter")
	public abstract SongBroadcast toModel(SongBroadcastRepresentation songBroadcastRepresentation);

	@Mapping(target = "broadcastTime", source = "broadcastTime", qualifiedByName = "timeFormatter")
	@Mapping(target = "broadcastDate", source = "broadcastDate", qualifiedByName = "dateFormatter")
	public abstract SongBroadcastRepresentation toRepresentation(SongBroadcast songBroadcast);
	public abstract List<SongBroadcastRepresentation> toRepresentationList(List<SongBroadcast> songBroadcastListBroadcast);
}

