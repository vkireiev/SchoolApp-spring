package ua.foxmided.foxstudent103852.schoolappspring.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import ua.foxmided.foxstudent103852.schoolappspring.config.EntityValidatorConfig;
import ua.foxmided.foxstudent103852.schoolappspring.exception.ConstraintViolationException;

@SpringBootTest
@ContextConfiguration(classes = { EntityValidatorConfig.class })
class EntityValidatorTest {

    @Autowired
    private EntityValidator entityValidator;

    @Test
    void nullCheck_WhenEntityIsNull_ThenException() {
        assertThrows(ConstraintViolationException.class,
                () -> entityValidator.nullCheck(null,
                        ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    void nullCheck_WhenEntityIsNotNull_ThenNothing() {
        Long object = new Long(ConstantsTest.ZERO_LONG);
        assertDoesNotThrow(() -> entityValidator.nullCheck(object, ConstantsTest.NOT_EMPTY_STRING));
    }

    @Test
    void validate_WhenEntityHaveNotNullConstraintAndNotValid_ThenException() {
        class TestEntity {
            @NotNull
            private Long testParameter;

            public TestEntity() {
            }

            public Long getTestParameter() {
                return testParameter;
            }

            public void setTestParameter(Long testParameter) {
                this.testParameter = testParameter;
            }
        }
        TestEntity testEntity = new TestEntity();
        testEntity.setTestParameter(null);

        assertThrows(ConstraintViolationException.class,
                () -> entityValidator.validate(testEntity,
                        ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
    }

    @Test
    void validate_WhenEntityHaveNotNullConstraintAndValid_ThenNothing() {
        class TestEntity {
            @NotNull
            private Long testParameter;

            public TestEntity() {
            }

            public Long getTestParameter() {
                return testParameter;
            }

            public void setTestParameter(Long testParameter) {
                this.testParameter = testParameter;
            }
        }
        TestEntity testEntity = new TestEntity();
        testEntity.setTestParameter(ConstantsTest.ZERO_LONG);

        assertDoesNotThrow(() -> entityValidator.validate(testEntity, ConstantsTest.NOT_EMPTY_STRING));
    }
}
