package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleReader;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleWriter;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@SpringBootTest(classes = { MenuTransactionService.class })
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class MenuTransactionServiceTest {

    @Value("${inf.msg.menu.console.item3.done}")
    private String menuItem3DoneMessage;

    @Value("${err.msg.menu.console.get.studentid.fail}")
    private String menuGetStudentIdFailMessage;

    @Value("${inf.msg.menu.console.item4.done}")
    private String menuItem4DoneMessage;

    @Value("${err.msg.menu.console.item5.fail}")
    private String menuItem5FailMessage;

    @Value("${inf.msg.menu.console.item5.done}")
    private String menuItem5DoneMessage;

    @Value("${err.msg.menu.console.item6.fail}")
    private String menuItem6FailMessage;

    @Value("${inf.msg.menu.console.item6.done}")
    private String menuItem6DoneMessage;

    @MockBean
    private CourseService courseServiceMock;
    @MockBean
    private GroupService groupServiceMock;
    @MockBean
    private StudentService studentServiceMock;
    @MockBean
    private ConsoleReader consoleReaderMock;
    @MockBean
    private ConsoleWriter consoleWriterMock;

    @Autowired
    private MenuTransactionService menuTransactionService;

    @Captor
    ArgumentCaptor<String> writerStringCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    void printGroupsWithFewerStudents_WhenCallAndResultListIsEmpty_ThenShowEmptyResult() throws IOException {
        long testValue = 15L;
        List<Group> resultGroupServiceMockito = new ArrayList<>(Arrays.asList());

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_1_MESSAGE);
        expectedResultList.add(String.format(Constants.MENU_PRINT_GROUPS_PATTERN, resultGroupServiceMockito.size()));

        Mockito.when(groupServiceMock.getGroupsWithFewerStudents(Mockito.longThat(argument -> (argument >= 0))))
                .thenReturn(resultGroupServiceMockito);
        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(testValue);

        menuTransactionService.printGroupsWithFewerStudents();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void printGroupsWithFewerStudents_WhenCallAndResultListIsEmpty_ThenShowResult() throws IOException {
        long testValue = 15L;
        List<Group> resultGroupServiceMockito = new ArrayList<>(Arrays.asList(
                new Group(1L, "EM-32"),
                new Group(4L, "NU-88"),
                new Group(5L, "CI-37")));

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_1_MESSAGE);
        expectedResultList.add(String.format(Constants.MENU_PRINT_GROUPS_PATTERN, resultGroupServiceMockito.size()));
        resultGroupServiceMockito.forEach(group -> expectedResultList.add(group.toString()));

        Mockito.when(groupServiceMock.getGroupsWithFewerStudents(Mockito.longThat(argument -> (argument >= 0))))
                .thenReturn(resultGroupServiceMockito);
        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(testValue);

        menuTransactionService.printGroupsWithFewerStudents();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void printStudentsWithCourse_WhenCallAndResultListIsEmpty_ThenShowEmptyResult() throws IOException {
        String testValue = "COMPUTER SCIENCE";
        List<Student> resultStudentServiceMockito = new ArrayList<>(Arrays.asList());
        List<Course> resultCourseServiceMockito = new ArrayList<>(Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"),
                new Course(3L, "ECONOMICS", "Description ECONOMICS"),
                new Course(5L, "HISTORY", "Description HISTORY")));
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_2_MESSAGE);
        expectedResultList.add(String.format(Constants.MENU_PRINT_COURSES_PATTERN, resultCourseServiceMockito.size()));
        resultCourseServiceMockito.forEach(course -> expectedResultList.add(course.toString()));
        expectedResultList
                .add(String.format(Constants.MENU_PRINT_STUDENTS_PATTERN, resultStudentServiceMockito.size()));
        resultStudentServiceMockito.forEach(student -> expectedResultList.add(student.toString()));

        Mockito.when(studentServiceMock.getStudentsByCourse(testValue))
                .thenReturn(resultStudentServiceMockito);
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(resultCourseServiceMockito);
        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        testValue,
                        Constants.MENU_EXIT_VALUE);

        menuTransactionService.printStudentsWithCourse();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void printStudentsWithCourse_WhenCallAndResultListIsNotEmpty_ThenShowResult() throws IOException {
        String testValue = "COMPUTER SCIENCE";
        List<Student> resultStudentServiceMockito = new ArrayList<>(Arrays.asList());
        Student student1 = new Student("Green", "Victoria");
        student1.setId(2L);
        student1.setGroup(null);
        student1.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        resultStudentServiceMockito.add(student1);
        Student student2 = new Student("Hoig", "Adrian");
        student2.setId(3L);
        student1.setGroup(new Group(7L, "KK-73"));
        student2.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        student2.getCourses().add(new Course(9L, "PHILOSOPHY", "Description PHILOSOPHY"));
        resultStudentServiceMockito.add(student2);
        List<Course> resultCourseServiceMockito = new ArrayList<>(Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"),
                new Course(3L, "ECONOMICS", "Description ECONOMICS"),
                new Course(5L, "HISTORY", "Description HISTORY")));
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_2_MESSAGE);
        expectedResultList.add(String.format(Constants.MENU_PRINT_COURSES_PATTERN, resultCourseServiceMockito.size()));
        resultCourseServiceMockito.forEach(course -> expectedResultList.add(course.toString()));
        expectedResultList
                .add(String.format(Constants.MENU_PRINT_STUDENTS_PATTERN, resultStudentServiceMockito.size()));
        resultStudentServiceMockito.forEach(student -> expectedResultList.add(student.toString()));

        Mockito.when(studentServiceMock.getStudentsByCourse(testValue))
                .thenReturn(resultStudentServiceMockito);
        Mockito.when(courseServiceMock.getAll())
                .thenReturn(resultCourseServiceMockito);
        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        testValue,
                        Constants.MENU_EXIT_VALUE);

        menuTransactionService.printStudentsWithCourse();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void addNewStudent_WhenCallAndTryAddExistsStudent_ThenShowExceptionMessage() throws IOException {
        Student newStudentMock = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudentMock.setGroup(new Group(3L, "FE-22"));
        List<Group> resultGroupServiceMockito = new ArrayList<>(Arrays.asList(
                new Group(1L, "EM-32"),
                new Group(3L, "FE-22"),
                new Group(5L, "CI-37")));
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_3_MESSAGE);
        expectedResultList.add(String.format(Constants.MENU_PRINT_GROUPS_PATTERN, resultGroupServiceMockito.size()));
        resultGroupServiceMockito.forEach(group -> expectedResultList.add(group.toString()));

        Mockito.when(groupServiceMock.getAll()).thenReturn(resultGroupServiceMockito);
        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        ConstantsTest.STUDENT_LAST_NAME,
                        ConstantsTest.STUDENT_FIRST_NAME);
        Mockito.when(consoleReaderMock.readYesNo(Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(consoleReaderMock.readNumberFromSet(Mockito.anyString(), Mockito.anySet(),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(newStudentMock.getGroup().getId());
        Mockito.when(studentServiceMock.add(Mockito.any(Student.class)))
                .thenThrow(new DataProcessingException(ConstantsTest.MOCKITO_DATA_PROCESSING_EXCEPTION_MESSAGE));

        assertThrows(DataProcessingException.class, () -> menuTransactionService.addNewStudent(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void addNewStudent_WhenCallAndTryAddNewStudent_ThenAddAndShowAddedStudent() throws IOException {
        Student newStudentMock = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudentMock.setId(1L);
        newStudentMock.setGroup(new Group(3L, "FE-22"));
        List<Group> resultGroupServiceMockito = new ArrayList<>(Arrays.asList(
                new Group(1L, "EM-32"),
                new Group(3L, "FE-22"),
                new Group(5L, "CI-37")));
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_3_MESSAGE);
        expectedResultList.add(String.format(Constants.MENU_PRINT_GROUPS_PATTERN, resultGroupServiceMockito.size()));
        resultGroupServiceMockito.forEach(group -> expectedResultList.add(group.toString()));
        expectedResultList.add(menuItem3DoneMessage);
        expectedResultList.add(newStudentMock.toString());

        Mockito.when(groupServiceMock.getAll()).thenReturn(resultGroupServiceMockito);
        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        ConstantsTest.STUDENT_LAST_NAME,
                        ConstantsTest.STUDENT_FIRST_NAME);
        Mockito.when(consoleReaderMock.readYesNo(Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(consoleReaderMock.readNumberFromSet(Mockito.anyString(), Mockito.anySet(),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(newStudentMock.getGroup().getId());
        Mockito.when(studentServiceMock.add(Mockito.any(Student.class)))
                .thenReturn(newStudentMock);

        menuTransactionService.addNewStudent();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void deleteStudentById_WhenCallAndTryDeleteNotExistsStudent_ThenShowFailMessage() throws IOException {
        long notExistsStudentId = 202L;
        Optional<Student> deletedStudentMock = Optional.empty();
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_4_MESSAGE);
        expectedResultList.add(String.format(menuGetStudentIdFailMessage, notExistsStudentId));

        Mockito.when(consoleReaderMock.readString(Mockito.anyString()))
                .thenReturn(
                        Constants.MENU_ITEM_4_VALUE);
        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(notExistsStudentId);
        Mockito.when(studentServiceMock.delete(Mockito.anyLong()))
                .thenReturn(deletedStudentMock);

        menuTransactionService.deleteStudentById();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void deleteStudentById_WhenCallAndTryDeleteExistsStudent_ThenDeleteAndShowDeletedStudent() throws IOException {
        long existsStudentId = 202L;
        Optional<Student> deletedStudentMock = Optional.of(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        deletedStudentMock.get().setId(existsStudentId);
        deletedStudentMock.get().setGroup(new Group(3L, "FE-22"));
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_4_MESSAGE);
        expectedResultList.add(deletedStudentMock.get().toString());
        expectedResultList.add(String.format(menuItem4DoneMessage, existsStudentId));

        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(existsStudentId);
        Mockito.when(studentServiceMock.get(Mockito.anyLong()))
                .thenReturn(deletedStudentMock);
        Mockito.when(studentServiceMock.delete(Mockito.anyLong()))
                .thenReturn(deletedStudentMock);

        menuTransactionService.deleteStudentById();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void addCourseForStudent_WhenCallAndTryAssignCourseForNotExistsStudent_ThenShowFailMessage() throws IOException {
        long studentId = 202L;
        Optional<Student> studentMock = Optional.empty();
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_5_MESSAGE);
        expectedResultList.add(String.format(menuGetStudentIdFailMessage, studentId));

        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(studentId);
        Mockito.when(studentServiceMock.get(Mockito.anyLong()))
                .thenReturn(studentMock);

        menuTransactionService.addCourseForStudent();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void addCourseForStudent_WhenCallAndTryAssignDuplicateCourse_ThenShowFailMessage() throws IOException {
        long studentId = 202L;
        Optional<Student> studentMock = Optional.of(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        studentMock.get().setId(studentId);
        studentMock.get().setGroup(new Group(3L, "FE-22"));
        studentMock.get().getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        studentMock.get().getCourses().add(new Course(3L, "ECONOMICS", "Description ECONOMICS"));
        studentMock.get().getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        long assignCourseId = 3L;
        List<Course> resultCourseServiceMockito = new ArrayList<>(Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"),
                new Course(3L, "ECONOMICS", "Description ECONOMICS"),
                new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"),
                new Course(5L, "HISTORY", "Description HISTORY")));
        List<Course> printCoursesList = resultCourseServiceMockito;
        printCoursesList.removeAll(studentMock.get().getCourses());

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_5_MESSAGE);
        expectedResultList.add(studentMock.get().toString());
        expectedResultList.add(String.format(Constants.MENU_PRINT_COURSES_PATTERN, printCoursesList.size()));
        printCoursesList.forEach(course -> expectedResultList.add(course.toString()));
        expectedResultList.add(menuItem5FailMessage);
        expectedResultList.add(studentMock.get().toString());

        Mockito.when(courseServiceMock.getAll())
                .thenReturn(resultCourseServiceMockito);
        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(studentId);
        Mockito.when(consoleReaderMock.readNumberFromSet(Mockito.anyString(), Mockito.anySet(),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(assignCourseId);
        Mockito.when(studentServiceMock.get(Mockito.anyLong()))
                .thenReturn(studentMock);
        Mockito.when(studentServiceMock.addCourseForStudent(Mockito.any(Student.class), Mockito.any(Course.class)))
                .thenReturn(false);

        menuTransactionService.addCourseForStudent();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void addCourseForStudent_WhenCallAndTryAssignCourseAndValid_ThenAssignCourse() throws IOException {
        long studentId = 202L;
        Optional<Student> studentMock = Optional.of(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        studentMock.get().setId(studentId);
        studentMock.get().setGroup(new Group(3L, "FE-22"));
        studentMock.get().getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        studentMock.get().getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));

        Optional<Student> updatedStudentMock = Optional.of(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        updatedStudentMock.get().setId(studentId);
        updatedStudentMock.get().setGroup(new Group(3L, "FE-22"));
        updatedStudentMock.get().getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        updatedStudentMock.get().getCourses().add(new Course(3L, "ECONOMICS", "Description ECONOMICS"));
        updatedStudentMock.get().getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));

        long assignCourseId = 3L;
        List<Course> resultCourseServiceMockito = new ArrayList<>(Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"),
                new Course(3L, "ECONOMICS", "Description ECONOMICS"),
                new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"),
                new Course(5L, "HISTORY", "Description HISTORY")));
        List<Course> printCoursesList = resultCourseServiceMockito;
        printCoursesList.removeAll(studentMock.get().getCourses());

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_5_MESSAGE);
        expectedResultList.add(studentMock.get().toString());
        expectedResultList.add(String.format(Constants.MENU_PRINT_COURSES_PATTERN, printCoursesList.size()));
        printCoursesList.forEach(course -> expectedResultList.add(course.toString()));
        expectedResultList.add(menuItem5DoneMessage);
        expectedResultList.add(updatedStudentMock.get().toString());

        Mockito.when(courseServiceMock.getAll())
                .thenReturn(resultCourseServiceMockito);
        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(studentId);
        Mockito.when(consoleReaderMock.readNumberFromSet(Mockito.anyString(), Mockito.anySet(),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(assignCourseId);
        Mockito.doReturn(studentMock)
                .doReturn(updatedStudentMock)
                .when(studentServiceMock).get(Mockito.anyLong());
        Mockito.when(studentServiceMock.addCourseForStudent(Mockito.any(Student.class), Mockito.any(Course.class)))
                .thenReturn(true);
        Mockito.when(studentServiceMock.update(Mockito.any(Student.class))).thenReturn(updatedStudentMock);

        menuTransactionService.addCourseForStudent();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void removeCourseForStudent_WhenCallAndTryRemoveCourseForNotExistsStudent_ThenShowFailMessage() throws IOException {
        long studentId = 202L;
        Optional<Student> studentMock = Optional.empty();
        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_6_MESSAGE);
        expectedResultList.add(String.format(menuGetStudentIdFailMessage, studentId));

        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(studentId);
        Mockito.when(studentServiceMock.get(Mockito.anyLong()))
                .thenReturn(studentMock);
        Mockito.when(studentServiceMock.removeCourseForStudent(Mockito.any(Student.class), Mockito.any(Course.class)))
                .thenReturn(false);

        menuTransactionService.removeCourseForStudent();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void removeCourseForStudent_WhenCallAndTryRemoveNotAnssignedCourse_ThenShowFailMessage() throws IOException {
        long studentId = 202L;
        Optional<Student> studentMock = Optional.of(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        studentMock.get().setId(studentId);
        studentMock.get().setGroup(new Group(3L, "FE-22"));
        studentMock.get().getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        studentMock.get().getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        long assignCourseId = 3L;
        List<Course> resultCourseServiceMockito = new ArrayList<>(Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"),
                new Course(3L, "ECONOMICS", "Description ECONOMICS"),
                new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"),
                new Course(5L, "HISTORY", "Description HISTORY")));
        List<Course> printCoursesList = studentMock.get().getCourses();

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_6_MESSAGE);
        expectedResultList.add(studentMock.get().toString());
        expectedResultList.add(String.format(Constants.MENU_PRINT_COURSES_PATTERN, printCoursesList.size()));
        printCoursesList.forEach(course -> expectedResultList.add(course.toString()));
        expectedResultList.add(menuItem6FailMessage);
        expectedResultList.add(studentMock.get().toString());

        Mockito.when(courseServiceMock.getAll())
                .thenReturn(resultCourseServiceMockito);
        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(studentId);
        Mockito.when(consoleReaderMock.readNumberFromSet(Mockito.anyString(), Mockito.anySet(),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(assignCourseId);
        Mockito.when(studentServiceMock.get(Mockito.anyLong()))
                .thenReturn(studentMock);
        Mockito.when(studentServiceMock.removeCourseForStudent(Mockito.any(Student.class), Mockito.any(Course.class)))
                .thenReturn(false);

        menuTransactionService.removeCourseForStudent();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }

    @Test
    void removeCourseForStudent_WhenCallAndTryRemoveCourseAndValid_ThenRemoveCourse() throws IOException {
        long studentId = 202L;
        Optional<Student> studentMock = Optional.of(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        studentMock.get().setId(studentId);
        studentMock.get().setGroup(new Group(3L, "FE-22"));
        studentMock.get().getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        studentMock.get().getCourses().add(new Course(3L, "ECONOMICS", "Description ECONOMICS"));
        studentMock.get().getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));

        Optional<Student> updatedStudentMock = Optional.of(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        updatedStudentMock.get().setId(studentId);
        updatedStudentMock.get().setGroup(new Group(3L, "FE-22"));
        updatedStudentMock.get().getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        updatedStudentMock.get().getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));

        long assignCourseId = 3L;
        List<Course> resultCourseServiceMockito = new ArrayList<>(Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"),
                new Course(3L, "ECONOMICS", "Description ECONOMICS"),
                new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"),
                new Course(5L, "HISTORY", "Description HISTORY")));
        List<Course> printCoursesList = studentMock.get().getCourses();

        List<String> expectedResultList = new ArrayList<>();
        expectedResultList.add(Constants.MENU_SELECTED_ITEM_PREFIX + Constants.MENU_ITEM_6_MESSAGE);
        expectedResultList.add(studentMock.get().toString());
        expectedResultList.add(String.format(Constants.MENU_PRINT_COURSES_PATTERN, printCoursesList.size()));
        printCoursesList.forEach(course -> expectedResultList.add(course.toString()));
        expectedResultList.add(menuItem6DoneMessage);
        expectedResultList.add(updatedStudentMock.get().toString());

        Mockito.when(courseServiceMock.getAll())
                .thenReturn(resultCourseServiceMockito);
        Mockito.when(consoleReaderMock.readNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(studentId);
        Mockito.when(consoleReaderMock.readNumberFromSet(Mockito.anyString(), Mockito.anySet(),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(assignCourseId);
        Mockito.doReturn(studentMock)
                .doReturn(updatedStudentMock)
                .when(studentServiceMock).get(Mockito.anyLong());
        Mockito.when(studentServiceMock.removeCourseForStudent(Mockito.any(Student.class), Mockito.any(Course.class)))
                .thenReturn(true);

        menuTransactionService.removeCourseForStudent();
        Mockito.verify(consoleWriterMock, Mockito.atLeast(1)).printMessage(writerStringCaptor.capture());
        List<String> captoredResultList = writerStringCaptor.getAllValues();
        assertEquals(expectedResultList.size(), captoredResultList.size());
        assertTrue(expectedResultList.containsAll(captoredResultList));
        assertTrue(captoredResultList.containsAll(expectedResultList));
    }
}
