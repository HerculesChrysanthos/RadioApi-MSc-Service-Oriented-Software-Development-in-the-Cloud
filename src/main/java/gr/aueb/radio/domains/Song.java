package gr.aueb.radio.domains;

import javax.persistence.*;
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
    
    public void addSongBroadcast(SongBroadcast songBroadcast) {
        if(songBroadcast != null){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return this.title.equals(song.title) && this.artist.equals(song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }
}

