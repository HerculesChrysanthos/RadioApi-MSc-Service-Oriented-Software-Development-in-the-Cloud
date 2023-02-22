package gr.aueb.radio.domains;

import gr.aueb.radio.enums.BroadcastEnum;
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

    @OneToMany(mappedBy = "broadcast", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<AddBroadcast> addBroadcasts = new ArrayList<>();

    @OneToMany(mappedBy = "broadcast", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SongBroadcast> songBroadcasts = new ArrayList<>();

    public Broadcast() {}

    public Broadcast(Integer duration, String startingDate, String startingTime, BroadcastEnum type) {
        this.duration = duration;
        this.startingDate = DateUtil.setDate(startingDate);
        this.startingTime = DateUtil.setTime(startingTime);
        this.type = type;
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

    public void setStartingDate(String startingDate) {
        this.startingDate = DateUtil.setDate(startingDate);
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = DateUtil.setTime(startingTime);
    }

    public BroadcastEnum getType() {
        return type;
    }

    public void setType(BroadcastEnum type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Broadcast broadcast = (Broadcast) o;
        return this.startingDate.equals(broadcast.startingDate) && this.startingTime.equals(broadcast.startingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startingDate, this.startingTime);
    }
}
