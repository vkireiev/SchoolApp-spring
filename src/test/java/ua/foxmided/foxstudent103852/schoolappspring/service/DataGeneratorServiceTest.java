package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@SpringBootTest(classes = { FileService.class, DataGeneratorService.class })
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class DataGeneratorServiceTest {

    @MockBean
    CourseService courseServiceMock;
    @MockBean
    private GroupService groupServiceMock;
    @MockBean
    private StudentService studentServiceMock;

    @Autowired
    DataGeneratorService dataGeneratorService;

    @Test
    void getRandomGroups_WhenCalledAndGroupsCountIsNegativeOrZero_ThenReturnEmptyListGroups() {
        assertTrue(dataGeneratorService
                .getRandomGroups(ConstantsTest.NEGATIVE_INT, Constants.GROUP_CHARACTERS, Constants.GROUP_DIGITS)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomGroups(ConstantsTest.ZERO_INT, Constants.GROUP_CHARACTERS, Constants.GROUP_DIGITS)
                .isEmpty());
    }

    @Test
    void getRandomGroups_WhenCalledAndCharactersIsNullOrEmpty_ThenReturnEmptyListGroups() {
        assertTrue(dataGeneratorService
                .getRandomGroups(Constants.GROUPS_COUNT, null, Constants.GROUP_DIGITS)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomGroups(Constants.GROUPS_COUNT, ConstantsTest.EMPTY_STRING, Constants.GROUP_DIGITS)
                .isEmpty());
    }

    @Test
    void getRandomGroups_WhenCalledAndDigitsIsNullOrEmpty_ThenReturnEmptyListGroups() {
        assertTrue(dataGeneratorService
                .getRandomGroups(Constants.GROUPS_COUNT, Constants.GROUP_CHARACTERS, null)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomGroups(Constants.GROUPS_COUNT, Constants.GROUP_CHARACTERS, ConstantsTest.EMPTY_STRING)
                .isEmpty());
    }

    @Test
    void getRandomGroups_WhenCalledAndValid_ThenReturnListGroups() {
        Set<Group> returnedResult = dataGeneratorService
                .getRandomGroups(Constants.GROUPS_COUNT, Constants.GROUP_CHARACTERS, Constants.GROUP_DIGITS);
        Set<String> groupNames = returnedResult.stream()
                .map(group -> group.getName())
                .collect(Collectors.toSet());
        assertEquals(Constants.GROUPS_COUNT, groupNames.size());
        for (Group group : returnedResult) {
            assertTrue(Pattern.matches(ConstantsTest.GROUP_NAME_CHECK_PATTERN, group.getName()));
        }
    }

    @Test
    void getRandomCourses_WhenCalledAndGroupsCountIsNegativeOrZero_ThenReturnEmptyListCourses() {
        assertTrue(dataGeneratorService
                .getRandomCourses(ConstantsTest.NEGATIVE_INT, Constants.COURSES_FILE)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomCourses(ConstantsTest.ZERO_INT, Constants.COURSES_FILE)
                .isEmpty());
    }

    @Test
    void getRandomCourses_WhenCalledAndCoursesFileIsInvalid_ThenReturnEmptyListGroups() {
        assertTrue(dataGeneratorService
                .getRandomCourses(Constants.COURSES_COUNT, null)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomCourses(Constants.COURSES_COUNT, ConstantsTest.EMPTY_STRING)
                .isEmpty());
    }

    @Test
    void getRandomCourses_WhenCalledAndCoursesFileIsNotExists_ThenException() {
        assertThrows(DataProcessingException.class, () -> dataGeneratorService
                .getRandomCourses(Constants.COURSES_COUNT, ConstantsTest.FILE_NOT_EXISTS),
                "DataProcessingException was expected");
    }

    @Test
    void getRandomCourses_WhenCalledAndValid_ThenReturnListCourses() {
        List<Course> returnedResult = dataGeneratorService
                .getRandomCourses(Constants.COURSES_COUNT, Constants.COURSES_FILE);
        Set<String> courseNames = returnedResult.stream()
                .filter(course -> !course.getName().isEmpty() && !course.getDescription().isEmpty())
                .map(course -> course.getName())
                .collect(Collectors.toSet());
        assertEquals(Constants.COURSES_COUNT, courseNames.size());
    }

    @Test
    void getRandomStudents_WhenCalledAndStudentsCountIsNegativeOrZero_ThenReturnEmptyListStudents() {
        List<Course> courses = Arrays.asList(new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        Set<Group> groups = new HashSet<>(Arrays.asList(new Group(ConstantsTest.GROUP_NAME)));
        assertTrue(dataGeneratorService
                .getRandomStudents(ConstantsTest.NEGATIVE_INT, Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE,
                        courses, groups)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomStudents(ConstantsTest.ZERO_INT, Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE,
                        courses, groups)
                .isEmpty());
    }

    @Test
    void getRandomStudents_WhenCalledAndLastNamesFileIsInvalid_ThenReturnEmptyListStudents() {
        List<Course> courses = Arrays.asList(new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        Set<Group> groups = new HashSet<>(Arrays.asList(new Group(ConstantsTest.GROUP_NAME)));
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, null, Constants.FIRST_NAMES_FILE,
                        courses, groups)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, ConstantsTest.EMPTY_STRING, Constants.FIRST_NAMES_FILE,
                        courses, groups)
                .isEmpty());
    }

    @Test
    void getRandomStudents_WhenCalledAndLastNamesFileIsNotExists_ThenException() {
        List<Course> courses = Arrays.asList(new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        Set<Group> groups = new HashSet<>(Arrays.asList(new Group(ConstantsTest.GROUP_NAME)));
        assertThrows(DataProcessingException.class, () -> dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, ConstantsTest.FILE_NOT_EXISTS, Constants.FIRST_NAMES_FILE,
                        courses, groups),
                "DataProcessingException was expected");
    }

    @Test
    void getRandomStudents_WhenCalledAndFirstNamesFileIsInvalid_ThenReturnEmptyListStudents() {
        List<Course> courses = Arrays.asList(new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        Set<Group> groups = new HashSet<>(Arrays.asList(new Group(ConstantsTest.GROUP_NAME)));
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, null,
                        courses, groups)
                .isEmpty());
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, ConstantsTest.EMPTY_STRING,
                        courses, groups)
                .isEmpty());
    }

    @Test
    void getRandomStudents_WhenCalledAndFirstNamesFileIsNotExists_ThenException() {
        List<Course> courses = Arrays.asList(new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        Set<Group> groups = new HashSet<>(Arrays.asList(new Group(ConstantsTest.GROUP_NAME)));
        assertThrows(DataProcessingException.class, () -> dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, ConstantsTest.FILE_NOT_EXISTS,
                        courses, groups),
                "DataProcessingException was expected");
    }

    @Test
    void getRandomStudents_WhenCalledAndCoursesIsInvalid_ThenReturnEmptyListStudents() {
        List<Course> courses = Arrays.asList();
        Set<Group> groups = new HashSet<>(Arrays.asList(new Group(ConstantsTest.GROUP_NAME)));
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE,
                        null, groups)
                .isEmpty());
        assertTrue(courses.isEmpty());
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE,
                        courses, groups)
                .isEmpty());
    }

    @Test
    void getRandomStudents_WhenCalledAndGroupsIsInvalid_ThenReturnEmptyListStudents() {
        List<Course> courses = Arrays.asList(new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        Set<Group> groups = new HashSet<>();
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE,
                        courses, null)
                .isEmpty());
        assertTrue(groups.isEmpty());
        assertTrue(dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE,
                        courses, groups)
                .isEmpty());
    }

    @Test
    void getRandomStudents_WhenCalledAndValid_ThenReturnListStudents() {
        List<Course> courses = dataGeneratorService.getRandomCourses(Constants.COURSES_COUNT, Constants.COURSES_FILE);
        assertFalse(courses.isEmpty());
        Set<Group> groups = dataGeneratorService
                .getRandomGroups(Constants.GROUPS_COUNT, Constants.GROUP_CHARACTERS, Constants.GROUP_DIGITS);
        assertFalse(groups.isEmpty());
        Set<Student> returnedResult = dataGeneratorService
                .getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE,
                        courses, groups);
        assertFalse(returnedResult.isEmpty());
        Set<String> studentNames = returnedResult.stream()
                .map(student -> student.getLastName() + student.getFirstName())
                .collect(Collectors.toSet());
        assertEquals(Constants.STUDENTS_COUNT, studentNames.size());
        assertTrue(returnedResult.stream()
                .anyMatch(student -> Objects.nonNull(student.getGroup())));
        assertTrue(returnedResult.stream()
                .allMatch(student -> student.getCourses().size() >= Constants.MIN_COURSES_PER_STUDENT
                        && student.getCourses().size() <= Constants.MAX_COURSES_PER_STUDENT));
    }

    @Test
    void fillDatabase_WhenCalledAndcourseServiceThrowsException_ThenReturnFalse() {
        Course courseMock = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        Student studentMock = new Student(Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE);
        Mockito.when(groupServiceMock.add(Mockito.any(Group.class))).thenThrow(DataProcessingException.class);
        Mockito.when(courseServiceMock.add(Mockito.any(Course.class))).thenReturn(courseMock);
        Mockito.when(studentServiceMock.add(Mockito.any(Student.class))).thenReturn(studentMock);
        assertFalse(dataGeneratorService.fillDatabase());
    }

    @Test
    void fillDatabase_WhenCalledAndGroupServiceThrowsException_ThenReturnFalse() {
        Group groupMock = new Group(ConstantsTest.GROUP_NAME);
        Student studentMock = new Student(Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE);
        Mockito.when(groupServiceMock.add(Mockito.any(Group.class))).thenReturn(groupMock);
        Mockito.when(courseServiceMock.add(Mockito.any(Course.class))).thenThrow(DataProcessingException.class);
        Mockito.when(studentServiceMock.add(Mockito.any(Student.class))).thenReturn(studentMock);
        assertFalse(dataGeneratorService.fillDatabase());
    }

    @Test
    void fillDatabase_WhenCalledAndStudentServiceThrowsException_ThenReturnFalse() {
        Group groupMock = new Group(ConstantsTest.GROUP_NAME);
        Course courseMock = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        Mockito.when(groupServiceMock.add(Mockito.any(Group.class))).thenReturn(groupMock);
        Mockito.when(courseServiceMock.add(Mockito.any(Course.class))).thenReturn(courseMock);
        Mockito.when(studentServiceMock.add(Mockito.any(Student.class))).thenThrow(DataProcessingException.class);
        assertFalse(dataGeneratorService.fillDatabase());
    }

    @Test
    void fillDatabase_WhenCalledAndValid_ThenFillDatabaseAndReturnTrue() {
        Group groupMock = new Group(ConstantsTest.GROUP_NAME);
        Course courseMock = new Course(ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION);
        Student studentMock = new Student(Constants.LAST_NAMES_FILE, Constants.FIRST_NAMES_FILE);
        Mockito.when(groupServiceMock.add(Mockito.any(Group.class))).thenReturn(groupMock);
        Mockito.when(courseServiceMock.add(Mockito.any(Course.class))).thenReturn(courseMock);
        Mockito.when(studentServiceMock.add(Mockito.any(Student.class))).thenReturn(studentMock);
        assertTrue(dataGeneratorService.fillDatabase());
        Mockito.verify(groupServiceMock, Mockito.times(Constants.GROUPS_COUNT)).add(Mockito.any(Group.class));
        Mockito.verify(courseServiceMock, Mockito.times(Constants.COURSES_COUNT)).add(Mockito.any(Course.class));
        Mockito.verify(studentServiceMock, Mockito.times(Constants.STUDENTS_COUNT)).add(Mockito.any(Student.class));
    }
}
