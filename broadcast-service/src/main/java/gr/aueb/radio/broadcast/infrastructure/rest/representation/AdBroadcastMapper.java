package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {BroadcastMapper.class, AdMapper.class})
public abstract  class AdBroadcastMapper {

    @Mapping(target = "broadcastTime", source = "broadcastTime", qualifiedByName = "stringTimeFormatter")
    @Mapping(target = "broadcastDate", source = "broadcastDate", qualifiedByName = "stringDateFormatter")
    public abstract AdBroadcast toModel(AdBroadcastRepresentation representation);

    @Mapping(target = "broadcastTime", source = "broadcastTime", qualifiedByName = "timeFormatter")
    @Mapping(target = "broadcastDate", source = "broadcastDate", qualifiedByName = "dateFormatter")
    public abstract AdBroadcastRepresentation toRepresentation(AdBroadcast adBroadcast);

    public abstract List<AdBroadcastRepresentation> toRepresentationList(List<AdBroadcast> adBroadcasts);

}
