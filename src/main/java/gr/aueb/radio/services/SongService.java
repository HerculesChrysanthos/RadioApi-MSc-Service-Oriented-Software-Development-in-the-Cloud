package gr.aueb.radio.services;

import gr.aueb.radio.domains.Song;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.SongMapper;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.representations.SongRepresentation;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped

public class SongService {
    @Inject
    SongRepository songRepository;

    @Inject
    SongMapper songMapper;

    @Transactional
    public Song create(SongRepresentation songRepresentation){
        Song song = songMapper.toModel(songRepresentation);
        if (songRepository.findSongByTitle(song.getTitle()) != null){
            throw new RadioException("A Song with that title already exists");
        }
        if (songRepository.findSongsByArtist(song.getArtist()) != null){
            throw new RadioException("A Song with this artist already exists");
        }
        songRepository.persist(song);
        return song;
    }

    @Transactional
    public SongRepresentation findSong(Integer Id){
        Song song = songRepository.findById(Id);
        if (song == null) {
            throw new NotFoundException("Song not found");
        }
        return songMapper.toRepresentation(song);
    }
    
      public PanacheQuery<Song> findSongs(String title) {
        return songRepository.find(title);
    }
}
