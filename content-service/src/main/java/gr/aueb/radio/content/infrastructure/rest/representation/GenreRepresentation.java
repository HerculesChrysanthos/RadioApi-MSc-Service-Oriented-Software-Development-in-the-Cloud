package gr.aueb.radio.content.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class GenreRepresentation {
    public Integer id;
    public String title;

    public void setId(Integer id) {
        this.id = id;
    }
}
