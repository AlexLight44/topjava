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
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractJspController;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractJspController {

    @Autowired
    private MealService service;

    @GetMapping
    public String getAll(HttpServletRequest request, Model model) {
        log.info("meals");
        List<MealTo> mealsTo;
        if ("filter".equals(request.getParameter("action"))) {
            LocalDate startDate = parseOrNull(request.getParameter("startDate"));
            LocalDate endDate   = parseOrNull(request.getParameter("endDate"));

            List<Meal> meals = service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId());
            mealsTo = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        } else {
            List<Meal> meals = service.getAll(SecurityUtil.authUserId());
            mealsTo = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        }

        model.addAttribute("meals", mealsTo);
        return "meals";
    }

    private LocalDate parseOrNull(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("create");
        model.addAttribute(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(@RequestParam int id, Model model) {
        log.info("update");
        model.addAttribute("meal", service.get(id, SecurityUtil.authUserId()));
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
            service.create(meal, userId);
        } else {
            service.update(meal, userId);
        }

        return "redirect:/meals";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        log.info("delete");
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }
}
