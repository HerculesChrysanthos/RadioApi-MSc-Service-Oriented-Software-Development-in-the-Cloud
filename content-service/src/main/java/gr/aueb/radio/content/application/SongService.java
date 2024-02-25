package gr.aueb.radio.content.application;

import gr.aueb.radio.content.common.NotFoundException;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.SongMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.SongRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class SongService {
    @Inject
    SongRepository songRepository;

    @Inject
    GenreService genreService;

    @Inject
    SongMapper songMapper;

    @Inject
    GenreMapper genreMapper;

    @Inject
    UserService userService;

    //@Inject
    //BroadcastService broadcastService;

    @Transactional
    public List<SongRepresentation> search(String artist, Integer genreId, String genreTitle, String title, List<Integer> songsIds, String auth){
        userService.verifyAuth(auth);

        List<Song> songs = songRepository.findByFilters(artist, genreId, genreTitle, title, songsIds);

        return songMapper.toRepresentationList(songs);
    }

    @Transactional
    public Song update(Integer id, SongRepresentation songRepresentation, String auth) {
        userService.verifyAuth(auth);

        Song song = songRepository.findById(id);
        if(song == null){
            throw new NotFoundException("Song not found");
        }
        if (song.getSongBroadcasts().size() != 0){
            throw new RadioException("Song is immutable, it has scheduled broadcasts");
        }

        Genre genre = genreMapper.toModel(genreService.getGenreById(songRepresentation.genreId));

        song.setGenre(genre);
        song.setDuration(songRepresentation.duration);
        song.setArtist(songRepresentation.artist);
        song.setYear(songRepresentation.year);
        song.setTitle(songRepresentation.title);
        songRepository.getEntityManager().merge(song);
        return song;
    }

    @Transactional
    public void delete(Integer id, String auth) {
        userService.verifyAuth(auth);

        Song song = songRepository.findById(id);
        if(song == null){
            throw new NotFoundException("Song not found");
        }

        // remove while , send one request for deleting songbroadcasts according to this song id
        while (song.getSongBroadcasts().size() != 0){
            //SongBroadcast songBroadcast = song.getSongBroadcasts().get(0);
           // broadcastService.removeSongBroadcast(songBroadcast.getBroadcast().getId(), songBroadcast.getId());
        }

        songRepository.deleteById(id);
    }


    @Transactional
    public SongRepresentation findSong(Integer Id, String auth){
        userService.verifyAuth(auth);

        Song song = songRepository.findById(Id);
        if (song == null) {
            throw new NotFoundException("Song not found");
        }
        return songMapper.toRepresentation(song);
    }

    @Transactional
    public Song create(SongRepresentation songRepresentation, String auth) {
        Song song = songMapper.toModel(songRepresentation);

        userService.verifyAuth(auth);

        songRepository.persist(song);
        return song;
    }
}