package gr.aueb.radio.utils;

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

    public static LocalDate dateNow(){
        String local = LocalDate.now(zone).format(dateFormatter);
        return setDate(local);
    }

    public static LocalTime timeNow(){
        String local = LocalTime.now(zone).format(timeFormatter);
        return setTime(local);
    }
}

