package gr.aueb.radio.domains;

import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name="broadcasts")
public class Broadcast {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="duration", nullable = false)
    private Integer duration;

    @Column(name="startingDate", nullable = false)
    private LocalDate startingDate;

    @Column(name="startingTime", nullable = false)
    private LocalTime startingTime;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private BroadcastEnum type;

    private ZoneEnum timezone;

    @OneToMany(mappedBy = "broadcast", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<AddBroadcast> addBroadcasts = new ArrayList<>();

    @OneToMany(mappedBy = "broadcast", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SongBroadcast> songBroadcasts = new ArrayList<>();

    public Broadcast() {}

    public Broadcast(Integer duration, LocalDate startingDate, LocalTime startingTime, BroadcastEnum type) {
        this.duration = duration;
        this.startingDate = startingDate;
        this.startingTime = startingTime;
        this.type = type;
        this.timezone = DateUtil.calculateTimezone(this.startingTime);
    }

    public Integer getId() {
        return id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
        this.timezone = DateUtil.calculateTimezone(this.startingTime);
    }

    public BroadcastEnum getType() {
        return type;
    }

    public void setType(BroadcastEnum type) {
        this.type = type;
    }

    public List<AddBroadcast> getAddBroadcasts(){
        return this.addBroadcasts;
    }

    public List<SongBroadcast> getSongBroadcasts(){
        return this.songBroadcasts;
    }

    public void createAddBroadcast(Add add, LocalTime time){
        AddBroadcast addBroadcast = new AddBroadcast(this.startingDate, time);
        addBroadcast.setBroadcast(this);
        add.addBroadcastAdd(addBroadcast);
        this.addBroadcasts.add(addBroadcast);
    }

    public void createSongBroadcast(Song song, LocalTime time){
        SongBroadcast songBroadcast = new SongBroadcast(this.startingDate, time);
        songBroadcast.setBroadcast(this);
        song.addSongBroadcast(songBroadcast);
        this.songBroadcasts.add(songBroadcast);
    }

    public void removeAddBroadcast(AddBroadcast addBroadcast) {
        this.addBroadcasts.remove(addBroadcast);
        addBroadcast.setBroadcast(null);
    }

    public void removeSongBroadcast(SongBroadcast songBroadcast) {
        this.songBroadcasts.remove(songBroadcast);
        songBroadcast.setBroadcast(null);
    }

    private boolean checkForOccurrence(LocalTime startingTime, Integer duration){
        for (SongBroadcast sb : this.songBroadcasts){
            LocalTime sbStartingTime = sb.getBroadcastTime();
            LocalTime sbEndingTime = sb.getBroadcastTime().plusMinutes(sb.getSong().getDuration());
            if(DateUtil.between(sbStartingTime, startingTime, sbEndingTime)){
                return true;
            }

            LocalTime endingTime = startingTime.plusMinutes(duration);
            if(DateUtil.between(sbStartingTime, endingTime, sbEndingTime)){
                return true;
            }
        }

        for (AddBroadcast ab : this.addBroadcasts){
            LocalTime abStartingTime = ab.getBroadcastTime();
            LocalTime abEndingTime = ab.getBroadcastTime().plusMinutes(ab.getAdd().getDuration());
            if(DateUtil.between(abStartingTime, startingTime, abEndingTime)){
                return true;
            }

            LocalTime endingTime = startingTime.plusMinutes(duration);
            if(DateUtil.between(abStartingTime, endingTime, abEndingTime)){
                return true;
            }
        }
        return false;
    }

    public Integer getAllocatedTime(){
        Integer totalTime = 0;
        for (SongBroadcast songBroadcast : this.songBroadcasts){
            totalTime += songBroadcast.getSong().getDuration();
        }
        for (AddBroadcast addBroadcast : this.addBroadcasts){
            totalTime += addBroadcast.getAdd().getDuration();
        }
        return totalTime;
    }

}
