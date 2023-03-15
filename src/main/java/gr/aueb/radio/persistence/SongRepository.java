package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.Song;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;

@RequestScoped
public class SongRepository implements PanacheRepositoryBase<Song, Integer> {


    public Song findSongByTitle(String title) {
        PanacheQuery<Song> query = find("select song from Song  song where song.title = :title", Parameters.with("title", title).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public List<Song> findSongsByArtist(String artist) {
        PanacheQuery<Song> query = find("select s from Song s where s.artist = :artist", Parameters.with("artist", artist).map());
        try {
            return query.list();
        } catch(NoResultException ex) {
            return null;
        }

    }

    public List<Song> findSongsByGenre(String genre) {
        PanacheQuery<Song> query = find("select s from Song s where s.genre = :genre", Parameters.with("genre", genre).map());
        try {
            return query.list();
        } catch(NoResultException ex) {
            return null;
        }

    }

    public List<Song> searchSongByTitleAndArtist(String title, String artist) {
        PanacheQuery<Song> query = find("select song from Song song where song.title = :title and song.artist = :artist", 
            Parameters.with("title", title).and("artist", artist).map());
        return query.list();
    }

}