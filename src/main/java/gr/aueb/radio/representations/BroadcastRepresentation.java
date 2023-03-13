package gr.aueb.radio.representations;

import gr.aueb.radio.enums.BroadcastEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;


@RegisterForReflection
public class BroadcastRepresentation {

    public Integer duration;
    public String startingDate;
    public String startingTime;
    public BroadcastEnum type;
}
