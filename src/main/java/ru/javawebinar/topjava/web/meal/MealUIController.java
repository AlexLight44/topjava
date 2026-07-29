package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/meals")
public class MealUIController extends AbstractMealController {

    @PostMapping
    @ResponseBody
    public Meal create(@RequestParam String dateTime,
                       @RequestParam String description,
                       @RequestParam int calories) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        return super.create(meal);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }
}
