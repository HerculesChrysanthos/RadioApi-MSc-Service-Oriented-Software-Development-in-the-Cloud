package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Zone;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Slf4j
public class SuggestionsService {

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    ContentService contentService;

    //    private String extractGenre(Broadcast broadcast){
//        Random rand = new Random();
//        if(broadcast.getSongBroadcasts().size() != 0){
//            return broadcast.getSongBroadcasts().get(0).getSong().getGenre();
//        }else {
//            List<String> genres = songRepository.getAllGenres();
//            int randomIndex = rand.nextInt(genres.size());
//            return genres.get(randomIndex);
//        }
//    }
    private static final Logger LOGGER = Logger.getLogger(SuggestionsService.class);

    @Transactional
    public List<SongBasicRepresentation> suggestSongs(Integer id, String auth) {
        try {
            Broadcast broadcast = broadcastRepository.findByIdSongDetails(id);
            if (broadcast == null) {
                throw new NotFoundException("Broadcast not found");
            }

            StringBuilder songsIds = new StringBuilder();
            for (int i = 0; i < broadcast.getSongBroadcasts().size(); i++) {
                int songId = broadcast.getSongBroadcasts().get(i).getSongId();

                songsIds.append(songId);
                if (i != broadcast.getSongBroadcasts().size() - 1) {
                    songsIds.append(",");
                }
            }
            LOGGER.infof("suggestSongs() call content - getSongsByFilters  ");

            List<SongBasicRepresentation> broadcastSongs = new ArrayList<>();
            if (!songsIds.toString().isEmpty()) {
//                System.out.println();
                broadcastSongs = contentService.getSongsByFilters(auth, null, null, null, null, songsIds.toString());
            }
            LOGGER.infof("suggestSongs() song retrieved getSongsByFilters from contentApi " + broadcastSongs.size());

            Integer genre = broadcast.getGenreId();
            List<SongBasicRepresentation> songsOfSameGenre = contentService.getSongsByFilters(auth, null, genre, null, null, null);
            LOGGER.infof("suggestSongs() song retrieved getSongsByFilters from contentApi instead of genre filter " + songsOfSameGenre.size());

            List<SongBasicRepresentation> suggestions = new ArrayList<>();
            for (SongBasicRepresentation song : songsOfSameGenre) {
                // get songBroadcasts of the day of broadcast
                List<SongBroadcast> songBroadcastsOfDay = songBroadcastRepository.findByDateDetails(broadcast.getStartingDate());
                if (broadcast.songCanBeAdded(song, broadcast.getStartingTime(), songBroadcastsOfDay, broadcastSongs)) {
                    suggestions.add(song);
                }
            }
            Collections.shuffle(suggestions);
            return suggestions.stream().limit(15).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("An error occurred while suggesting songs: ", e);
            // Handle the error or throw it further depending on your application logic
            throw new RuntimeException("Failed to suggest songs", e);
        }
    }

    @Transactional
    public List<AdBasicRepresentation> suggestAds(Integer id, String auth) {
        Broadcast broadcast = broadcastRepository.findByIdAdDetails(id);
        if (broadcast == null) {
            throw new NotFoundException("Broadcast not found");
        }

        StringBuilder adsIds = new StringBuilder();
        for (int i = 0; i < broadcast.getAdBroadcasts().size(); i++) {
            int adId = broadcast.getAdBroadcasts().get(i).getAdId();

            adsIds.append(adId);
            if (i != broadcast.getAdBroadcasts().size() - 1) {
                adsIds.append(",");
            }
        }
        LOGGER.infof("suggestAds() call content - getAdsByFilters  ");
        List<AdBasicRepresentation> broadcastAds = new ArrayList<>();
        if (!adsIds.toString().isEmpty()) {
            broadcastAds = contentService.getAdsByFilters(auth, null, adsIds.toString());
        }
        LOGGER.infof("suggestAds() ad retrieved getAdsByFilters from contentApi " + broadcastAds.size());


        Zone timezone = broadcast.getTimezone();
        List<AdBasicRepresentation> adsOfSameTimezone = contentService.getAdsByFilters(auth, timezone.toString(), null);
        LOGGER.infof("suggestAds() ad retrieved getAdsByFilters from contentApi instead of timezone filter " + adsOfSameTimezone.size());

        List<AdBasicRepresentation> suggestions = new ArrayList<>();
        for (AdBasicRepresentation ad : adsOfSameTimezone) {

            List<AdBroadcast> adBroadcastsOfDay = adBroadcastRepository.findByAdId(ad.id);
            if (broadcast.adCanBeAdded(ad, broadcast.getStartingTime(), adBroadcastsOfDay, broadcastAds)) {
                suggestions.add(ad);
            }
        }
        Collections.shuffle(suggestions);
        return suggestions.stream().limit(15).collect(Collectors.toList());
    }
}
