package gr.aueb.radio.content.infrastructure.rest.representation;

import gr.aueb.radio.content.domain.ad.Ad;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public abstract class AdMapper {
//@Mapping(target = "startingDate", source = "startingDate", qualifiedByName = "stringDateFormatter")
//@Mapping(target = "endingDate", source = "endingDate", qualifiedByName = "stringDateFormatter")

public abstract Ad toModel(AdRepresentation representation);

//
//@Mapping(target = "startingDate", source = "startingDate", qualifiedByName = "dateFormatter")
//@Mapping(target = "endingDate", source = "endingDate", qualifiedByName = "dateFormatter")
public abstract AdRepresentation toRepresentation(Ad ad);

//@Mapping(target = "startingDate", source = "startingDate", qualifiedByName = "dateFormatter")
//@Mapping(target = "endingDate", source = "endingDate", qualifiedByName = "dateFormatter")
public abstract List<AdRepresentation> toRepresentationList(List<Ad> ad);

}
