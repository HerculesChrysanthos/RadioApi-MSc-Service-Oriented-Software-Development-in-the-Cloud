package gr.aueb.radio.dto;

import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.representations.AdRepresentation;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    public ZoneEnum timezone;

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
