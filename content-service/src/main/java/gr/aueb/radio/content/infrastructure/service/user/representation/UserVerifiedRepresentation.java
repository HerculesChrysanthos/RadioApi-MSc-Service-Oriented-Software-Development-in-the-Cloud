package gr.aueb.radio.content.infrastructure.service.user.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserVerifiedRepresentation {

    public Integer id;
    public String role;
}
