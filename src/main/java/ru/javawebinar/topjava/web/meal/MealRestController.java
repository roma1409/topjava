package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        log.info("create {} for user with id {}", meal, userId);
        checkNew(meal);
        return service.create(userId, meal);
    }

    public void update(Meal Meal, int mealId) {
        int userId = authUserId();
        log.info("update {} with mealId={} for user with id {}", Meal, mealId, userId);
        assureIdConsistent(Meal, mealId);
        service.update(userId, Meal);
    }

    public void delete(int mealId) {
        int userId = authUserId();
        log.info("delete {} for user with id {}", mealId, userId);
        service.delete(userId, mealId);
    }

    public Object get(int mealId) {
        int userId = authUserId();
        log.info("get {} for user with id {}", mealId, userId);
        return service.get(userId, mealId);
    }

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.info("getAll for user with id {}", userId);
        return service.getAll(userId, authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByTimeAndDate(LocalTime startTime, LocalDate startDate, LocalTime endTime, LocalDate endDate) {
        int userId = authUserId();
        log.info("getAllByTime for user with id {}", userId);
        return service.getAllByTimeAndDate(userId, authUserCaloriesPerDay(), startTime, startDate, endTime, endDate);
    }
}
