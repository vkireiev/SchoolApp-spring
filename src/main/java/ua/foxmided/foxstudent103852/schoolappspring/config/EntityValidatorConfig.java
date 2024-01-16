package ua.foxmided.foxstudent103852.schoolappspring.config;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.foxmided.foxstudent103852.schoolappspring.util.EntityValidator;

@Configuration
public class EntityValidatorConfig {
    @Bean
    public ValidatorFactory validatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }

    @Bean
    public EntityValidator entityValidator() {
        return new EntityValidator(validatorFactory());
    }
}
