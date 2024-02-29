package gr.aueb.radio.content.application;

import gr.aueb.radio.content.common.NotFoundException;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.infrastructure.persistence.GenreRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class GenreService {

    @Inject
    public
    GenreRepository genreRepository;

    @Inject
    public
    GenreMapper genreMapper;

    @Inject
    UserService userService;

    @Transactional
    public GenreRepresentation getGenreById(Integer genreId, String auth){
        Genre foundGenre = genreRepository.findById(genreId);

        if(foundGenre ==null){
            throw new NotFoundException("Genre not found");
        }

        return genreMapper.toRepresentation(foundGenre);
    }

    @Transactional
    public List<GenreRepresentation> getAllGenres(String auth){
        // verify user role - producer
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to change this.", 403);
        }

        List<Genre> genres = genreRepository.findAll().list();
        return genreMapper.toRepresentationList(genres);
    }

}
