package gr.aueb.radio.domains;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table (name = "add_broadcasts")
public class AddBroadcast {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "broadcast_date", unique = false, nullable = false)
    private LocalDate broadcastDate;

    @Column(name = "broadcast_time", unique = false, nullable = false)
    private LocalTime broadcastTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_id")
    private Add add;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broadcast_id")
    private Broadcast broadcast;

    public AddBroadcast() {}

    public AddBroadcast(LocalDate broadcastDate, LocalTime broadcastTime){
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
    public Add getAdd() {
        return this.add;
    }


    public void setAdd(Add add) {
        this.add = add;
    }

    public Broadcast getBroadcast() {
        return this.broadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        this.broadcast = broadcast;
    }

    public LocalDateTime getBroadcastEndingDateTime(){
        LocalDateTime startingLocalDateTime = this.broadcastDate.atTime(this.broadcastTime);
        return startingLocalDateTime.plusMinutes(this.add.getDuration());
    }
}
