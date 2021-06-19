package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealDao implements MealDao {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> idToMeals = new ConcurrentHashMap<>();

    public MapMealDao() {
        MealsUtil.meals.forEach(this::add);
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
