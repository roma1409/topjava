package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, Integer.parseInt(id));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        (Meal) controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                List<MealTo> mealTos;
                if (checkNullOrEmpty(startDateStr) && checkNullOrEmpty(endDateStr) && checkNullOrEmpty(startTimeStr) && checkNullOrEmpty(endTimeStr)) {
                    mealTos = controller.getAll();
                } else {
                    LocalDate startDate = parseDateOrNull(startDateStr);
                    LocalTime startTime = parseTimeOrNull(startTimeStr);
                    LocalDate endDate = parseDateOrNull(endDateStr);
                    LocalTime endTime = parseTimeOrNull(endTimeStr);
                    mealTos = controller.getAllByTimeAndDate(startTime, startDate, endTime, endDate);
                }
                request.setAttribute("meals", mealTos);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private boolean checkNullOrEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

    private LocalDate parseDateOrNull(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private LocalTime parseTimeOrNull(String date) {
        try {
            return LocalTime.parse(date);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
