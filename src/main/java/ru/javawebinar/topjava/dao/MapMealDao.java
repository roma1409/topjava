package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class MapMealDao implements MealDao {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> idToMeals = new ConcurrentHashMap<>();

    public MapMealDao() {
        Stream.of(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ).forEach(this::add);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(idToMeals.values());
    }

    public Meal get(Integer id) {
        return idToMeals.get(id);
    }

    public void add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        idToMeals.put(meal.getId(), meal);
    }

    public void update(Meal meal) {
        idToMeals.put(meal.getId(), meal);
    }

    public void delete(int id) {
        idToMeals.remove(id);
    }
}
