package gr.aueb.radio.content.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SongRepresentation {

    public Integer id;
    public String title;
    public String artist;
    public Integer year;
    public Integer duration;
    public Integer genreId;
}
