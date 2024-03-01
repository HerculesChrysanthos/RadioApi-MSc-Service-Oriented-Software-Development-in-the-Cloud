package gr.aueb.radio.content.infrastructure.rest.representation;

import gr.aueb.radio.content.domain.genre.Genre;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
        , uses = {SongMapper.class}
)

public abstract class GenreMapper {

    public abstract GenreRepresentation toRepresentation(Genre genre);

    public abstract Genre toModel(GenreRepresentation representation);
    public abstract List<GenreRepresentation> toRepresentationList(List<Genre> genres);

}
