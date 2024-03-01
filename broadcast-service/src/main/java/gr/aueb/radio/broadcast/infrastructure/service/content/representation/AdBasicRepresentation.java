package gr.aueb.radio.broadcast.infrastructure.service.content.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbDateFormat;

import java.time.LocalDate;

@RegisterForReflection
public class AdBasicRepresentation {

    public Integer id;
    public String timezone;
    public Integer duration;
    @JsonbDateFormat("dd-MM-yyyy")
    public LocalDate startingDate;
    @JsonbDateFormat("dd-MM-yyyy")
    public LocalDate endingDate;
    public Integer repPerZone;
}
