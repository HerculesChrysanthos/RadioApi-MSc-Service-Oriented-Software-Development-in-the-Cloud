package gr.aueb.radio.services;

import gr.aueb.radio.domains.AddBroadcast;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.mappers.AdBroadcastMapper;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.representations.AdBroadcastRepresentation;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class AdBroadcastService {
    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    AdBroadcastMapper adBroadcastMapper;


    @Transactional
    public AdBroadcastRepresentation findAddBroadcast(Integer id){
        AddBroadcast addBroadcast = adBroadcastRepository.findById(id);
        if (addBroadcast == null) {
            throw new NotFoundException("AddBroadcast not found");
        }
        return adBroadcastMapper.toRepresentation(addBroadcast);
    }
}
