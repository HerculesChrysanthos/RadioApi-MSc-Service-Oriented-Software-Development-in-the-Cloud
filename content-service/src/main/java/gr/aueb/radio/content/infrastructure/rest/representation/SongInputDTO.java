package gr.aueb.radio.content.infrastructure.rest.representation;

import gr.aueb.radio.content.domain.genre.Genre;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@RegisterForReflection
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
    // constraint (@NotBlank) intended for strings on an integer field
//    @NotBlank
    public Integer genreId;

    public GenreRepresentation genre;

    public SongRepresentation toRepresentation(GenreRepresentation genreRepresentation) {
        SongRepresentation songRepresentation = new SongRepresentation();
        songRepresentation.title = this.title;
        songRepresentation.artist = this.artist;
        songRepresentation.year = this.year;
        songRepresentation.duration = this.duration;
        songRepresentation.genre = genreRepresentation;
        return songRepresentation;
    }
}
