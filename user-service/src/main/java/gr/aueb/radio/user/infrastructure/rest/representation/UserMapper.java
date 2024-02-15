package gr.aueb.radio.user.infrastructure.rest.representation;

import gr.aueb.radio.user.domain.user.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta",injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract  class UserMapper {
    @Mapping(target = "role", ignore = true)
    public abstract User toModel(UserRepresentation representation);

    public abstract UserRepresentation toRepresentation(User user);
}
