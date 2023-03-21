package gr.aueb.radio.dto;

import gr.aueb.radio.representations.SongRepresentation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
public class SongInputDTO {
    @NotNull
    @NotBlank
    public String title;

    @NotNull
    @NotBlank
    public String artist;

    @Min(value = 1)
    @Max(value = 9999)
    @Positive
    @NotNull
    public Integer year;

    @Positive
    @NotNull
    public Integer duration;

    @NotNull
    @NotBlank
    public String genre;

    public SongRepresentation toRepresentation(){
        SongRepresentation songRepresentation = new SongRepresentation();
        songRepresentation.title = this.title;
        songRepresentation.artist = this.artist;
        songRepresentation.year = this.year;
        songRepresentation.duration = this.duration;
        songRepresentation.genre = this.genre;
        return songRepresentation;
    }
}
