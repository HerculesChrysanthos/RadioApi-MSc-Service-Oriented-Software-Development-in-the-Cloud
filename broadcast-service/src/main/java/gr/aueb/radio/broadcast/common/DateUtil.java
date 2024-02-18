package gr.aueb.radio.broadcast.utils;


import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.broadcast.enums.ZoneEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private static ZoneId zone = ZoneId.of("Europe/Athens");
    public static LocalDate setDate(String date){
        try {
            return LocalDate.parse(date, dateFormatter);
        }catch (Exception e){
            throw new RadioException("Invalid date format, should be dd-MM-yyyy");
        }
    }

    public static LocalTime setTime(String time){
        try {
            return LocalTime.parse(time, timeFormatter);
        }catch (Exception e){
            throw new RadioException("Invalid time format, should be HH:mm");
        }
    }

    public static String setTimeToString(LocalTime time){
        return time.format(timeFormatter);
    }

    public static String setDateToString(LocalDate date){
        return date.format(dateFormatter);
    }

    public static LocalDate dateNow(){
        String local = LocalDate.now(zone).format(dateFormatter);
        return setDate(local);
    }

    public static LocalTime timeNow(){
        String local = LocalTime.now(zone).format(timeFormatter);
        return setTime(local);
    }

    public static boolean between(LocalTime starting, LocalTime middle, LocalTime ending){
        if ((starting.equals(middle) || starting.isBefore(middle)) && (ending.equals(middle) || ending.isAfter(middle))){
            return true;
        }
        return false;
    }

    public static boolean betweenOpenClose(LocalTime starting, LocalTime middle, LocalTime ending){
        if (starting.isBefore(middle) && (ending.equals(middle) || ending.isAfter(middle))){
            return true;
        }
        return false;
    }

    public static boolean betweenCloseOpen(LocalTime starting, LocalTime middle, LocalTime ending){
        if ((starting.equals(middle) || starting.isBefore(middle)) && ending.isAfter(middle)){
            return true;
        }
        return false;
    }

    public static boolean between(LocalDateTime starting, LocalDateTime middle, LocalDateTime ending){
        if ((starting.equals(middle) || starting.isBefore(middle)) && (ending.equals(middle) || ending.isAfter(middle))){
            return true;
        }
        return false;
    }

    public static boolean betweenOpenClose(LocalDateTime starting, LocalDateTime middle, LocalDateTime ending){
        if (starting.isBefore(middle) && (ending.equals(middle) || ending.isAfter(middle))){
            return true;
        }
        return false;
    }

    public static boolean betweenCloseOpen(LocalDateTime starting, LocalDateTime middle, LocalDateTime ending){
        if ((starting.equals(middle) || starting.isBefore(middle)) && ending.isAfter(middle)){
            return true;
        }
        return false;
    }

    public static boolean between(LocalDate starting, LocalDate middle, LocalDate ending) {
        if ((starting.equals(middle) || starting.isBefore(middle)) && (ending.equals(middle) || ending.isAfter(middle))) {
            return true;
        }
        return false;
    }

    public static ZoneEnum calculateTimezone(LocalTime startingTime){
        if (DateUtil.betweenCloseOpen(LocalTime.of(0,0), startingTime,LocalTime.of(6,0))){
            return ZoneEnum.LateNight;
        } else if (DateUtil.betweenCloseOpen(LocalTime.of(6,0), startingTime, LocalTime.of(10,0))) {
            return ZoneEnum.EarlyMorning;
        } else if (DateUtil.betweenCloseOpen(LocalTime.of(10,0), startingTime, LocalTime.of(13,0))){
            return ZoneEnum.Morning;
        } else if (DateUtil.betweenCloseOpen(LocalTime.of(13,0), startingTime, LocalTime.of(17,0))){
            return ZoneEnum.Noon;
        } else if (DateUtil.betweenCloseOpen(LocalTime.of(17,0), startingTime, LocalTime.of(20,0))){
            return ZoneEnum.Afternoon;
        } else {
            return ZoneEnum.PrimeTime;
        }
    }
}

