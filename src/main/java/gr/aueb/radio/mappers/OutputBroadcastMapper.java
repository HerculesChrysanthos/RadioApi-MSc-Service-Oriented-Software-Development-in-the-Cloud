package gr.aueb.radio.mappers;

import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SongBroadcastMapper.class, BroadcastMapper.class, AdBroadcastMapper.class}
)
public abstract class OutputBroadcastMapper {

    @Mapping(target = "startingTime", source = "startingTime", qualifiedByName = "timeFormatter")
    @Mapping(target = "startingDate", source = "startingDate", qualifiedByName = "dateFormatter")
    public abstract BroadcastOutputRepresentation toRepresentation(Broadcast broadcast);

    @Mapping(target = "startingTime", source = "startingTime", qualifiedByName = "timeFormatter")
    @Mapping(target = "startingDate", source = "startingDate", qualifiedByName = "dateFormatter")
    public abstract List<BroadcastOutputRepresentation> toRepresentationList(List<Broadcast> broadcastList);

}
