package gr.aueb.radio.domains;

import gr.aueb.radio.enums.ZoneEnum;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table (name = "adds")
public class Add {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer AddId;

    @Column(name="duration", unique = false, nullable = false, length = 50)
    private Integer duration;

    @Column(name="repPerZone", unique = false, nullable = false, length = 50)
    private Integer repPerZone;

    @Column(name="startingDate",unique = false)
    private   LocalDate startingDate ;

    @Column(name="endingDate", unique = false)
    private LocalDate endingDate ;

    @Enumerated(EnumType.STRING)
    @Column(name="TimeZone")
    private ZoneEnum TimeZone;

    @OneToMany(mappedBy = "add", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<BroadcastAdd> broadcastAdds = new HashSet<BroadcastAdd>();


    public Add() {
    }

    public Add( Integer duration, Integer repPerZone, LocalDate startingDate, LocalDate endingDate, ZoneEnum TimeZone) {
        this.duration = duration;
        this.repPerZone = repPerZone;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.TimeZone = TimeZone;
    }
    public Integer getId() {
        return AddId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getRepPerZone() { return repPerZone; }

    public void setRepPerZone(Integer repPerZone) {
        this.repPerZone = repPerZone;
    }

    public LocalDate getStartingDate() { return startingDate;}

    public void setStartingDate(LocalDate date){this.startingDate = date; }
    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate date){this.endingDate = date; }

    public ZoneEnum getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(ZoneEnum TimeZone) {
        this.TimeZone = TimeZone;
    }
    /*  μεταδόσεις της διαφήμισης  */

//    public void setBroadcastAdds(Set<BroadcastAdd> broadcastAdds) {
//        this.broadcastAdds = broadcastAdds;
//    }

    public Set<BroadcastAdd> getBroadcastAdds() {
        return broadcastAdds;
    }

    public void addBroadcastadd (BroadcastAdd broadcastadd ) {
        if (broadcastadd != null) {
            broadcastadd.setAdd(this);
        }
    }
}
