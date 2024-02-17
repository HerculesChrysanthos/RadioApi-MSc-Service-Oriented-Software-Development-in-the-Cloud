package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.genre.Genre;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class GenreRepository implements PanacheRepositoryBase<Genre, Integer> {

}
