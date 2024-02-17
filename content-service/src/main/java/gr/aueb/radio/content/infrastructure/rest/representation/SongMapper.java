package gr.aueb.radio.content.infrastructure.rest.representation;

import gr.aueb.radio.content.domain.song.Song;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SongMapper {

    public abstract SongRepresentation toRepresentation(Song song);
    @Mapping(target = "id", ignore = true)
    public abstract Song toModel(SongRepresentation representation);
    public abstract List<SongRepresentation> toRepresentationList(List<Song> songs);

}
