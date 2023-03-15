
package gr.aueb.radio.representations;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SongRepresentation {
    public String title;
    public String artist;
    public Integer year;
    public Integer duration;
    public String genre;
}
