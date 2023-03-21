package gr.aueb.radio.representations;

import gr.aueb.radio.enums.BroadcastEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@RegisterForReflection
public class BroadcastRepresentation {

    @Positive
    @Min(value = 1)
    public Integer duration;
    public String startingDate;
    public String startingTime;
    @NotNull
    public BroadcastEnum type;

}
