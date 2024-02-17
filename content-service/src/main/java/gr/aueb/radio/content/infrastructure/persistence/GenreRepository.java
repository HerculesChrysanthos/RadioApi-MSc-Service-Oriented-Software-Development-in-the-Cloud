package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.genre.Genre;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public class GenreRepository implements PanacheRepositoryBase<Genre, Integer> {

}
