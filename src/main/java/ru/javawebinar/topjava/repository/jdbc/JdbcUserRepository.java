package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                        UPDATE users SET name=:name, email=:email, password=:password,\s
                        registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    \s""", parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }
        setRoles(user);
        return user;
    }

    private void setRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (roles.isEmpty()) return;
        List<Object[]> batch = roles.stream()
                .map(role -> new Object[]{user.getId(), role.name()}).toList();
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_role (user_id, role) VALUES (?, ?)", batch
        );
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setRoles(getRoles(id));
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setRoles(getRoles(user.getId()));
        }
        return user;
    }

    private Set<Role> getRoles(int userId) {
        List<Role> roles = jdbcTemplate.query(
                "SELECT role FROM user_role WHERE user_id=?",
                (rs, rowNum) -> Role.valueOf(rs.getString("role")),
                userId);
        return roles.isEmpty() ? Collections.emptySet() : EnumSet.copyOf(roles);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> userRolesMap = new HashMap<>();
        jdbcTemplate.query("SELECT user_id, role FROM user_role", rs -> {
            int userId = rs.getInt("user_id");
            Role role = Role.valueOf(rs.getString("role"));
            userRolesMap.computeIfAbsent(userId, k -> EnumSet.noneOf(Role.class)).add(role);
        });
        users.forEach(user -> user.setRoles(userRolesMap.getOrDefault(user.getId(), Collections.emptySet())));
        return users;
    }
}
