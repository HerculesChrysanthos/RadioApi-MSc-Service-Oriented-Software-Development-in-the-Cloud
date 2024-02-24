package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequestScoped
public class PlaylistService {

//    @Inject
//    AdBroadcastRepository adBroadcastRepository;
//
//    @Inject
//    ContentService contentService;
//
//
//    private boolean adCanBeScheduled(Broadcast broadcast){
//        return adBroadcastRepository.findByTimezoneDate(broadcast.getTimezone(), broadcast.getStartingDate()).size() >= 4;
//    }
//    public void populateBroadcast(Broadcast broadcast){
//        // genre tha erxetai apo to request
//       // String genre = getRandomGenre();
//
//        // business validation me ta 4 max ads per broadcast
//        boolean adScheduled = adCanBeScheduled(broadcast);
//
//        // tha kalei to content songs api
//        List<SongBasicRepresentation> possibleSongs = songRepository.findSongsByGenre(broadcast.getGenreId());
//        Collections.shuffle(possibleSongs);
//        Iterator<SongBasicRepresentation> songsIterator = possibleSongs.iterator();
//
//        // content ads api
//        List<AdBasicRepresentation> possibleAds = adRepository.findByTimezone(broadcast.getTimezone());
//        Collections.shuffle(possibleAds);
//        Iterator<AdBasicRepresentation> adsIterator = possibleAds.iterator();
//
//
//        LocalTime trackedTime = broadcast.getStartingTime();
//        //first loop
//        while(songsIterator.hasNext() && broadcast.getAllocatedTime() < broadcast.getDuration()){
//            SongBasicRepresentation song = songsIterator.next();
//            SongBroadcast sb = broadcast.createSongBroadcast(song, trackedTime);
//            if(sb != null){
//                trackedTime = trackedTime.plusMinutes(song.getDuration()).plusMinutes(1);
//                songsIterator.remove();
//                possibleSongs.remove(song);
//            }
//            if(broadcast.getAllocatedTime() >= broadcast.getDuration() / 2 && !adScheduled){
//                // time to add an ad
//                while (!adScheduled && adsIterator.hasNext()){
//                    Ad ad = adsIterator.next();
//                    AdBroadcast ab = broadcast.createAdBroadcast(ad, trackedTime);
//                    if(ab != null){
//                        trackedTime = trackedTime.plusMinutes(ad.getDuration()).plusMinutes(1);
//                        adScheduled = true;
//                    }
//                }
//            }
//        }
//
//        songsIterator = possibleSongs.iterator();
//        //second loop
//        while(songsIterator.hasNext() && broadcast.getAllocatedTime() < broadcast.getDuration()){
//            Song song = songsIterator.next();
//            SongBroadcast sb = broadcast.createSongBroadcast(song, trackedTime);
//            if(sb != null){
//                trackedTime = trackedTime.plusMinutes(song.getDuration()).plusMinutes(1);
//                songsIterator.remove();
//                possibleSongs.remove(song);
//            }
//        }
//    }
}

