package gr.aueb.radio.broadcast.infrastructure.service.content.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDate;

@RegisterForReflection
public class AdBasicRepresentation {

    public Integer id;
    public String timezone;
    public Integer duration;
    public LocalDate startingDate;
    public LocalDate endingDate;
    public Integer repPerZone;
}
