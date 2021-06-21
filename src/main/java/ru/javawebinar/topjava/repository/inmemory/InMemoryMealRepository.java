package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<Integer, Map<Integer, Meal>> userIdToMeal = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private final UserRepository userRepository;

    public InMemoryMealRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        MealsUtil.meals.forEach(e -> save(1, e));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save meal {} for user with id {}", meal, userId);
        synchronized (userRepository.get(userId)) {
            Map<Integer, Meal> mealIdToMeal = getUserMeals(userId);
            if (meal.isNew()) {
                int mealId = counter.incrementAndGet();
                meal.setId(mealId);
                mealIdToMeal.put(mealId, meal);
                userIdToMeal.put(userId, mealIdToMeal);
                return meal;
            }
            // handle case: update, but not present in storage
            return mealIdToMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.info("delete meal with id {} for user with id {}", mealId, userId);
        synchronized (userRepository.get(userId)) {
            return Objects.nonNull(getUserMeals(userId).remove(mealId));
        }
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("get meal {} for user with id {}", mealId, userId);
        return getUserMeals(userId).get(mealId);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll meals for user with id {}", userId);
        return getUserMeals(userId)
                .values()
                .stream()
                .sorted((o1, o2) -> o1.getDateTime().isAfter(o2.getDateTime()) ? -1 : 0)
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getUserMeals(int userId) {
        log.info("getUserMeal for user with id {}", userId);
        return userIdToMeal.getOrDefault(
                userId,
                new ConcurrentHashMap<>()
        );
    }
}

