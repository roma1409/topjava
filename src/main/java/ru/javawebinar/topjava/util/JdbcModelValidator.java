package ru.javawebinar.topjava.util;

import javax.validation.*;
import java.util.Set;

public class JdbcModelValidator {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T>boolean validate(T model) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return true;
    }
}
