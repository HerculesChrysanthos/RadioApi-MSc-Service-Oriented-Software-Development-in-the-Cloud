package gr.aueb.radio.content.infrastructure.rest.representation;

import gr.aueb.radio.content.domain.ad.Zone;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AdRepresentation {
    public Integer id;
    public Integer duration;
    public Integer repPerZone;
    public String startingDate;
    public String endingDate;
    public Zone timezone;

}
