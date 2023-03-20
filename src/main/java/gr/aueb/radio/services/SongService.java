package gr.aueb.radio.services;

import gr.aueb.radio.domains.Song;
import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.SongMapper;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.representations.SongRepresentation;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
public class SongService {
    @Inject
    SongRepository songRepository;

    @Inject
    SongMapper songMapper;

    @Inject
    BroadcastService broadcastService;

    @Transactional
    public List<SongRepresentation> search(String artist, String genre, String title){
        List<Song> songs = songRepository.listAll();
        if(artist != null){
            // will filter and return all songs based on artist
            songs = songs.stream().filter(s -> s.getArtist().equals(artist)).collect(Collectors.toList());
        }
        if(genre != null){
            // will filter and return all songs based on genre
            songs = songs.stream().filter(s -> s.getGenre().equals(genre)).collect(Collectors.toList());
        }
        if(title != null){
            // will filter and return all songs based on title
            songs = songs.stream().filter(s -> s.getTitle().equals(title)).collect(Collectors.toList());
        }
        return songMapper.toRepresentationList(songs);
    }

    @Transactional
    public Song update(Integer id, SongRepresentation songRepresentation) {
        Song song = songRepository.findById(id);
        if(song == null){
            throw new NotFoundException("Song not found");
        }
        if (song.getSongBroadcasts().size() != 0){
            throw new RadioException("Song is immutable, it has scheduled broadcasts");
        }
        song.setGenre(songRepresentation.genre);
        song.setDuration(songRepresentation.duration);
        song.setArtist(songRepresentation.artist);
        song.setYear(songRepresentation.year);
        song.setTitle(songRepresentation.title);
        songRepository.getEntityManager().merge(song);
        return song;
    }

    @Transactional
    public void delete(Integer id) {
        Song song = songRepository.findById(id);
        if(song == null){
            throw new NotFoundException("Song not found");
        }
        while (song.getSongBroadcasts().size() != 0){
            SongBroadcast songBroadcast = song.getSongBroadcasts().get(0);
            broadcastService.removeSongBroadcast(songBroadcast.getBroadcast().getId(), songBroadcast.getId());
        }

        songRepository.deleteById(id);
    }


    @Transactional
    public SongRepresentation findSong(Integer Id){
        Song song = songRepository.findById(Id);
        if (song == null) {
            throw new NotFoundException("Song not found");
        }
        return songMapper.toRepresentation(song);
    }

    @Transactional
    public Song create(SongRepresentation songRepresentation) {
        Song song = songMapper.toModel(songRepresentation);
        songRepository.persist(song);
        return song;
    }
}
