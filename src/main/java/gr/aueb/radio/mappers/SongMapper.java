package gr.aueb.radio.mappers;

import gr.aueb.radio.domains.Song;
import gr.aueb.radio.representations.SongRepresentation;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi",
injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SongMapper {

    public abstract SongRepresentation toRepresentation(Song song);
    @Mapping(target = "id", ignore = true)
    public abstract Song toModel(SongRepresentation representation);
    public abstract List<SongRepresentation> toRepresentationList(List<Song> songs);

}

