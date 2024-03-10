package gr.aueb.radio.user.application;

import gr.aueb.radio.user.common.RadioException;
import gr.aueb.radio.user.domain.user.Role;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import gr.aueb.radio.user.infrastructure.rest.representation.UserBasicRepresentation;
import gr.aueb.radio.user.infrastructure.rest.representation.UserMapper;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.time.temporal.ChronoUnit;

@RequestScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    @Transactional
    public User create(UserRepresentation userRepresentation){
        User user = userMapper.toModel(userRepresentation);
        user.setRole(Role.USER);
        if (userRepository.findByUsername(user.getUsername()) != null){
            throw new RadioException("User with this username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null){
            throw new RadioException("User with this email already exists");
        }
        userRepository.persist(user);
        return user;
    }

    @Transactional
    @Retry(maxRetries = 3, delay = 2,
            delayUnit = ChronoUnit.SECONDS)
    public UserRepresentation findUser(Integer id){
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return userMapper.toRepresentation(user);
    }

    @Transactional
    @Retry(maxRetries = 3, delay = 2,
            delayUnit = ChronoUnit.SECONDS)
    public UserBasicRepresentation findUserByUsername(String username){
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new NotFoundException("User not found");
        }

        return userMapper.toBasicRepresentation(user);
    }
}
