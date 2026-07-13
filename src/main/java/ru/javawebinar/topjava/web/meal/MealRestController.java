package ru.javawebinar.topjava.web.meal;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController extends AbstractMealController{

    public MealRestController(MealService service) {
        super(service);
    }

    public Meal get(int id) {
        return getForUser(id);
    }

    public void delete(int id) {
        deleteForUser(id);
    }

    public List<MealTo> getAll() {
        return getAllForUser();
    }

    public Meal create(Meal meal) {
        return createForUser(meal);
    }

    public void update(Meal meal, int id) {
        updateForUser(meal, id);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        return getBetweenForUser(startDate, startTime, endDate, endTime);
    }
}