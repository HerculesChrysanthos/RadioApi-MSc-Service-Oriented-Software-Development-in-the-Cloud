package gr.aueb.radio.broadcast.domain.broadcast.SongBroadcast;

import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name ="song_broadcasts")
public class SongBroadcast {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "broadcast_date", nullable = false)
    private LocalDate broadcastDate;

    @Column(name = "broadcast_time", nullable = false)
    private LocalTime broadcastTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broadcast_id")
    private Broadcast broadcast;

    public SongBroadcast() {
    }

    public SongBroadcast(LocalDate broadcastDate, LocalTime broadcastTime) {
        this.broadcastDate = broadcastDate;
        this.broadcastTime = broadcastTime;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getBroadcastDate() {
        return broadcastDate;
    }

    public void setBroadcastDate(LocalDate broadcastDate) {
        this.broadcastDate = broadcastDate;
    }

    public LocalTime getBroadcastTime() {
        return broadcastTime;
    }

    public void setBroadcastTime(LocalTime broadcastTime) {
        this.broadcastTime = broadcastTime;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Broadcast getBroadcast() {
        return this.broadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }

    public LocalDateTime getBroadcastEndingDateTime(){
        LocalDateTime startingDate = this.broadcastDate.atTime(this.broadcastTime);
        return startingDate.plusMinutes(this.song.getDuration());
    }
}
