package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dailyCaloriesMap = new HashMap<>();
        for (UserMeal userMealExcess : meals) {
            dailyCaloriesMap.merge(userMealExcess.getDateTime().toLocalDate(), userMealExcess.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> resultingList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                int sumCalories = dailyCaloriesMap.get(userMeal.getDateTime().toLocalDate());
                boolean dailyCaloriesOfDays = sumCalories > caloriesPerDay;
                resultingList.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), dailyCaloriesOfDays));
            }
        }
        return resultingList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCaloriesMap = meals.stream().collect(Collectors.groupingBy(
                meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream().filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)).map(meal -> {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            int sumCalories = dailyCaloriesMap.get(localDate);
            boolean dailyCaloriesOfDays = sumCalories > caloriesPerDay;
            return new UserMealWithExcess(
                    meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories(),
                    dailyCaloriesOfDays
            );
        }).collect(Collectors.toList());
    }
}
