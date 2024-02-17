package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class SongRepository implements PanacheRepositoryBase<Song, Integer> {

    public List<Song> findSongsByGenre(String genre) {
        PanacheQuery<Song> query = find("select s from Song s where s.genre = :genre", Parameters.with("genre", genre).map());
        return query.list();
    }

    public List<String> getAllGenres(){
        return find("select distinct song.genre from Song song group by song.genre having count(*) >= 10").project(String.class).list();
    }


}