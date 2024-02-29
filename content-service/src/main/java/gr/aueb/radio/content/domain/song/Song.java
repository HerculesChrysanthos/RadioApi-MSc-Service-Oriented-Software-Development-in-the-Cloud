package gr.aueb.radio.content.domain.song;

import gr.aueb.radio.content.domain.genre.Genre;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="songs")
public class Song {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "artist", nullable = false, length = 50)
    private String artist;

    @Column(name = "release_year", nullable = false)
    private Integer year;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "genre_id")
    private Genre genre;

//    private List<Integer> songBroadcasts = new ArrayList<>();
    public Song() {
    }

    public Song(String title, Genre genre, Integer duration, String artist, Integer year) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.year = year;
        this.genre = genre;
    }

    public Integer getId(){
        return this.id;
    }

//    public void addSongBroadcast(Integer songBroadcast) {
//        if(songBroadcast != null){
//            //songBroadcast.setSong(this);
//            this.songBroadcasts.add(songBroadcast);
//        }
//    }

//    public List<Integer> getSongBroadcasts() {
//        return this.songBroadcasts;
//    }

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

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setId(int i) {
    }


//    private boolean checkForPrevOccurrence(List<SongBroadcast> broadcasts, LocalDateTime dateTime){
//        // 1 hour before starting time
//        LocalDateTime previousHour = dateTime.minusHours(1);
//        for (SongBroadcast b : broadcasts){
//            // time-1 <= b.endingTime <= time
//            LocalDateTime ending = b.getBroadcastEndingDateTime();
//            if(DateUtil.between(previousHour, ending , dateTime)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean checkForNextOccurrence(List<SongBroadcast> broadcasts, LocalDateTime dateTime){
//        // 1 hour after ending time
//        LocalDateTime nextHour = dateTime.plusMinutes(this.duration).plusHours(1);
//        for (SongBroadcast b : broadcasts){
//            // time <= b.startingTime <= time+duration+1
//            LocalDateTime starting = b.getBroadcastDate().atTime(b.getBroadcastTime());
//            if(DateUtil.between(dateTime, starting , nextHour)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//
//
//    public void removeSongBroadcast(SongBroadcast songBroadcast){
//        songBroadcast.setSong(null);
//        this.songBroadcasts.remove(songBroadcast);
//    }

}
