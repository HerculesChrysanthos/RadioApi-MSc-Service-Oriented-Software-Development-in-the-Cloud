package gr.aueb.radio.domains;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name ="song_transmission")
public class Transmission {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;
}
