package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = { BroadcastMapper.class}
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
