package gr.aueb.radio.content.domain.ad;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "duration", unique = false, nullable = false)
    private Integer duration;

    @Column(name = "rep_per_zone", unique = false, nullable = false)
    private Integer repPerZone;

    @Column(name = "starting_date", unique = false, nullable = false)
    private LocalDate startingDate;

    @Column(name = "ending_date", unique = false, nullable = false)
    private LocalDate endingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "timezone")
    private Zone timezone;


    private List<Integer> adBroadcasts = new ArrayList<>();


    public Ad() {
    }

    public Ad(Integer duration, Integer repPerZone, LocalDate startingDate, LocalDate endingDate, Zone timezone) {
        this.duration = duration;
        this.repPerZone = repPerZone;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.timezone = timezone;
    }

    public Integer getId() {
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

    public Zone getTimezone() {
        return this.timezone;
    }

    public void setTimezone(Zone timezone) {
        this.timezone = timezone;
    }

    public List<Integer> getBroadcastAds() {
        return this.adBroadcasts;
    }

    public void addBroadcastAd(Integer adBroadcast) {
        if (adBroadcast != null) {
           // adBroadcast.setAd(this);
            this.adBroadcasts.add(adBroadcast);
        }
    }

//    public void removeAdBroadcast(AdBroadcast adBroadcast) {
//        adBroadcast.setAd(null);
//        this.adBroadcasts.remove(adBroadcast);
//    }
//
//    public boolean toBeBroadcasted(LocalDate date){
//        if (!DateUtil.between(this.startingDate, date, this.endingDate)){
//            return false;
//        }
//        return this.adBroadcasts.size() < this.repPerZone;
//    }


}