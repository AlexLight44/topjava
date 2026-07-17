package ru.javawebinar.topjava.service.datajpa;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void springCacheInAction() {
        List<User> before = service.getAll();
        userRepository.delete(USER_ID);
        List<User> after = service.getAll();
        assertThat(before.size()).isGreaterThan(after.size());
    }

    @Test
    public void getWithMeals() {
        User admin = service.getWithMeals(ADMIN_ID);
        USER_MATCHER.assertMatch(admin, admin);
        MEAL_MATCHER.assertMatch(admin.getMeals(), MealTestData.adminMeal2, MealTestData.adminMeal1);
    }

    @Test
    public void getWithMealsNotFound() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeals(NOT_FOUND));
    }

    @PersistenceContext
    private EntityManager em;

    @Test
    public void hibernate2lvlCacheInAction() {
        User before = service.get(USER_ID);
        assertThat(before).isNotNull();
        // Удаляем по секрету от Hibernate
        Session session = em.unwrap(Session.class);
        session.doWork(connection -> {
            try (Statement st = connection.createStatement()) {
                st.executeUpdate("DELETE FROM users");
            }
        });
        // если кеш отключен, повторный запрос выполнится и ничего не найдет, а если не отключен, то вернется юзер из кеша
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }
}