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
    public void betweenTest(){
        LocalTime start = DateUtil.setTime("12:30");
        LocalTime end = DateUtil.setTime("13:30");
        assertTrue(DateUtil.between(start, DateUtil.setTime("12:30"), end));
        assertTrue(DateUtil.between(start, DateUtil.setTime("13:00"), end));
        assertTrue(DateUtil.between(start, DateUtil.setTime("13:30"), end));
        assertFalse(DateUtil.between(start, DateUtil.setTime("11:30"), end));
        assertFalse(DateUtil.between(start, DateUtil.setTime("14:30"), end));
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
