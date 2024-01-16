package ua.foxmided.foxstudent103852.schoolappspring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan("ua.foxmided.foxstudent103852.schoolappspring.model")
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddCourseWithNullNameAndNullDescription_ThenException() {
        Course newCourse = new Course(null, null);
        assertNull(this.testEntityManager.getId(newCourse));
        assertThrows(ValidationException.class, () -> courseRepository.save(newCourse),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddCourseWithNullName_ThenException() {
        Course newCourse = new Course(null, ConstantsTest.COURSE_DESCRIPTION);
        assertNull(this.testEntityManager.getId(newCourse));
        assertThrows(ValidationException.class, () -> courseRepository.save(newCourse),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddCourseWithNullDescription_ThenException() {
        Course newCourse = new Course(ConstantsTest.COURSE_NAME, null);
        assertNull(this.testEntityManager.getId(newCourse));
        assertThrows(ValidationException.class, () -> courseRepository.save(newCourse),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newCourse));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddValidCourse_ThenShoudAddAndReturnAddedEntity() {
        Course newCourse = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        assertNull(this.testEntityManager.getId(newCourse));
        assertTrue(courseRepository.save(newCourse) instanceof Course);
        assertNotNull(this.testEntityManager.getId(newCourse));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddDuplicateCourse_ThenException() {
        Course newCourse = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        Course duplicateCourse = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        assertNull(this.testEntityManager.getId(newCourse));
        assertNull(this.testEntityManager.getId(duplicateCourse));
        courseRepository.save(newCourse);
        assertNotNull(this.testEntityManager.getId(newCourse));
        assertThrows(DataAccessException.class, () -> courseRepository.save(duplicateCourse),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(duplicateCourse));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findById_WhenIdIsNull_ThenException() {
        assertThrows(DataAccessException.class, () -> courseRepository.findById(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findById_WhenNotExistsCourse_ThenReturnOptionalNull() {
        assertFalse(courseRepository.findById(0L).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void findById_WhenExistsCourse_ThenReturnOptionalCourse() {
        Optional<Course> courseExpected = Optional.of(new Course(6L, "LAW", "Description LAW"));
        Optional<Course> courseReturned = courseRepository.findById(6L);
        assertTrue(courseReturned.isPresent());
        assertEquals(courseExpected, courseReturned);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsCourses_ThenReturnEmptyListCourses() {
        assertThat(courseRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_WhenExistsCourses_ThenReturnListCourses() {
        List<Course> courseExpected = Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"),
                new Course(3L, "ECONOMICS", "Description ECONOMICS"),
                new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"),
                new Course(5L, "HISTORY", "Description HISTORY"),
                new Course(6L, "LAW", "Description LAW"),
                new Course(7L, "MATHEMATICS", "Description MATHEMATICS"),
                new Course(8L, "MEDICINE", "Description MEDICINE"),
                new Course(9L, "PHILOSOPHY", "Description PHILOSOPHY"),
                new Course(10L, "SOCIOLOGY", "Description SOCIOLOGY"));
        List<Course> coursesReturned = (List<Course>) courseRepository.findAll();
        assertThat(coursesReturned).isNotEmpty();
        assertEquals(courseExpected.size(), coursesReturned.size());
        assertTrue(courseExpected.containsAll(coursesReturned));
        assertTrue(coursesReturned.containsAll(courseExpected));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsCourses_ThenReturnZero() {
        assertEquals(0, courseRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void count_WhenExistsCourses_ThenReturnCountCourses() {
        assertEquals(10L, courseRepository.count());
    }
}
