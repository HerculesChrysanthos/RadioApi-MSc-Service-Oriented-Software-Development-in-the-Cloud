package gr.aueb.radio.common;

import gr.aueb.radio.content.common.DateUtil;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Zone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class DateUtilTest {

    @Test
    public void testSetDate() {
        LocalDate date = DateUtil.setDate("01-01-2022");
        Assertions.assertEquals("2022-01-01", date.toString());
    }

    @Test
    public void testSetTime() {
        LocalTime time = DateUtil.setTime("12:30");
        Assertions.assertEquals("12:30", time.toString());
    }

    @Test
    public void testCalculateTimezone() {
        Assertions.assertEquals(Zone.LateNight, DateUtil.calculateTimezone(LocalTime.of(3, 0)));
        Assertions.assertEquals(Zone.EarlyMorning, DateUtil.calculateTimezone(LocalTime.of(8, 0)));
        Assertions.assertEquals(Zone.Morning, DateUtil.calculateTimezone(LocalTime.of(11, 0)));
        Assertions.assertEquals(Zone.Noon, DateUtil.calculateTimezone(LocalTime.of(14, 0)));
        Assertions.assertEquals(Zone.Afternoon, DateUtil.calculateTimezone(LocalTime.of(18, 0)));
        Assertions.assertEquals(Zone.PrimeTime, DateUtil.calculateTimezone(LocalTime.of(21, 0)));
    }

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Test
    public void testSetTimeToString() {
        LocalTime time = LocalTime.of(12, 30);
        Assertions.assertEquals("12:30", DateUtil.setTimeToString(time));
    }

    @Test
    public void testSetDateToString() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Assertions.assertEquals("01-01-2022", DateUtil.setDateToString(date));
    }

    @Test
    public void testDateNow() {
        LocalDate currentDate = LocalDate.now();
        String formattedCurrentDate = currentDate.format(dateFormatter);
        Assertions.assertEquals(formattedCurrentDate, DateUtil.dateNow().format(dateFormatter));
    }

    @Test
    public void testTimeNow() {
        LocalTime currentTime = LocalTime.now();
        String formattedCurrentTime = currentTime.format(timeFormatter);
        Assertions.assertEquals(formattedCurrentTime, DateUtil.timeNow().format(timeFormatter));
    }

    @Test
    public void testSetDate_InvalidDateFormat() {
        // Attempt to parse an invalid date format
        String invalidDate = "2022-01-01"; // Invalid format, should be "dd-MM-yyyy"

        // Ensure that a RadioException is thrown with the correct message
        RadioException exception = Assertions.assertThrows(RadioException.class, () -> {
            DateUtil.setDate(invalidDate);
        });

        Assertions.assertEquals("Invalid date format, should be dd-MM-yyyy", exception.getMessage());
    }
    @Test
    public void testSetTime_InvalidTimeFormat() {
        // Attempt to parse an invalid time format
        String invalidTime = "12:30:00"; // Invalid format, should be "HH:mm"

        // Ensure that a RadioException is thrown with the correct message
        RadioException exception = Assertions.assertThrows(RadioException.class, () -> {
            DateUtil.setTime(invalidTime);
        });

        Assertions.assertEquals("Invalid time format, should be HH:mm", exception.getMessage());
    }

    @Test
    public void testBetween_WithinRange() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(12, 0);
        LocalTime ending = LocalTime.of(15, 0);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle time should be within the range");
    }

    @Test
    public void testBetween_EqualStartingTime() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(9, 0);
        LocalTime ending = LocalTime.of(15, 0);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle time should be equal to the starting time");
    }

    @Test
    public void testBetween_EqualEndingTime() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(15, 0);
        LocalTime ending = LocalTime.of(15, 0);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle time should be equal to the ending time");
    }

    @Test
    public void testBetween_OutsideRange() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(16, 0);
        LocalTime ending = LocalTime.of(18, 0);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle time should be outside the range");
    }

    @Test
    public void testBetweenOpenClose_WithinRange() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(12, 0);
        LocalTime ending = LocalTime.of(15, 0);

        // When
        boolean result = DateUtil.betweenOpenClose(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle time should be within the range");
    }

    @Test
    public void testBetweenOpenClose_EqualStartingTime() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(9, 0);
        LocalTime ending = LocalTime.of(15, 0);

        // When
        boolean result = DateUtil.betweenOpenClose(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle time should not be equal to the starting time");
    }

    @Test
    public void testBetweenOpenClose_EqualEndingTime() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(15, 0);
        LocalTime ending = LocalTime.of(15, 0);

        // When
        boolean result = DateUtil.betweenOpenClose(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle time should be equal to the ending time");
    }

    @Test
    public void testBetweenOpenClose_OutsideRange() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(16, 0);
        LocalTime ending = LocalTime.of(18, 0);

        // When
        boolean result = DateUtil.betweenOpenClose(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle time should be outside the range");
    }

    @Test
    public void testBetweenOpenClose_EqualStartingAndEndingTime() {
        // Given
        LocalTime starting = LocalTime.of(9, 0);
        LocalTime middle = LocalTime.of(9, 0);
        LocalTime ending = LocalTime.of(9, 0);

        // When
        boolean result = DateUtil.betweenOpenClose(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle time should not be equal to both starting and ending times");
    }


    @Test
    public void testBetween_LocalDateTime_WithinRange() {
        // Given
        LocalDateTime starting = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime middle = LocalDateTime.of(2022, 1, 1, 12, 0);
        LocalDateTime ending = LocalDateTime.of(2022, 1, 1, 15, 0);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle date time should be within the range");
    }

    @Test
    public void testBetween_LocalDateTime_OutsideRange() {
        // Given
        LocalDateTime starting = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime middle = LocalDateTime.of(2022, 1, 1, 16, 0);
        LocalDateTime ending = LocalDateTime.of(2022, 1, 1, 18, 0);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle date time should be outside the range");
    }

    // Test cases for betweenOpenClose(LocalDateTime starting, LocalDateTime middle, LocalDateTime ending)

    @Test
    public void testBetweenOpenClose_LocalDateTime_WithinRange() {
        // Given
        LocalDateTime starting = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime middle = LocalDateTime.of(2022, 1, 1, 12, 0);
        LocalDateTime ending = LocalDateTime.of(2022, 1, 1, 15, 0);

        // When
        boolean result = DateUtil.betweenOpenClose(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle date time should be within the range");
    }

    @Test
    public void testBetweenOpenClose_LocalDateTime_OutsideRange() {
        // Given
        LocalDateTime starting = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime middle = LocalDateTime.of(2022, 1, 1, 16, 0);
        LocalDateTime ending = LocalDateTime.of(2022, 1, 1, 18, 0);

        // When
        boolean result = DateUtil.betweenOpenClose(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle date time should be outside the range");
    }

    // Test cases for betweenCloseOpen(LocalDateTime starting, LocalDateTime middle, LocalDateTime ending)

    @Test
    public void testBetweenCloseOpen_LocalDateTime_WithinRange() {
        // Given
        LocalDateTime starting = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime middle = LocalDateTime.of(2022, 1, 1, 12, 0);
        LocalDateTime ending = LocalDateTime.of(2022, 1, 1, 15, 0);

        // When
        boolean result = DateUtil.betweenCloseOpen(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle date time should be within the range");
    }

    @Test
    public void testBetweenCloseOpen_LocalDateTime_OutsideRange() {
        // Given
        LocalDateTime starting = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime middle = LocalDateTime.of(2022, 1, 1, 16, 0);
        LocalDateTime ending = LocalDateTime.of(2022, 1, 1, 18, 0);

        // When
        boolean result = DateUtil.betweenCloseOpen(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle date time should be outside the range");
    }

    // Test cases for between(LocalDate starting, LocalDate middle, LocalDate ending)

    @Test
    public void testBetween_LocalDate_WithinRange() {
        // Given
        LocalDate starting = LocalDate.of(2022, 1, 1);
        LocalDate middle = LocalDate.of(2022, 1, 5);
        LocalDate ending = LocalDate.of(2022, 1, 10);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertTrue(result, "The middle date should be within the range");
    }

    @Test
    public void testBetween_LocalDate_OutsideRange() {
        // Given
        LocalDate starting = LocalDate.of(2022, 1, 1);
        LocalDate middle = LocalDate.of(2022, 1, 12);
        LocalDate ending = LocalDate.of(2022, 1, 15);

        // When
        boolean result = DateUtil.between(starting, middle, ending);

        // Then
        Assertions.assertFalse(result, "The middle date should be outside the range");
    }
}
