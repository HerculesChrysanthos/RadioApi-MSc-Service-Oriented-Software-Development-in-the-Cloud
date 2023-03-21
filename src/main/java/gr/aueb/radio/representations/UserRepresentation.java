package gr.aueb.radio.representations;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserRepresentation {
    public Integer id;
    public String username;
    public String password;
    public String email;
}
