package gr.aueb.radio.domains;

import gr.aueb.radio.utils.DateUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name="songs")
public class Song {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "artist", nullable = false, length = 50)
    private String artist;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "genre", nullable = false, length = 50)
    private String genre;

    @OneToMany(mappedBy = "song", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SongBroadcast> songBroadcasts = new ArrayList<>();
    public Song() {
    }

    public Song(String title, String genre, Integer duration, String artist, Integer year) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.year = year;
        this.genre = genre;
    }

    public Integer getId(){
        return this.id;
    }

    public void addSongBroadcast(SongBroadcast songBroadcast) {
        if(songBroadcast != null){
            songBroadcast.setSong(this);
            this.songBroadcasts.add(songBroadcast);
        }
    }

    public List<SongBroadcast> getSongBroadcasts() {
        return this.songBroadcasts;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    private List<SongBroadcast> getBroadcastsOfDay(LocalDate date){
        List<SongBroadcast> broadcastsOfDay = new ArrayList<>();
        for (SongBroadcast b : this.songBroadcasts){
            if(b.getBroadcastDate().isEqual(date)){
                broadcastsOfDay.add(b);
            }
        }
        return broadcastsOfDay;
    }

    private boolean checkForPrevOccurrence(List<SongBroadcast> broadcasts, LocalTime time){
        for (SongBroadcast b : broadcasts) {
            // b.startingtime < time < b.endingtime
            LocalTime starting = b.getBroadcastTime();
            LocalTime ending = b.getBroadcastTime().plusMinutes(this.duration);
            if (DateUtil.between(starting, time, ending)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForNextOccurrence(List<SongBroadcast> broadcasts, LocalTime time){
        for (SongBroadcast b : broadcasts){
            // time < b.startingtime
            LocalTime starting = b.getBroadcastTime();
            if(DateUtil.between(time.minusHours(1), starting, time)){
                return true;
            }
        }
        return false;
    }


    public boolean toBeBroadcasted(LocalDate date, LocalTime time){
        // 1 hour before starting time
        LocalTime previousHour = time.minusHours(1);
        // 1 hour after starting time
        LocalTime nextHour = time.plusHours(1);
        // Filter from list and extract broadcasts from date
        List<SongBroadcast> broadcastsOfDay = getBroadcastsOfDay(date);
        if (broadcastsOfDay.size() == 4){
            return false;
        }

        // Check for occurrence in last hour
        if (checkForPrevOccurrence(broadcastsOfDay, previousHour) || checkForNextOccurrence(broadcastsOfDay, nextHour)){
            return false;
        }
        return true;
    }

    public void removeSongBroadcast(SongBroadcast songBroadcast){
        songBroadcast.setSong(null);
        this.songBroadcasts.remove(songBroadcast);
    }

}

