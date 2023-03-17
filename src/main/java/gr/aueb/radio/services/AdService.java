package gr.aueb.radio.services;

import gr.aueb.radio.domains.Add;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdMapper;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.representations.AdRepresentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class AdService {
    @Inject
    AdRepository adRepository;

    @Inject
    AdMapper adMapper;


    @Transactional
    public AdRepresentation findAdd(Integer id) {
        Add add = adRepository.findById(id);
        if (add == null) {
            throw new NotFoundException("Add not found");
        }
        return adMapper.toRepresentation(add);
    }

    @Transactional
    public List<AdRepresentation> search(ZoneEnum timezone) {
        List<Add> adsByTimezone;
        if (timezone == null) {
            adsByTimezone = adRepository.listAll();
        } else {
            adsByTimezone = adRepository.findByTimezone(timezone);
        }
        return adMapper.toRepresentationList(adsByTimezone);
    }

    @Transactional
    public Add create(AdRepresentation adRepresentation) {
        Add add = adMapper.toModel(adRepresentation);
        if (adRepository.findById(add.getId()) != null) {
            throw new RadioException("Add already exists");
        }
        else {
        }
        adRepository.persist(add);
        return add;
    }
}

