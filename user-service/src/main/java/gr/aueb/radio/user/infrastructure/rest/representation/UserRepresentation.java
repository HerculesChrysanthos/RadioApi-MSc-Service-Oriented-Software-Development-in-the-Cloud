package gr.aueb.radio.user.infrastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserRepresentation {
    public Integer id;
    public String username;
    public String password;
    public String email;
}