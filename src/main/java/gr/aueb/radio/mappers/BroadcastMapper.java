package gr.aueb.radio.mappers;

import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.representations.BroadcastRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SongBroadcastMapper.class, AdBroadcastMapper.class}
)
public abstract class BroadcastMapper {

    @Mapping(target = "startingTime", source = "startingTime", qualifiedByName = "stringTimeFormatter")
    @Mapping(target = "startingDate", source = "startingDate", qualifiedByName = "stringDateFormatter")
    public abstract Broadcast toModel(BroadcastRepresentation broadcastRepresentation);

    @Mapping(target = "startingTime", source = "startingTime", qualifiedByName = "timeFormatter")
    @Mapping(target = "startingDate", source = "startingDate", qualifiedByName = "dateFormatter")
    public abstract BroadcastRepresentation toRepresentation(Broadcast broadcast);
    public abstract List<BroadcastRepresentation> toRepresentationList(List<Broadcast> broadcasts);

    @Named("timeFormatter")
    public String formatTime(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    @Named("stringTimeFormatter")
    public LocalTime formatTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, formatter);
    }

    @Named("dateFormatter")
    public String formatDate(LocalDate time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return time.format(formatter);
    }

    @Named("stringDateFormatter")
    public LocalDate formatDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }
}
