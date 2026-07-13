package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController{

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", getAllForUser());
        return "meals";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", getBetweenForUser(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("GET /create for user {}", userId);
        model.addAttribute(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(@RequestParam int id, Model model) {
        model.addAttribute("meal", getForUser(id));
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
        if (meal.isNew()) {
            createForUser(meal);
        } else {
            updateForUser(meal, id);
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        deleteForUser(id);
        return "redirect:/meals";
    }
}
