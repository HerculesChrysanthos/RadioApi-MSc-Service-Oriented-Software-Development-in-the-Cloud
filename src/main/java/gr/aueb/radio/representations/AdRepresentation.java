package gr.aueb.radio.representations;

import gr.aueb.radio.enums.ZoneEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AdRepresentation {

    public Integer duration;
    public Integer repPerZone;
    public String startingDate;
    public String endingDate;
    public ZoneEnum timezone;

}
