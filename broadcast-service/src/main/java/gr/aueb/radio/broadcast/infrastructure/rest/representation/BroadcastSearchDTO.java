package gr.aueb.radio.broadcast.infrastructure.rest.representation;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalTime;

public class BroadcastSearchDTO {
    private LocalTime fromTime;
    private LocalTime toTime;
    private LocalDate date;

    private BroadcastType type;
    public BroadcastSearchDTO(String fromTime, String toTime, String date, BroadcastType type){
        this.fromTime = fromTime == null ? DateUtil.setTime("00:00") : DateUtil.setTime(fromTime);
        this.toTime = toTime == null ? DateUtil.setTime("23:59") : DateUtil.setTime(toTime);
        this.date = date == null ? null : DateUtil.setDate(date);
        this.type = type;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public BroadcastType getType() {
        return type;
    }
}
