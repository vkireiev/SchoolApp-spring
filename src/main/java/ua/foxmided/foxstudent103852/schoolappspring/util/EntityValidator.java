package ua.foxmided.foxstudent103852.schoolappspring.util;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ua.foxmided.foxstudent103852.schoolappspring.exception.ConstraintViolationException;

public class EntityValidator {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private final Validator validator;

    @Autowired
    public EntityValidator(ValidatorFactory validatorFactory) {
        this.validator = validatorFactory.usingContext().getValidator();
    }

    public void nullCheck(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new ConstraintViolationException(String.format(message, object));
        }
    }

    public <T> void validate(T t, String message) {
        nullCheck(t, message);
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            String exceptionMessage = violations.stream()
                    .map(violation -> {
                        log.error(violation.getMessage() + ": " + t);
                        return violation.getMessage();
                    })
                    .collect(Collectors.joining(", "));
            throw new ConstraintViolationException(exceptionMessage + ": " + t);
        }
    }
}
