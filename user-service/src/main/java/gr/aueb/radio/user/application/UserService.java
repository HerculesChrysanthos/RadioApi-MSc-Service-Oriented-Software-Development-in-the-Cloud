package gr.aueb.radio.user.application;

import gr.aueb.radio.user.common.NotFoundException;
import gr.aueb.radio.user.common.RadioException;
import gr.aueb.radio.user.domain.user.Role;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import gr.aueb.radio.user.infrastructure.rest.representation.UserMapper;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
    public UserRepresentation findUser(Integer id){
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return userMapper.toRepresentation(user);
    }
}
