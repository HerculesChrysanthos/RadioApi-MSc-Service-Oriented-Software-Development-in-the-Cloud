package gr.aueb.radio.mappers;

import gr.aueb.radio.domains.User;
import gr.aueb.radio.representations.UserRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi",injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract  class UserMapper {
    @Mapping(target = "role", ignore = true)
    public abstract User toModel(UserRepresentation representation);

    public abstract UserRepresentation toRepresentation(User user);
}
