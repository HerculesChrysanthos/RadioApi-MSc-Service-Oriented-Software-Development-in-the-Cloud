package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsDTO {

    public List<AdBasicRepresentation> ads;
    public List<SongBasicRepresentation> songs;

    public SuggestionsDTO(){
        ads = new ArrayList<>();
        songs = new ArrayList<>();
    }
}
