package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;

import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequestScoped
public class PlaylistService {

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    ContentService contentService;

    private boolean adCanBeScheduled(Broadcast broadcast){
        return adBroadcastRepository.findByTimezoneDate(broadcast.getTimezone(), broadcast.getStartingDate()).size() >= 4;
    }
    public void populateBroadcast(Broadcast broadcast, String auth){
        // business validation me ta 4 max ads per broadcast
        boolean adScheduled = adCanBeScheduled(broadcast);

        List<SongBasicRepresentation> possibleSongs = contentService.getSongsByFilters(auth, null, broadcast.getGenreId(), null, null, null);
        Collections.shuffle(possibleSongs);
        Iterator<SongBasicRepresentation> songsIterator = possibleSongs.iterator();

        List<AdBasicRepresentation> possibleAds = contentService.getAdsByFilters(auth, broadcast.getTimezone().toString(), null);
        Collections.shuffle(possibleAds);
        Iterator<AdBasicRepresentation> adsIterator = possibleAds.iterator();


        LocalTime trackedTime = broadcast.getStartingTime();
        //first loop
        while(songsIterator.hasNext() && broadcast.getAllocatedTime(possibleAds, possibleSongs) < broadcast.getDuration()){
            SongBasicRepresentation song = songsIterator.next();
            // get songBroadcasts of this song ? maybe not. we want the sb of br until now. Are they attached with broadcast?
            List<SongBroadcast> songBroadcastsOfBr = songBroadcastRepository.searchBySongId(song.id);
            // get songBroadcasts of the day of broadcast
            List<SongBroadcast> songBroadcastsOfDay = songBroadcastRepository.findByDateDetails(broadcast.getStartingDate());

            List<Integer> broadcastSongIds = new ArrayList<>();
            for (SongBroadcast songBroadcast : songBroadcastsOfBr) {
                broadcastSongIds.add(songBroadcast.getSongId());
            }

            List<SongBasicRepresentation> existingBroadcastSongBroadcasts = new ArrayList<>();
            for (SongBasicRepresentation searchingSong: possibleSongs) {
                if (broadcastSongIds.contains(searchingSong.id)) {
                    existingBroadcastSongBroadcasts.add(searchingSong);
                }
            }

            SongBroadcast sb = broadcast.createSongBroadcast(
                    song, trackedTime, songBroadcastsOfBr, songBroadcastsOfDay, existingBroadcastSongBroadcasts
            );

            if(sb != null){
                trackedTime = trackedTime.plusMinutes(song.duration).plusMinutes(1);
                songsIterator.remove();
                possibleSongs.remove(song);
            }
            if(broadcast.getAllocatedTime(possibleAds, possibleSongs) >= broadcast.getDuration() / 2 && !adScheduled){
                // time to add an ad
                while (!adScheduled && adsIterator.hasNext()){
                    AdBasicRepresentation ad = adsIterator.next();

                    // maybe not List<AdBroadcast> adBroadcastsOfBr = adBroadcastRepository.findByIdDetails(ad.id);


                    AdBroadcast ab = broadcast.createAdBroadcast(ad, trackedTime);
                    if(ab != null){
                        trackedTime = trackedTime.plusMinutes(ad.duration).plusMinutes(1);
                        adScheduled = true;
                    }
                }
            }
        }

        songsIterator = possibleSongs.iterator();
        //second loop
        while(songsIterator.hasNext() && broadcast.getAllocatedTime(possibleAds, possibleSongs) < broadcast.getDuration()){
            SongBasicRepresentation song = songsIterator.next();
            SongBroadcast sb = broadcast.createSongBroadcast(song, trackedTime);
            if(sb != null){
                trackedTime = trackedTime.plusMinutes(song.duration).plusMinutes(1);
                songsIterator.remove();
                possibleSongs.remove(song);
            }
        }
    }
}

