package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@RegisterForReflection
public class BroadcastRepresentation {

    @Positive
    @Min(value = 1)
    public Integer duration;
    public String startingDate;
    public String startingTime;
    @NotNull
    public BroadcastType type;
    //public Integer userId;
    @NotNull
    public Integer genreId;

}
