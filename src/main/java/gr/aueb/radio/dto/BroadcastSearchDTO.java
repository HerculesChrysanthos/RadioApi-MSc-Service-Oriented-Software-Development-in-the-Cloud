package gr.aueb.radio.dto;

import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.utils.DateUtil;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class BroadcastSearchDTO {
    private LocalTime fromTime;
    private LocalTime toTime;
    private LocalDate date;
    private BroadcastEnum type;
    public BroadcastSearchDTO(String fromTime, String toTime, String date, BroadcastEnum type){
        this.fromTime = fromTime == null ? DateUtil.setTime("00:00") : DateUtil.setTime(fromTime);
        this.toTime = toTime == null ? DateUtil.setTime("23:59") : DateUtil.setTime(toTime);
        this.date = date == null ? null : DateUtil.setDate(date);
        this.type = type;
    }
}
