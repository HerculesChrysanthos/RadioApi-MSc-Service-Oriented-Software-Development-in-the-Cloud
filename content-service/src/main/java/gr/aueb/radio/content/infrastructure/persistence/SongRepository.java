package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class SongRepository implements PanacheRepositoryBase<Song, Integer> {

    public List<Song> findSongsByGenre(String genre) {
        PanacheQuery<Song> query = find("select s from Song s where s.genre = :genre", Parameters.with("genre", genre).map());
        return query.list();
    }

    public List<String> getAllGenres(){
        return find("select distinct song.genre from Song song group by song.genre having count(*) >= 10").project(String.class).list();
    }

    public List<Song> findSongsByIds(List<Integer> ids) {
        PanacheQuery<Song> query = find("select s from Song s left join fetch s.genre where s.id IN :ids ", Parameters.with("ids", ids).map());

        return query.list();
    }

    public List<Song> findByFilters(String artist, Integer genreId, String genreTitle, String title, List<Integer> songsIds) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder("select s from Song s left join fetch s.genre where 1=1");

        if (artist != null) {
            queryBuilder.append(" and s.artist = :artist");
            params.put("artist", artist);
        }

        if (genreId != null) {
            queryBuilder.append(" and s.genre.id = :genreId");
            params.put("genreId", genreId);
        }

        if (genreTitle != null) {
            queryBuilder.append(" and s.genre.title = :genreTitle");
            params.put("genreTitle", genreTitle);
        }

        if (title != null) {
            queryBuilder.append(" and s.title = :title");
            params.put("title", title);
        }

        if (songsIds != null && !songsIds.isEmpty()) {
            queryBuilder.append(" and s.id in :songsIds");
            params.put("songsIds", songsIds);
        }

        PanacheQuery<Song> query = find(queryBuilder.toString(), params);
        return query.list();
    }



//    public Song findByIdDetails(Integer id) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", id);
//
//        return find("select s from Song s left join fetch s.genre where s.id = :id", params).singleResult();
//    }
}