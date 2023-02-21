package gr.aueb.radio.domains;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table (name = "broadcastadd")
public class BroadcastAdd {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @Column(name = "broadcastDate", unique = false, nullable = false)
    private LocalDate broadcastDate;

    @Column(name = "broadcastTime", unique = false, nullable = false)
    private LocalTime broadcastTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "add_id", nullable = false)
    private Add add;

    public BroadcastAdd() {
        }

    public BroadcastAdd(LocalDate broadcastDate, LocalTime broadcastTime){
        this.broadcastDate = broadcastDate;
        this.broadcastTime = broadcastTime;
    }

    public Integer getId() {
        return Id;
    }
    public LocalTime getBroadcastTime() { return broadcastTime;}
    public void setBroadcastTime(LocalTime broadcastTime) {
        this.broadcastTime = broadcastTime;
    }
    public LocalDate getBroadcastDate() {  return broadcastDate;   }
    public void setBroadcastDate(LocalDate broadcastDate) {
        this.broadcastDate = broadcastDate;
    }
    public Add getAdd() {
        return add;
    }

    public void setAdd(Add add) {
        this.add = add;
    }
}
