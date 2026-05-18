package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 8, 0),
                "Завтрак админа", 100), 2);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 12, 0),
                "Обед админа", 700), 2);
        save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 18, 0),
                "Ужин админа", 450), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for user {}", meal, userId);
        Map<Integer, Meal> userMeals = mealsMap.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} for user {}", id, userId);
        Map<Integer, Meal> userMeals = getMapUserMeals(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for user {}", id, userId);
        Map<Integer, Meal> userMeals = getMapUserMeals(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for user {}", userId);
        Map<Integer, Meal> userMeals = getMapUserMeals(userId);
        if (userMeals == null) {
            return Collections.emptyList();
        }
        return userMeals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate,
                             LocalTime startTime, LocalTime endTime) {
        log.info("getAll for user {} with filter [{}, {}] [{}, {}]",
                userId, startDate, endDate, startTime, endTime);
        List<Meal> meals = getAll(userId);
        if (startDate == null && endDate == null && startTime == null && endTime == null) {
            return meals;
        }
        return meals.stream()
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalDate(), startDate, endDate))
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMapUserMeals(int userId) {
        return mealsMap.get(userId);
    }
}

