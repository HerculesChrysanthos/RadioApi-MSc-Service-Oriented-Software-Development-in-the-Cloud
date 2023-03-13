package gr.aueb.radio.utils;

import gr.aueb.radio.enums.ZoneEnum;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class DateUtil {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private static ZoneId zone = ZoneId.of("Europe/Athens");
    public static LocalDate setDate(String date){
        return LocalDate.parse(date, dateFormatter);
    }

    public static LocalTime setTime(String time){
        return LocalTime.parse(time, timeFormatter);
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

