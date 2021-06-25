package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_1 = START_SEQ + 2;
    public static final int USER_MEAL_2 = START_SEQ + 3;
    public static final int USER_MEAL_3 = START_SEQ + 4;
    public static final int USER_MEAL_4 = START_SEQ + 5;
    public static final int USER_MEAL_5 = START_SEQ + 6;
    public static final int USER_MEAL_6 = START_SEQ + 7;
    public static final int USER_MEAL_7 = START_SEQ + 8;
    public static final int ADMIN_MEAL_1 = START_SEQ + 9;
    public static final int ADMIN_MEAL_2 = START_SEQ + 10;
    public static final int NOT_FOUND = 100;

    public static final Meal userMeal1 = new Meal(USER_MEAL_1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal2 = new Meal(USER_MEAL_2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal3 = new Meal(USER_MEAL_3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal userMeal4 = new Meal(USER_MEAL_4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на пограничное значение", 100);
    public static final Meal userMeal5 = new Meal(USER_MEAL_5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userMeal6 = new Meal(USER_MEAL_6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal userMeal7 = new Meal(USER_MEAL_7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_1, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL_2, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2077, Month.JULY, 7, 7, 7), "new-descr", 777);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal1);
        updated.setDescription("updated-descr");
        updated.setCalories(888);
        updated.setDateTime(LocalDateTime.now());
        return updated;
    }
}
