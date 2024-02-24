package gr.aueb.radio.content.application;

import gr.aueb.radio.content.common.NotFoundException;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.infrastructure.persistence.GenreRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class GenreService {

    @Inject
    public
    GenreRepository genreRepository;

    @Inject
    public
    GenreMapper genreMapper;

    public GenreRepresentation getGenreById(Integer genreId){
        Genre foundGenre = genreRepository.findById(genreId);

        if(foundGenre ==null){
            throw new NotFoundException("Genre not found");
        }

        return genreMapper.toRepresentation(foundGenre);
    }

}
