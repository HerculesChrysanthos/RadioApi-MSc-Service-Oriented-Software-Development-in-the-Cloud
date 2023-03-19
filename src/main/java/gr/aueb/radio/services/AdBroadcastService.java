package gr.aueb.radio.services;

import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdBroadcastMapper;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.representations.AdBroadcastRepresentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
public class AdBroadcastService {
    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    AdBroadcastMapper adBroadcastMapper;


    @Transactional
    public AdBroadcastRepresentation findAdBroadcast(Integer id){
        AdBroadcast adBroadcast = adBroadcastRepository.findById(id);
        if (adBroadcast == null) {
            throw new NotFoundException("AddBroadcast not found");
        }
        return adBroadcastMapper.toRepresentation(adBroadcast);
    }

    @Transactional
    public AdBroadcast create(AdBroadcastRepresentation adBroadcastRepresentation) {
        AdBroadcast adBroadcast = adBroadcastMapper.toModel(adBroadcastRepresentation);
        if (adBroadcastRepository.findById(adBroadcast.getId()) != null) {
            throw new RadioException("adBroadcast already exists");
        }
        adBroadcastRepository.persist(adBroadcast);
        return adBroadcast;
    }
}
