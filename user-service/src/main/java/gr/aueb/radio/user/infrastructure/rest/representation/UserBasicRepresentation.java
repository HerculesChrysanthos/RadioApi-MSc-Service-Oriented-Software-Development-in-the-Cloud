package gr.aueb.radio.user.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserBasicRepresentation {
    public Integer id;
    public String role;
}
