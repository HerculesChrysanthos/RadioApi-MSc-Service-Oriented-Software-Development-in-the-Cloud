package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;

import java.util.List;

public interface ContentService {

    AdBasicRepresentation getAd(String auth, Integer adId);

    SongBasicRepresentation getSong(String auth, Integer songId);

    List<SongBasicRepresentation> getSongsByIds(String auth, String songsIds);
}
