package gr.aueb.radio.services;

import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.domains.Song;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.persistence.BroadcastRepository;
import gr.aueb.radio.persistence.SongRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequestScoped
@Slf4j
public class SuggestionsService {

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    SongRepository songRepository;

    @Inject
    AdRepository adRepository;

    private String extractGenre(Broadcast broadcast){
        Random rand = new Random();
        if(broadcast.getSongBroadcasts().size() != 0){
            return broadcast.getSongBroadcasts().get(0).getSong().getGenre();
        }else {
            List<String> genres = songRepository.getAllGenres();
            int randomIndex = rand.nextInt(genres.size());
            return genres.get(randomIndex);
        }
    }

    @Transactional
    public List<Song> suggestSongs(Integer id){
        Broadcast broadcast = broadcastRepository.findByIdSongDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        String genre = extractGenre(broadcast);
        List<Song> songsOfSameGenre = songRepository.findSongsByGenre(genre);
        List<Song> suggestions = new ArrayList<>();
        for (Song song : songsOfSameGenre){
            if (broadcast.songCanBeAdded(song, broadcast.getStartingTime())){
                suggestions.add(song);
            }
        }
        return suggestions.stream().limit(15).collect(Collectors.toList());
    }

    @Transactional
    public List<Ad> suggestAds(Integer id){
        Broadcast broadcast = broadcastRepository.findByIdAdDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        ZoneEnum timezone = broadcast.getTimezone();
        List<Ad> adsOfSameTimezone = adRepository.findByTimezone(timezone);
        List<Ad> suggestions = new ArrayList<>();
        for (Ad ad : adsOfSameTimezone){
            if (broadcast.adCanBeAdded(ad, broadcast.getStartingTime())){
                suggestions.add(ad);
            }
        }
        return suggestions.stream().limit(15).collect(Collectors.toList());
    }
}
