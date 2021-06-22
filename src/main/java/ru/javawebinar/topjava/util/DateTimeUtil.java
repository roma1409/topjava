package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        boolean isStartTimePassed = Objects.isNull(startTime) || lt.compareTo(startTime) >= 0;
        boolean isEndTimePassed = Objects.isNull(endTime) || lt.compareTo(endTime) < 0;
        return isStartTimePassed && isEndTimePassed;
    }

    public static boolean isDateBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        boolean isStartDatePassed = Objects.isNull(startDate) || date.compareTo(startDate) >= 0;
        boolean isEndDatePassed = Objects.isNull(endDate) || date.compareTo(endDate) <= 0;
        return isStartDatePassed && isEndDatePassed;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

