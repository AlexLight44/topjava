package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for userId={}", userId);

        List<Meal> meals = service.getAll(userId);
        return MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllDateTime(LocalDate startDate, LocalDate endDate,
                               LocalTime startTime, LocalTime endTime) {

        int userId = SecurityUtil.authUserId();
        log.info("getAll for userId={}, startDate={}, endDate={}, startTime={}, endTime={}",
                userId, startDate, endDate, startTime, endTime);

        List<Meal> meals = service.getAllDateTime(userId, startDate, endDate, null, null);

        List<MealTo> mealTos = MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY);

        if (startDate != null || endDate != null || startTime != null || endTime != null) {
            mealTos = mealTos.stream()
                    .filter(mealTo -> TimeUtil.isBetween(mealTo.getDate(), startDate, endDate))
                    .filter(mealTo -> TimeUtil.isBetween(mealTo.getTime(), startTime, endTime))
                    .collect(Collectors.toList());
        }
        return mealTos;
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create {} for user {}", meal, userId);
        checkIsNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("update {} for user {}", meal, userId);
        assureIdConsistent(meal, meal.getId());
        service.update(meal, userId);
    }
}