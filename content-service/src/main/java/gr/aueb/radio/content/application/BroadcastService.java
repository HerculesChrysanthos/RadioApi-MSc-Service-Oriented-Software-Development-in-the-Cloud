package gr.aueb.radio.content.application;

import gr.aueb.radio.content.infrastructure.service.broadcast.representation.AdBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;

import java.util.List;
import java.util.Optional;


public interface BroadcastService {

    Optional deleteSongBroadcastsBySongId(String auth, Integer songId);

    Optional deleteAdBroadcastsByAdId(String auth, Integer adId);

    List<SongBroadcastBasicRepresentation> getSongBroadcastsBySongId(String auth, Integer id);

    List<AdBroadcastBasicRepresentation> getAdBroadcastsByAdId(String auth, Integer id);

}
