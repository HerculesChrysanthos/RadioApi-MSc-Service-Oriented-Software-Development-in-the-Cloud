package gr.aueb.radio.domains;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transmission {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;
}
