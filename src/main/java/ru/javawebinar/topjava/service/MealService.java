package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    public void delete(int userId, int mealId) {
        checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    public Object get(int userId, int mealId) {
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getAllByTimeAndDate(int userId, int caloriesPerDay, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Collection<Meal> meals = repository.getAll(userId);
        List<Meal> filteredByDate = getFilteredByDate(meals, startDateTime.toLocalDate(), endDateTime.toLocalDate());
        return getFilteredByTimeTos(filteredByDate, caloriesPerDay, startDateTime.toLocalTime(), endDateTime.toLocalTime());
    }
}
