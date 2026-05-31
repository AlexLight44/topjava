package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))

public class JdbcMealRepositoryTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private JdbcMealRepository repository;

    @Test
    public void save() {
        Meal created = repository.save(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(repository.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                repository.save(new Meal(null, meal1.getDateTime(), "Duplicate", 500), USER_ID));
    }

    @Test
    public void delete() {
        assertTrue(repository.delete(MEAL_ID, USER_ID));
        assertNull(repository.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertFalse(repository.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void get() {
        Meal meal = repository.get(MEAL_ID, USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getNotFound() {
        assertNull(repository.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = repository.getAll(USER_ID);
        assertMatch(all, meal2, meal1);
    }

    @Test
    public void getForeignMeal() {
        assertNull(repository.get(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void deleteForeignMeal() {
        assertFalse(repository.delete(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void updateForeignMeal() {
        Meal updated = getUpdated();
        assertNull(repository.save(updated, ADMIN_ID));
    }
}