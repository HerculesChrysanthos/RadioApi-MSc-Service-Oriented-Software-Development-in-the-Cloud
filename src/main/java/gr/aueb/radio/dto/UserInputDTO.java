package gr.aueb.radio.dto;


import gr.aueb.radio.representations.UserRepresentation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
