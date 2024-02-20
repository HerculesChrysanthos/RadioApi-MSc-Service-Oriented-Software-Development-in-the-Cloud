package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;

public interface UserService {

    UserVerifiedRepresentation verifyAuth(String auth);
}
