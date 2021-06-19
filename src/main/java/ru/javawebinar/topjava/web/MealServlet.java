package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MapMealDao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final static int caloriesPerDay = 2_000;
    private MealDao mealDao;

    @Override
    public void init() throws ServletException {
        super.init();
        mealDao = new MapMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet()");

        String action = request.getParameter("action");
        if (Objects.nonNull(action)) {
            String id = request.getParameter("id");
            Meal meal = null;
            switch (action) {
                case "create":
                    meal = new Meal();
                    break;
                case "edit":
                    meal = mealDao.get(Integer.parseInt(id));
                    break;
                case "delete":
                    mealDao.delete(Integer.parseInt(id));
                    response.sendRedirect("meals");
                    return;
            }

            if (Objects.equals("create", action) || Objects.equals("edit", action)) {
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("WEB-INF/jsp/edit.jsp").forward(request, response);
                return;
            }
        }

        List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
        mealsTo.sort((o1, o2) -> o1.getDateTime().isBefore(o2.getDateTime()) ? -1 : 0);
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher("WEB-INF/jsp/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.debug("doPost()");

        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("datetime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if (id.isEmpty()) {
            mealDao.add(new Meal(date, description, calories));
        } else {
            mealDao.update(new Meal(Integer.valueOf(id), date, description, calories));
        }

        resp.sendRedirect("meals");
    }
}
