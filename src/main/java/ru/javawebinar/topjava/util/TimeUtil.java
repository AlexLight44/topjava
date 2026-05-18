package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeUtil {
    public static LocalDate parseLocalDate(String date) {
        return date == null || date.isEmpty() ? null : LocalDate.parse(date);
    }

    public static LocalTime parseLocalTime(String time) {
        return time == null || time.isEmpty() ? null : LocalTime.parse(time);
    }

    public static boolean isBetween(LocalDate localDate, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || !localDate.isBefore(startDate)) && (endDate == null || !localDate.isBefore(endDate));
    }

    public static boolean isBetween(LocalTime localTime, LocalTime startTime, LocalTime endTime) {
        return (startTime == null || !localTime.isBefore(startTime)) && (endTime == null || localTime.isBefore(endTime));
    }
}
