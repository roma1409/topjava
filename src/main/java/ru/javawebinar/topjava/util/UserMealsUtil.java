package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateToCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
//            Integer calories = dateToCalories.getOrDefault(date, 0);
//            calories += meal.getCalories();
            Integer calories = dateToCalories.merge(date, meal.getCalories(), Integer::sum);
            dateToCalories.put(date, calories);
        }

        List<UserMealWithExcess> excessMeals = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDateTime dateTime = meal.getDateTime();
            if (isBetweenHalfOpen(dateTime.toLocalTime(), startTime, endTime)) {
                LocalDate date = dateTime.toLocalDate();
                Integer calories = dateToCalories.get(date);
                boolean excess = calories > caloriesPerDay;
                excessMeals.add(new UserMealWithExcess(dateTime, meal.getDescription(), meal.getCalories(), excess));
            }
        }

        return excessMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayToCalories = meals.stream()
                .parallel()
                .collect(
                        Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                                Collectors.reducing(0, UserMeal::getCalories, Integer::sum))
                );

        return meals.stream()
                .parallel()
                .filter(e -> isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime))
                .map(e -> {
                    LocalDateTime dateTime = e.getDateTime();
                    String description = e.getDescription();
                    int calories = e.getCalories();
                    boolean excess = dayToCalories.get(dateTime.toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExcess(dateTime, description, calories, excess);
                })
                .collect(Collectors.toList());
    }
}
