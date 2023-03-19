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
	public List<SongRepresentation> listAll() {
		return songMapper.toRepresentationList(songRepository.listAll());
	}

    @Transactional
    public List<SongRepresentation> findSongsByArtist(String artist) {
        return songMapper.toRepresentationList(songRepository.findSongsByArtist(artist));
    }

    @Transactional
    public List<SongRepresentation> findSongsByGenre(String genre) {
        return songMapper.toRepresentationList(songRepository.findSongsByGenre(genre));
    }
   
    @Transactional
    public SongRepresentation findSong(Integer Id){
        Song song = songRepository.findById(Id);
        if (song == null) {
            throw new NotFoundException("Song not found");
        }
        return songMapper.toRepresentation(song);
    }
    


}
