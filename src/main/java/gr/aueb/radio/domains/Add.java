package gr.aueb.radio.domains;

import gr.aueb.radio.enums.ZoneEnum;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;


@Entity
@Table (name = "adds")
public class Add {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="duration", unique = false, nullable = false)
    private Integer duration;

    @Column(name="rep_per_zone", unique = false, nullable = false)
    private Integer repPerZone;

    @Column(name="starting_date",unique = false, nullable = false)
    private LocalDate startingDate ;

    @Column(name="ending_date", unique = false, nullable = false)
    private LocalDate endingDate ;

    @Enumerated(EnumType.STRING)
    @Column(name="timezone")
    private ZoneEnum timezone;

    @OneToMany(mappedBy = "add", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<AddBroadcast> addBroadcasts = new ArrayList<>();


    public Add() {
    }

    public Add( Integer duration, Integer repPerZone, LocalDate startingDate, LocalDate endingDate, ZoneEnum timezone) {
        this.duration = duration;
        this.repPerZone = repPerZone;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.timezone = timezone;
    }

    public Integer getId(){
        return this.id;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getRepPerZone() {
        return this.repPerZone;
    }

    public void setRepPerZone(Integer repPerZone) {
        this.repPerZone = repPerZone;
    }

    public LocalDate getStartingDate() {
        return this.startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getEndingDate() {
        return this.endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    public ZoneEnum getTimezone() {
        return this.timezone;
    }

    public void setTimezone(ZoneEnum timezone) {
        this.timezone = timezone;
    }

    public List<AddBroadcast> getBroadcastAdds() {
        return this.addBroadcasts;
    }

    public void addBroadcastAdd (AddBroadcast addBroadcast) {
        if (addBroadcast != null) {
            this.addBroadcasts.add(addBroadcast);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Add add = (Add) o;
        return add.duration == this.duration && this.startingDate.equals(add.startingDate) && this.endingDate.equals(add.endingDate) && this.timezone == add.getTimezone();
    }

    @Override
    public int hashCode() {
        return Objects.hash( duration, startingDate, endingDate, timezone);
    }
}
