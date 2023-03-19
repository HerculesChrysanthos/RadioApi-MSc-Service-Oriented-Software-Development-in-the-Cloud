package gr.aueb.radio.services;

import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.SongBroadcastMapper;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.representations.SongBroadcastRepresentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

public class SongBroadcastService {
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    SongBroadcastMapper songBroadcastMapper;

    
    @Transactional
    public SongBroadcastRepresentation findSongBroadcast(Integer id){
        SongBroadcast songBroadcast = songBroadcastRepository.findById(id);
        if (songBroadcast == null) {
            throw new NotFoundException("SongBroadcast not found");
        }
        return songBroadcastMapper.toRepresentation(songBroadcast);
    }

    @Transactional 
    public SongBroadcast create(SongBroadcastRepresentation songBroadcastRepresentation) {
        SongBroadcast songBroadcast = songBroadcastMapper.toModel(songBroadcastRepresentation);
        if (songBroadcastRepository.findById(songBroadcast.getId()) != null) {
            throw new RadioException("SongBroadcast already exists");
        }
        songBroadcastRepository.persist(songBroadcast);
        return songBroadcast;
    
    }
}
