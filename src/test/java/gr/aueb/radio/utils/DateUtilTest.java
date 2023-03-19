package gr.aueb.radio.utils;

import gr.aueb.radio.enums.ZoneEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
public class DateUtilTest {
    @Test
    public void setDateTest(){
        LocalDate testingDate = DateUtil.setDate("01-01-2022");
        LocalDate expectedDate = LocalDate.of(2022, 1, 1);
        assertTrue(expectedDate.isEqual(testingDate));
    }

    @Test
    public void setTimeTest(){
        LocalTime testingTime = DateUtil.setTime("12:30");
        LocalTime expectedTime = LocalTime.of(12, 30);
        assertTrue(expectedTime.equals(testingTime));
    }

    @Test
    public void betweenTestLocalDate(){
        LocalDate start = DateUtil.setDate("01-11-2022");
        LocalDate end = DateUtil.setDate("01-12-2022");
        assertTrue(DateUtil.between(start, DateUtil.setDate("01-11-2022"), end));
        assertTrue(DateUtil.between(start, DateUtil.setDate("21-11-2022"), end));
        assertTrue(DateUtil.between(start, DateUtil.setDate("01-12-2022"), end));
        assertFalse(DateUtil.between(start, DateUtil.setDate("01-12-2023"), end));
        assertFalse(DateUtil.between(start, DateUtil.setDate("01-12-2021"), end));
    }

    @Test
    public void betweenTestLocalTime(){
        LocalTime start = DateUtil.setTime("12:30");
        LocalTime end = DateUtil.setTime("13:30");
        assertTrue(DateUtil.between(start, DateUtil.setTime("12:30"), end));
        assertTrue(DateUtil.between(start, DateUtil.setTime("13:00"), end));
        assertTrue(DateUtil.between(start, DateUtil.setTime("13:30"), end));
        assertFalse(DateUtil.between(start, DateUtil.setTime("11:30"), end));
        assertFalse(DateUtil.between(start, DateUtil.setTime("14:30"), end));
    }

    @Test
    public void betweenTestLocalDateTime(){
        LocalDate date = DateUtil.setDate("01-01-2022");
        LocalTime start = DateUtil.setTime("12:30");
        LocalTime end = DateUtil.setTime("13:30");
        LocalTime middle = DateUtil.setTime("12:30");
        assertTrue(DateUtil.between(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("13:00");
        assertTrue(DateUtil.between(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("13:30");
        assertTrue(DateUtil.between(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("11:30");
        assertFalse(DateUtil.between(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("14:30");
        assertFalse(DateUtil.between(date.atTime(start),date.atTime(middle), date.atTime(end)));
    }

    @Test
    public void betweenTestCloseOpenLocalTime(){
        LocalTime start = DateUtil.setTime("12:30");
        LocalTime end = DateUtil.setTime("13:30");
        assertTrue(DateUtil.betweenCloseOpen(start, DateUtil.setTime("12:30"), end));
        assertTrue(DateUtil.betweenCloseOpen(start, DateUtil.setTime("13:00"), end));
        assertFalse(DateUtil.betweenCloseOpen(start, DateUtil.setTime("13:30"), end));
        assertFalse(DateUtil.betweenCloseOpen(start, DateUtil.setTime("11:30"), end));
        assertFalse(DateUtil.betweenCloseOpen(start, DateUtil.setTime("14:30"), end));
    }

    @Test
    public void betweenTestCloseOpenLocalDateTime(){
        LocalDate date = DateUtil.setDate("01-01-2022");
        LocalTime start = DateUtil.setTime("12:30");
        LocalTime end = DateUtil.setTime("13:30");
        LocalTime middle = DateUtil.setTime("12:30");
        assertTrue(DateUtil.betweenCloseOpen(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("13:00");
        assertTrue(DateUtil.betweenCloseOpen(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("13:30");
        assertFalse(DateUtil.betweenCloseOpen(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("11:30");
        assertFalse(DateUtil.betweenCloseOpen(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("14:30");
        assertFalse(DateUtil.betweenCloseOpen(date.atTime(start),date.atTime(middle), date.atTime(end)));
    }

    @Test
    public void betweenTestOpenCloseLocalTime(){
        LocalTime start = DateUtil.setTime("12:30");
        LocalTime end = DateUtil.setTime("13:30");
        assertFalse(DateUtil.betweenOpenClose(start, DateUtil.setTime("12:30"), end));
        assertTrue(DateUtil.betweenOpenClose(start, DateUtil.setTime("13:00"), end));
        assertTrue(DateUtil.betweenOpenClose(start, DateUtil.setTime("13:30"), end));
        assertFalse(DateUtil.betweenOpenClose(start, DateUtil.setTime("11:30"), end));
        assertFalse(DateUtil.betweenOpenClose(start, DateUtil.setTime("14:30"), end));
    }

    @Test
    public void betweenTestOpenCloseLocalDateTime(){
        LocalDate date = DateUtil.setDate("01-01-2022");
        LocalTime start = DateUtil.setTime("12:30");
        LocalTime end = DateUtil.setTime("13:30");
        LocalTime middle = DateUtil.setTime("12:30");
        assertFalse(DateUtil.betweenOpenClose(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("13:00");
        assertTrue(DateUtil.betweenOpenClose(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("13:30");
        assertTrue(DateUtil.betweenOpenClose(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("11:30");
        assertFalse(DateUtil.betweenOpenClose(date.atTime(start),date.atTime(middle), date.atTime(end)));
        middle = DateUtil.setTime("14:30");
        assertFalse(DateUtil.betweenOpenClose(date.atTime(start),date.atTime(middle), date.atTime(end)));
    }

    @ParameterizedTest
    @CsvSource({
            "01:00, LateNight",
            "07:00, EarlyMorning",
            "11:00, Morning",
            "14:00, Noon",
            "18:00, Afternoon",
            "21:00, PrimeTime",
    })
    public void calculateTimezoneTest(String time, ZoneEnum expected){
        LocalTime localTime = DateUtil.setTime(time);
        ZoneEnum zone = DateUtil.calculateTimezone(localTime);
        assertEquals(expected, zone);
    }
}
