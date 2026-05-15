package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MealService service;

    public Collection<Meal> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll");
        return service.getAll(userId);
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
        assureIdConsistent(meal, userId);
        service.update(meal, userId);
    }
}