package gr.aueb.radio.user.infrastructure.rest.representation;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserInputDTO {
    @NotNull
    @NotBlank
    public String username;

    @NotNull
    @NotBlank
    public String password;

    @Email
    @NotNull
    @NotBlank
    public String email;

    public UserRepresentation toRepresentation(){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.password = this.password;
        userRepresentation.email = this.email;
        userRepresentation.username = this.username;
        return userRepresentation;
    }
}

