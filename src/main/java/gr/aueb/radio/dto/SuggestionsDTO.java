package gr.aueb.radio.dto;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.domains.Song;
import gr.aueb.radio.mappers.AdMapper;
import gr.aueb.radio.mappers.SongMapper;
import gr.aueb.radio.representations.AdRepresentation;
import gr.aueb.radio.representations.SongRepresentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SuggestionsDTO {

    public List<AdRepresentation> ads;
    public List<SongRepresentation> songs;

    public SuggestionsDTO(){
        ads = new ArrayList<>();
        songs = new ArrayList<>();
    }
}
