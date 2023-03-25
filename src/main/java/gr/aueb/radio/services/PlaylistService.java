package gr.aueb.radio.services;

import gr.aueb.radio.domains.*;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.persistence.SongRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RequestScoped
public class PlaylistService {

    @Inject
    SongRepository songRepository;

    @Inject
    AdRepository adRepository;
    private String getRandomGenre(){
        Random rand = new Random();
        List<String> genres = songRepository.getAllGenres();
        int randomIndex = rand.nextInt(genres.size());
        return genres.get(randomIndex);
    }
    public void populateBroadcast(Broadcast broadcast){
        String genre = getRandomGenre();
        boolean adScheduled = false;
        List<Song> possibleSongs = songRepository.findSongsByGenre(genre);
        Collections.shuffle(possibleSongs);
        Iterator<Song> songsIterator = possibleSongs.iterator();
        List<Ad> possibleAds = adRepository.findByTimezone(broadcast.getTimezone());
        Collections.shuffle(possibleAds);
        Iterator<Ad> adsIterator = possibleAds.iterator();
        LocalTime trackedTime = broadcast.getStartingTime();
        //first loop
        while(songsIterator.hasNext() && broadcast.getAllocatedTime() < broadcast.getDuration()){
            Song song = songsIterator.next();
            SongBroadcast sb = broadcast.createSongBroadcast(song, trackedTime);
            if(sb != null){
                trackedTime = trackedTime.plusMinutes(song.getDuration()).plusMinutes(1);
                songsIterator.remove();
                possibleSongs.remove(song);
            }
            if(broadcast.getAllocatedTime() >= broadcast.getDuration() / 2 && !adScheduled){
                // time to add an ad
                while (!adScheduled && adsIterator.hasNext()){
                    Ad ad = adsIterator.next();
                    AdBroadcast ab = broadcast.createAdBroadcast(ad, trackedTime);
                    if(ab != null){
                        trackedTime = trackedTime.plusMinutes(ad.getDuration()).plusMinutes(1);
                        adScheduled = true;
                    }
                }
            }
        }

        songsIterator = possibleSongs.iterator();
        //second loop
        while(songsIterator.hasNext() && broadcast.getAllocatedTime() < broadcast.getDuration()){
            Song song = songsIterator.next();
            SongBroadcast sb = broadcast.createSongBroadcast(song, trackedTime);
            if(sb != null){
                trackedTime = trackedTime.plusMinutes(song.getDuration()).plusMinutes(1);
                songsIterator.remove();
                possibleSongs.remove(song);
            }
        }
    }
}
