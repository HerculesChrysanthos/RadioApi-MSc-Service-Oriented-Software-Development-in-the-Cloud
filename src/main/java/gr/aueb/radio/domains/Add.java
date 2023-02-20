package gr.aueb.radio.domains;

import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Table (name = "adds")
public class Add {

    @Id
    @Column(name="Add_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer AddId;

    @Column(name="duration", unique = false, nullable = false, length = 50)
    private Integer duration;

    @Column(name="repPerZone", unique = false, nullable = false, length = 50)
    private Integer repPerZone;

    @Column(name="StartingDate",unique = false)
    private   LocalDate StartingDate ;

    @Column(name="EndingDate", unique = false)
    private LocalDate EndingDate ;

    @Enumerated(EnumType.STRING)
    @Column(name="TimeZone")
    private ZoneEnum TimeZone;

    @OneToMany(mappedBy = "add", fetch = FetchType.LAZY)
    private Set<BroadcastAdd> BroadcastAdd ;


    public Add() {
    }

    public Add( Integer duration, Integer repPerZone, LocalDate StartingDate, LocalDate EndingDate, ZoneEnum TimeZone) {
        this.duration = duration;
        this.repPerZone = repPerZone;
        this.StartingDate = StartingDate;
        this.EndingDate = EndingDate;
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

    public LocalDate getStartingDate() { return StartingDate;}

    public void setStartingDate(String date){this.StartingDate = DateUtil.setDate(date); }

    public LocalDate getEndingDate() {
        return EndingDate;
    }

    public void setEndingDate(String date){this.EndingDate = DateUtil.setDate(date); }


    public ZoneEnum getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(ZoneEnum TimeZone) {
        this.TimeZone = TimeZone;
    }
}