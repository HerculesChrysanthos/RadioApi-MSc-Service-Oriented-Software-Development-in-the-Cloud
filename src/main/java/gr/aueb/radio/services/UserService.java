package gr.aueb.radio.services;

import gr.aueb.radio.domains.User;
import gr.aueb.radio.enums.RoleEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.UserMapper;
import gr.aueb.radio.persistence.UserRepository;
import gr.aueb.radio.representations.UserRepresentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    @Transactional
    public User create(UserRepresentation userRepresentation){
        User user = userMapper.toModel(userRepresentation);
        user.setRole(RoleEnum.USER);
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
