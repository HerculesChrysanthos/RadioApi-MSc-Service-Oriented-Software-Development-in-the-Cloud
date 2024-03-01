package gr.aueb.radio.content.infrastructure.rest.representation;

import gr.aueb.radio.content.domain.ad.Zone;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AdInputDTO {
    @NotNull
    @Positive
    public Integer duration;
    @NotNull
    @Positive
    public Integer repPerZone;
    public String startingDate;
    public String endingDate;
    @NotNull
    public Zone timezone;

    public AdRepresentation toRepresentation(){
        AdRepresentation adRepresentation = new AdRepresentation();
        adRepresentation.startingDate = this.startingDate;
        adRepresentation.endingDate = this.endingDate;
        adRepresentation.duration = this.duration;
        adRepresentation.timezone = this.timezone;
        adRepresentation.repPerZone = this.repPerZone;
        return adRepresentation;
    }
}
