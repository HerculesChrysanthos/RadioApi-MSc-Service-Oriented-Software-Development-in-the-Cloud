package gr.aueb.radio.content.application;

import java.util.Optional;


public interface BroadcastService {

    Optional deleteSongBroadcastsBySongId(String auth, Integer songId);

    Optional deleteAdBroadcastsByAdId(String auth, Integer adId);
}
