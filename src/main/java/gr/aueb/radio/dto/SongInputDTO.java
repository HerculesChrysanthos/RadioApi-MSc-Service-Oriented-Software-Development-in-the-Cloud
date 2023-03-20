package gr.aueb.radio.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SongInputDTO {
    public String title;
    public String artist;
    public Integer year;
    public Integer duration;
    public String genre;
}
