package gr.aueb.radio.broadcast.infrastructure.service.content.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AdBasicRepresentation {

    public Integer id;
    public String timezone;
    public Integer duration;
}
