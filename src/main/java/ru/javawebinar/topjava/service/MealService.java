package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredByTimeTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
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

    public List<MealTo> getAllByTimeAndDate(int userId, int caloriesPerDay, LocalTime startTime, LocalDate startDate, LocalTime endTime, LocalDate endDate) {
        Collection<Meal> filteredByDate = repository.getFilteredByDateInterval(userId, startDate, endDate);
        return getFilteredByTimeTos(filteredByDate, caloriesPerDay, startTime, endTime);
    }
}
