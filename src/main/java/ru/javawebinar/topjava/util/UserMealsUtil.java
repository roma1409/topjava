package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

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
        Map<Integer, Integer> dayToCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            int dayOfMonth = meal.getDateTime().getDayOfMonth();
//            Integer calories = dayToCalories.getOrDefault(dayOfMonth, 0);
//            calories += meal.getCalories();
            Integer calories = dayToCalories.merge(dayOfMonth, meal.getCalories(), Integer::sum);
            dayToCalories.put(dayOfMonth, calories);
        }

        List<UserMealWithExcess> excessMeals = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                int dayOfMonth = meal.getDateTime().getDayOfMonth();
                Integer calories = dayToCalories.get(dayOfMonth);
                boolean excess = calories > caloriesPerDay;
                excessMeals.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }

        return excessMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<Integer, Integer> dayToCalories = meals.stream()
                .parallel()
                .collect(
                        Collectors.groupingBy(meal -> meal.getDateTime().getDayOfMonth(),
                                Collectors.reducing(0, UserMeal::getCalories, Integer::sum))
                );

        return meals.stream()
                .parallel()
                .filter(e -> isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime))
                .map(e -> new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), dayToCalories.get(e.getDateTime().getDayOfMonth()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
