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
        System.out.println("possibleSongs " + possibleSongs.size());
        Collections.shuffle(possibleSongs);
        Iterator<SongBasicRepresentation> songsIterator = possibleSongs.iterator();
        List<SongBasicRepresentation> songsAdded = new ArrayList<>();

        List<AdBasicRepresentation> possibleAds = contentService.getAdsByFilters(auth, broadcast.getTimezone().toString(), null);
        Collections.shuffle(possibleAds);
        System.out.println("possibleAds " + possibleAds.size());

        Iterator<AdBasicRepresentation> adsIterator = possibleAds.iterator();
        List<AdBasicRepresentation> adsAdded = new ArrayList<>();

        LocalTime trackedTime = broadcast.getStartingTime();
        //first loop
        while(songsIterator.hasNext() && broadcast.getAllocatedTime(possibleAds, possibleSongs) < broadcast.getDuration()){
            SongBasicRepresentation song = songsIterator.next();
//            System.out.println("song in iterator " + song);

            // get songBroadcasts of this song ? maybe not. we want the sb of br until now. Are they attached with broadcast?
            // maybe when creating a sb in line 70 we can push it into an array of sb and use it as songBroadcastsOfBr. Same about ads below
           // List<SongBroadcast> songBroadcastsOfBr = broadcast.getSongBroadcasts();
            // get song's songBroadcasts of the day of broadcast
            List<SongBroadcast> songBroadcastsOfDay = songBroadcastRepository.findBySongIdDate(song.id, broadcast.getStartingDate());
//            System.out.println("ssongBroadcastsOfDay " + songBroadcastsOfDay);

            SongBroadcast sb = broadcast.createSongBroadcast(song, trackedTime, songBroadcastsOfDay, songsAdded);

            if(sb != null){
                trackedTime = trackedTime.plusMinutes(song.duration).plusMinutes(1);
                songsAdded.add(song);
                songsIterator.remove();
                possibleSongs.remove(song);
            }
            // how much time can be allocated to ads during the broadcast
            if(broadcast.getAllocatedTime(possibleAds, possibleSongs) >= broadcast.getDuration() / 2 && !adScheduled){
                // time to add an ad
                while (!adScheduled && adsIterator.hasNext()){
                    AdBasicRepresentation ad = adsIterator.next();
//                    System.out.println("ad in iterator " + ad);

                    List<AdBroadcast> adBroadcastsOfDay = adBroadcastRepository.findByAdId(ad.id);
//                    System.out.println("adBroadcastsOfDay " + adBroadcastsOfDay);


                    AdBroadcast ab = broadcast.createAdBroadcast(ad, trackedTime, adBroadcastsOfDay, adsAdded);
                    if(ab != null){
                        trackedTime = trackedTime.plusMinutes(ad.duration).plusMinutes(1);
                        adScheduled = true;
                        adsAdded.add(ad);
                    }
                }
            }
        }

        songsIterator = possibleSongs.iterator();
        //second loop
        while(songsIterator.hasNext() && broadcast.getAllocatedTime(possibleAds, possibleSongs) < broadcast.getDuration()){
            SongBasicRepresentation song = songsIterator.next();
            List<SongBroadcast> songBroadcastsOfDay = songBroadcastRepository.findBySongIdDate(song.id, broadcast.getStartingDate());

            SongBroadcast sb = broadcast.createSongBroadcast(song, trackedTime, songBroadcastsOfDay, songsAdded);
            if(sb != null){
                trackedTime = trackedTime.plusMinutes(song.duration).plusMinutes(1);
                songsAdded.add(song);
                songsIterator.remove();
                possibleSongs.remove(song);
            }
        }
    }
}

