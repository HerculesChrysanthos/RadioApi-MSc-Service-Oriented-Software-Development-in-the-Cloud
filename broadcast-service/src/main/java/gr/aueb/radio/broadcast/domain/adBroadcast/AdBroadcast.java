package gr.aueb.radio.broadcast.domain.adBroadcast;


import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table (name = "ad_broadcasts")
public class AdBroadcast {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "broadcast_date", unique = false, nullable = false)
    private LocalDate broadcastDate;

    @Column(name = "broadcast_time", unique = false, nullable = false)
    private LocalTime broadcastTime;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "ad_id")
    private Integer adId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broadcast_id")
    private Broadcast broadcast;

    public AdBroadcast() {}

    public AdBroadcast(LocalDate broadcastDate, LocalTime broadcastTime){
        this.broadcastDate = broadcastDate;
        this.broadcastTime = broadcastTime;
    }

    public Integer getId() {
        return this.id;
    }
    public LocalTime getBroadcastTime() {
        return this.broadcastTime;
    }
    public void setBroadcastTime(LocalTime broadcastTime) {
        this.broadcastTime = broadcastTime;
    }
    public LocalDate getBroadcastDate() {
        return this.broadcastDate;
    }
    public void setBroadcastDate(LocalDate broadcastDate) {
        this.broadcastDate = broadcastDate;
    }
//    public Ad getAd() {
//        return this.ad;
//    }


//    public void setAd(Ad ad) {
//        this.ad = ad;
//    }

    public Broadcast getBroadcast() {
        return this.broadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }

//    public LocalDateTime getBroadcastEndingDateTime(){
//        LocalDateTime startingLocalDateTime = this.broadcastDate.atTime(this.broadcastTime);
//        return startingLocalDateTime.plusMinutes(this.ad.getDuration());
//    }
}
