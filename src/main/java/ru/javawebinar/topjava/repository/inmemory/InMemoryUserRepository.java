package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> idToUser = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        save(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return Objects.nonNull(idToUser.remove(id));
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            int id = counter.incrementAndGet();
            user.setId(id);
            idToUser.put(id, user);
            return user;
        }
        return idToUser.computeIfPresent(user.getId(), (_id, _oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return idToUser.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return idToUser.values()
                .stream()
                .sorted(Comparator.comparing(AbstractNamedEntity::getName))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return idToUser.values()
                .stream()
                .filter(user -> Objects.equals(email, user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
