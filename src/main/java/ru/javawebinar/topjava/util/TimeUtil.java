package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeUtil {
    public static LocalDate parseLocalDate(String startDate) {
        return startDate == null || startDate.isEmpty() ? null : LocalDate.parse(startDate);
    }

    public static LocalTime parseLocalTime(String startTime) {
        return startTime == null || startTime.isEmpty() ? null : LocalTime.parse(startTime);
    }

    public static boolean isBetween(LocalDate localDate, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || !localDate.isBefore(startDate)) && (endDate == null || !localDate.isAfter(endDate));
    }

    public static boolean isBetween(LocalTime localTime, LocalTime startTime, LocalTime endTime) {
        return (startTime == null || !localTime.isBefore(startTime)) && (endTime == null || !LocalTime.now().isAfter(endTime));
    }
}
