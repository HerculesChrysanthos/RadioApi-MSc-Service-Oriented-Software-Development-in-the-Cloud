package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class GenreRepository implements PanacheRepositoryBase<Genre, Integer> {

}
