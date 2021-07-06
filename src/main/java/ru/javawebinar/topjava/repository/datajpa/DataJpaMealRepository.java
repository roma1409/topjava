package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository mealRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository mealRepository, CrudUserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getOne(userId));
        return (!meal.isNew() && Objects.isNull(get(meal.id(), userId))) ?
                null :
                mealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Optional<Meal> optionalMeal = mealRepository.findById(id);
        return optionalMeal.isPresent() && optionalMeal.get().getUser().getId() == userId ? optionalMeal.get() : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.findAllByUserIdAndDateTimeBetweenOrderByDateTimeDesc(userId, startDateTime, endDateTime)
                .stream()
                .filter(meal -> meal.getDateTime().compareTo(endDateTime) < 0)
                .toList();
    }
}
