package gr.aueb.radio.content.application;

import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;

public interface UserService {

    UserVerifiedRepresentation verifyAuth(String username, String password);
}
