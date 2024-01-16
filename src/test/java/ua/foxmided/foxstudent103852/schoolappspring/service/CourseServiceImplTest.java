package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ua.foxmided.foxstudent103852.schoolappspring.config.EntityValidatorConfig;
import ua.foxmided.foxstudent103852.schoolappspring.exception.ConstraintViolationException;
import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.repository.CourseRepository;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@SpringBootTest(classes = { CourseServiceImpl.class })
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { EntityValidatorConfig.class })
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class CourseServiceImplTest {

    @MockBean
    CourseRepository courseRepositoryMock;

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void add_WhenCalledWithNullAsCourseEntity_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> courseService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);
    }

    @Test
    void add_WhenCalledWithEmptyCourseName_ThenException() {
        Course course1 = new Course(null, ConstantsTest.COURSE_DESCRIPTION);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);

        Course course2 = new Course(ConstantsTest.EMPTY_STRING, ConstantsTest.COURSE_DESCRIPTION);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);
    }

    @Test
    void add_WhenCalledWithEmptyCourseDescription_ThenException() {
        Course course1 = new Course(ConstantsTest.COURSE_NAME, null);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);

        Course course2 = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.EMPTY_STRING);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);
    }

    @Test
    void add_WhenCalledWithNotValidCourseName_ThenException() {
        Course course1 = new Course(ConstantsTest.COURSE_NAME_NOT_VALID_1, ConstantsTest.COURSE_DESCRIPTION);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);

        Course course2 = new Course(ConstantsTest.COURSE_NAME_NOT_VALID_2, ConstantsTest.COURSE_DESCRIPTION);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);

        Course course3 = new Course(ConstantsTest.COURSE_NAME_NOT_VALID_3, ConstantsTest.COURSE_DESCRIPTION);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course3),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);
    }

    @Test
    void add_WhenCalledWithNotValidCourseDescription_ThenException() {
        Course course1 = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION_NOT_VALID_1);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);

        Course course2 = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION_NOT_VALID_2);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);

        Course course3 = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION_NOT_VALID_3);
        assertThrows(ConstraintViolationException.class, () -> courseService.add(course3),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(0)).save(Mockito.any(Course.class));
        Mockito.verifyNoInteractions(courseRepositoryMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(courseRepositoryMock.save(Mockito.any(Course.class))).thenThrow(BadSqlGrammarException.class);
        Course addCourse = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        assertThrows(DataProcessingException.class, () -> courseService.add(addCourse),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).save(Mockito.any(Course.class));
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }

    @Test
    void add_WhenCalledWithValidCourseEntity_ThenAddAndReturnAddedEntity() {
        Course resultMockito = new Course(1L, ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        Mockito.when(courseRepositoryMock.save(Mockito.any(Course.class))).thenReturn(resultMockito);
        Course resultService = courseService.add(new Course(ConstantsTest.COURSE_NAME,
                ConstantsTest.COURSE_DESCRIPTION));
        assertTrue(resultService instanceof Course);
        assertNotNull(resultService.getId());
        assertEquals(resultMockito, resultService);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).save(Mockito.any(Course.class));
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndExistsCourses_ShouldReturnListCourses() {
        List<Course> resultMockito = Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        Mockito.when(courseRepositoryMock.findAll()).thenReturn(resultMockito);
        List<Course> resultService = courseService.getAll();
        assertFalse(resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndNotExistsCourses_ShouldReturnEmptyListCourses() {
        List<Course> resultMockito = Arrays.asList();
        Mockito.when(courseRepositoryMock.findAll()).thenReturn(resultMockito);
        List<Course> resultService = courseService.getAll();
        assertTrue(resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(courseRepositoryMock.findAll()).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> courseService.getAll(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }

    @Test
    void count_WhenCalledAndExistsCourses_ShouldReturnCoursesCount() {
        long resultMockito = 10L;
        Mockito.when(courseRepositoryMock.count()).thenReturn(resultMockito);
        long resultService = courseService.count();
        assertEquals(resultMockito, resultService);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }

    @Test
    void count_WhenCalledAndNotExistsCourses_ShouldReturnZero() {
        long resultMockito = ConstantsTest.ZERO_LONG;
        Mockito.when(courseRepositoryMock.count()).thenReturn(resultMockito);
        long resultService = courseService.count();
        assertEquals(resultMockito, resultService);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }

    @Test
    void count_WhenCalledAndNotExistsCoursesTable_ThenException() {
        Mockito.when(courseRepositoryMock.count()).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> courseService.count(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(courseRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(courseRepositoryMock);
    }
}
