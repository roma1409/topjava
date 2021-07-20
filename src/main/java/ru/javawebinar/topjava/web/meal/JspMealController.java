package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getMeals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getFilteredMeals(Model model, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
                                   @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        LocalDate parsedStartDate = parseLocalDate(startDate);
        LocalDate parsedEndDate = parseLocalDate(endDate);
        LocalTime parsedStartTime = parseLocalTime(startTime);
        LocalTime parsedEndTime = parseLocalTime(endTime);
        model.addAttribute("meals", super.getBetween(parsedStartDate, parsedStartTime, parsedEndDate, parsedEndTime));
        return "meals";
    }

    @GetMapping("/form")
    public String showForm(Model model, @RequestParam(required = false) Integer id) {
        Meal meal = Objects.nonNull(id) ? super.get(id) : new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Integer id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @PostMapping("/create")
    public String create(@RequestParam String dateTime, @RequestParam String description, @RequestParam Integer calories) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        super.create(meal);
        return "redirect:/meals";
    }

    @PostMapping("/update")
    public String update(@RequestParam String dateTime, @RequestParam String description, @RequestParam Integer calories,
                         @RequestParam Integer id) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        super.update(meal, id);
        return "redirect:/meals";
    }
}
