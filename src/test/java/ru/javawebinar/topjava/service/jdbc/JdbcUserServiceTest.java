package ru.javawebinar.topjava.service.jdbc;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(JDBC)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
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
    public void test1_update() {
        User updated = getUpdated();
        service.update(updated);
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, getUpdated());
    }

    @Test
    public void test2_getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }
}