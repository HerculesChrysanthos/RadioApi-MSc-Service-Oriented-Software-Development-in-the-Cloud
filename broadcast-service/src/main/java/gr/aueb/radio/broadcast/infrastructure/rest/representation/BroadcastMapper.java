package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
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
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(time, formatter);
        }catch (Exception e){
            throw new RadioException("Invalid date format, should be HH:mm");
        }
    }

    @Named("dateFormatter")
    public String formatDate(LocalDate time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return time.format(formatter);
    }

    @Named("stringDateFormatter")
    public LocalDate formatDate(String date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(date, formatter);
        }catch (Exception e){
            throw new RadioException("Invalid date format, should be dd-MM-yyyy");
        }
    }
}