package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractJspController;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractJspController {

    @Autowired
    private MealService service;

    @GetMapping
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        List<Meal> meals = service.getAll(userId);
        List<MealTo> mealsTo = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meals", mealsTo);
        return "meals";
    }

    @GetMapping(params = "action=filter")
    public String filter(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
        log.info("filter meals: dates({} - {}), time({} - {}) for user {}",
                startDate, endDate, startTime, endTime, userId);
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, userId);
        List<MealTo> mealsTo = MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        model.addAttribute("meals", mealsTo);
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("create for user {}", userId);
        model.addAttribute(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(@RequestParam int id, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("update meal {} for user {}", id, userId);
        model.addAttribute("meal", service.get(id, userId));
        return "mealForm";
    }

    @PostMapping
    public String save(HttpServletRequest request) {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        String idParam = request.getParameter("id");
        Integer id = (idParam == null || idParam.isEmpty()) ? null : Integer.parseInt(idParam);

        Meal meal = new Meal(id, dateTime, description, calories);

        int userId = SecurityUtil.authUserId();

        if (meal.isNew()) {
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        } else {
            log.info("update {} for user {}", meal, userId);
            service.update(meal, userId);
        }

        return "redirect:/meals";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }
}
