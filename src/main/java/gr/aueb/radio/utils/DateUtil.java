package gr.aueb.radio.utils;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class DateUtil {

    protected DateUtil() {}

    protected static void setStubs(LocalDate date, LocalTime time){
        timeStub = time;
        dateStub = date;
    }

    protected static void removeStubs(){
        timeStub = null;
        dateStub = null;
    }

    private static LocalTime timeStub;

    private static LocalDate dateStub;
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
        if (dateStub != null){
            return dateStub;
        }
        String local = LocalDate.now(zone).format(dateFormatter);
        return setDate(local);
    }

    public static LocalTime timeNow(){
        if (timeStub != null){
            return timeStub;
        }
        String local = LocalTime.now(zone).format(timeFormatter);
        return setTime(local);
    }
}

