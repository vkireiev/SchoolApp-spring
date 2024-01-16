package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;
import ua.foxmided.foxstudent103852.schoolappspring.repository.StudentRepository;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@SpringBootTest(classes = { StudentServiceImpl.class })
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { EntityValidatorConfig.class })
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class StudentServiceImplTest {

    @MockBean
    StudentRepository studentRepositoryMock;

    @Autowired
    StudentServiceImpl studentService;

    @Test
    void add_WhenCalledWithNullAsStudentEntity_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> studentService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void add_WhenCalledWithEmptyLastName_ThenException() {
        Student student1 = new Student(null, ConstantsTest.STUDENT_FIRST_NAME);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student2 = new Student(ConstantsTest.EMPTY_STRING, ConstantsTest.STUDENT_FIRST_NAME);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void add_WhenCalledWithNotValidLastName_ThenException() {
        Student student1 = new Student(ConstantsTest.STUDENT_LAST_NAME_NOT_VALID_1, ConstantsTest.STUDENT_FIRST_NAME);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student2 = new Student(ConstantsTest.STUDENT_LAST_NAME_NOT_VALID_2, ConstantsTest.STUDENT_FIRST_NAME);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student3 = new Student(ConstantsTest.STUDENT_LAST_NAME_NOT_VALID_3, ConstantsTest.STUDENT_FIRST_NAME);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student3),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void add_WhenCalledWithEmptyFirstName_ThenException() {
        Student student1 = new Student(ConstantsTest.STUDENT_LAST_NAME, null);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student2 = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.EMPTY_STRING);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void add_WhenCalledWithNotValidFirstName_ThenException() {
        Student student1 = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME_NOT_VALID_1);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student2 = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME_NOT_VALID_2);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student3 = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME_NOT_VALID_3);
        assertThrows(ConstraintViolationException.class, () -> studentService.add(student3),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void add_WhenCalledWithNotInitializedListCourses_ThenException() {
        Student student = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);

        assertThrows(NullPointerException.class, () -> {
            student.setCourses(null);
            studentService.add(student);
        }, ConstantsTest.NULL_POINTER_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verify(studentRepositoryMock, Mockito.times(0)).save(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        Student student = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        Mockito.when(studentRepositoryMock.save(Mockito.any(Student.class))).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.add(student),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void add_WhenCalledWithValidStudentEntity_ThenAddToDataBase() {
        Student resultMockito = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        resultMockito.setId(1L);
        resultMockito.setGroup(new Group(5L, "CI-37"));
        resultMockito.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        resultMockito.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));

        Mockito.when(studentRepositoryMock.save(Mockito.any(Student.class))).thenReturn(resultMockito);

        Student resultService = studentService.add(new Student(ConstantsTest.STUDENT_LAST_NAME,
                ConstantsTest.STUDENT_FIRST_NAME));
        assertNotNull(resultService.getId());
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void get_WhenCalledWithNullAsStudentId_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> studentService.get(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).findById(null);
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void get_WhenCalledAndNotExistsStudent_ThenReturnOptionalNull() {
        Optional<Student> resultMockito = Optional.empty();
        Mockito.when(studentRepositoryMock.findById(Mockito.anyLong())).thenReturn(resultMockito);
        Optional<Student> resultService = studentService.get(15L);
        assertFalse(resultService.isPresent());
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void get_WhenCalledAndExistsStudent_ThenReturnOptionalStudent() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        Optional<Student> resultMockito = Optional.ofNullable(student);
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(resultMockito);
        Optional<Student> resultService = studentService.get(student.getId());
        assertTrue(resultService.isPresent());
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(student.getId());
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void get_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(studentRepositoryMock.findById(Mockito.anyLong())).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.get(5L),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndNotExistsStudents_ShouldReturnEmptyListStudents() {
        List<Student> resultMockito = Arrays.asList();
        Mockito.when(studentRepositoryMock.findAll()).thenReturn(resultMockito);
        List<Student> resultService = studentService.getAll();
        assertTrue(resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndExistsStudents_ShouldReturnListStudents() {
        List<Student> resultMockito = new ArrayList<>();
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        resultMockito.add(student);
        student = new Student("Green", "Victoria");
        student.setId(2L);
        student.setGroup(null);
        student.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        resultMockito.add(student);
        Mockito.when(studentRepositoryMock.findAll()).thenReturn(resultMockito);
        List<Student> resultService = studentService.getAll();
        assertFalse(resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(studentRepositoryMock.findAll()).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.getAll(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNullAsStudentEntity_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> studentService.update(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).update(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void update_WhenCalledWithEmptyLastName_ThenException() {
        Student student1 = new Student(null, ConstantsTest.STUDENT_FIRST_NAME);
        assertThrows(ConstraintViolationException.class, () -> studentService.update(student1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).update(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student2 = new Student(ConstantsTest.EMPTY_STRING, ConstantsTest.STUDENT_FIRST_NAME);
        assertThrows(ConstraintViolationException.class, () -> studentService.update(student2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).update(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void update_WhenCalledWithEmptyFirstName_ThenException() {
        Student student1 = new Student(ConstantsTest.STUDENT_LAST_NAME, null);
        assertThrows(ConstraintViolationException.class, () -> studentService.update(student1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).update(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);

        Student student2 = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.EMPTY_STRING);
        assertThrows(ConstraintViolationException.class, () -> studentService.update(student2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).update(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNotInitializedListCourses_ThenException() {
        Student student = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);

        assertThrows(NullPointerException.class, () -> {
            student.setCourses(null);
            studentService.update(student);
        }, ConstantsTest.NULL_POINTER_EXCEPTION_EXPECTED_MESSAGE);

        Mockito.verify(studentRepositoryMock, Mockito.times(0)).update(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void update_WhenCalledWithNotExistsStudent_ThenNotUpdateAndReturnOptionalNull() {
        Optional<Student> resultMockito = Optional.empty();
        Student updateStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        updateStudent.setId(1L);
        updateStudent.setGroup(new Group(5L, "CI-37"));
        updateStudent.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        updateStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        Mockito.when(studentRepositoryMock.findById(updateStudent.getId())).thenReturn(resultMockito);
        assertFalse(studentService.update(updateStudent).isPresent());
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).update(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void update_WhenCalledWithExistsStudent_ThenUpdateAndReturnUpdatedEntity() {
        Student updateStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        updateStudent.setId(1L);
        updateStudent.setGroup(new Group(5L, "CI-37"));
        updateStudent.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        updateStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        Optional<Student> resultMockito = Optional.of(updateStudent);
        Mockito.when(studentRepositoryMock.findById(updateStudent.getId())).thenReturn(resultMockito);
        Mockito.when(studentRepositoryMock.update(updateStudent)).thenReturn(updateStudent.getId());
        assertTrue(studentService.update(updateStudent).get() instanceof Student);
        Mockito.verify(studentRepositoryMock, Mockito.times(2)).findById(Mockito.anyLong());
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void update_WhenCalledAndThrowsDataAccessException_ThenException() {
        Student updateStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        updateStudent.setId(1L);
        updateStudent.setGroup(new Group(5L, "CI-37"));
        updateStudent.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        updateStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        Optional<Student> resultMockito = Optional.of(updateStudent);
        Mockito.when(studentRepositoryMock.findById(updateStudent.getId())).thenReturn(resultMockito);
        Mockito.when(studentRepositoryMock.save(Mockito.any(Student.class))).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.update(updateStudent),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsStudentId_ThenException() {
        Long nullStudentId = null;
        assertThrows(ConstraintViolationException.class, () -> studentService.delete(nullStudentId),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).delete(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNullAsStudentEntity_ThenException() {
        Student nullStudent = null;
        assertThrows(ConstraintViolationException.class, () -> studentService.delete(nullStudent),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).delete(Mockito.any(Student.class));
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithNotExistsStudent_ThenNotDeleteAndReturnOptionalNull() {
        Student studentMock = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        studentMock.setId(11L);
        studentMock.setGroup(new Group(5L, "CI-37"));
        studentMock.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        studentMock.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        Mockito.when(studentRepositoryMock.findById(studentMock.getId())).thenReturn(Optional.empty());
        Optional<Student> resultService = studentService.delete(studentMock.getId());
        assertEquals(Optional.empty(), resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(studentMock.getId());
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).delete(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void delete_WhenCalledWithExistsStudent_ThenDeleteAndReturnOptionalDeletedStudent() {
        Student studentMock = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        studentMock.setId(11L);
        studentMock.setGroup(new Group(5L, "CI-37"));
        studentMock.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        studentMock.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        Mockito.when(studentRepositoryMock.findById(studentMock.getId())).thenReturn(Optional.of(studentMock));
        doNothing().when(studentRepositoryMock).delete(Mockito.any(Student.class));
        Optional<Student> resultService = studentService.delete(studentMock.getId());
        assertTrue(resultService.isPresent());
        assertEquals(studentMock, resultService.get());
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(studentMock.getId());
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).delete(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void delete_WhenCalledAndThrowsDataAccessException_ThenException() {
        Student studentMock = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        studentMock.setId(11L);
        studentMock.setGroup(new Group(5L, "CI-37"));
        studentMock.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        studentMock.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        Mockito.when(studentRepositoryMock.findById(studentMock.getId())).thenReturn(Optional.of(studentMock));
        doThrow(BadSqlGrammarException.class).when(studentRepositoryMock).delete(Mockito.any(Student.class));
        assertThrows(DataProcessingException.class, () -> studentService.delete(studentMock.getId()),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(studentMock.getId());
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).delete(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void addCourseForStudent_WhenCalledWithNullAsStudentEntity_ThenException() {
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        assertThrows(ConstraintViolationException.class, () -> studentService.addCourseForStudent(null, course),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void addCourseForStudent_WhenCalledWithNullAsCourseEntity_ThenException() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        assertThrows(ConstraintViolationException.class, () -> studentService.addCourseForStudent(student, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void addCourseForStudent_WhenCalledAndStudentNotExists_ThenReturnFalse() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.empty());
        assertFalse(studentService.addCourseForStudent(student, course));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void addCourseForStudent_WhenCalledAndStudentExistsAndAssignedCourse_ThenReturnFalse() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.of(student));
        assertFalse(studentService.addCourseForStudent(student, course));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void addCourseForStudent_WhenCalledAndExistsStudentAndNotAssignedCourse_ThenAddCourseAndReturnTrue() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.of(student));
        Mockito.when(studentRepositoryMock.update(Mockito.any(Student.class))).thenReturn(student.getId());
        assertTrue(studentService.addCourseForStudent(student, course));
        Mockito.verify(studentRepositoryMock, Mockito.times(3)).findById(Mockito.any(Long.class));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void addCourseForStudent_WhenCalledAndThrowsDataAccessException_ThenException() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.ofNullable(student));
        Mockito.when(studentRepositoryMock.save(Mockito.any(Student.class))).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.addCourseForStudent(student, course),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(2)).findById(Mockito.any(Long.class));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void removeCourseForStudent_WhenCalledWithNullAsStudentEntity_ThenException() {
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        assertThrows(ConstraintViolationException.class, () -> studentService.removeCourseForStudent(null, course),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void removeCourseForStudent_WhenCalledWithNullAsCourseEntity_ThenException() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        assertThrows(ConstraintViolationException.class, () -> studentService.removeCourseForStudent(student, null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void removeCourseForStudent_WhenCalledAndStudentNotExists_ThenReturnFalse() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.ofNullable(null));
        assertFalse(studentService.removeCourseForStudent(student, course));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void removeCourseForStudent_WhenCalledAndStudentExistsWithNotAssignedCourse_ThenReturnFalse() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.ofNullable(student));
        assertFalse(studentService.removeCourseForStudent(student, course));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void removeCourseForStudent_WhenCalledAndStudentExistsWithAssignedCourse_ThenRemoveCourseAndReturnTrue() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.ofNullable(student));
        Mockito.when(studentRepositoryMock.update(Mockito.any(Student.class))).thenReturn(student.getId());
        assertTrue(studentService.removeCourseForStudent(student, course));
        Mockito.verify(studentRepositoryMock, Mockito.times(3)).findById(Mockito.any(Long.class));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void removeCourseForStudent_WhenCalledAndThrowsDataAccessException_ThenException() {
        Student student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Course course = new Course(8L, "MEDICINE", "Description MEDICINE");
        Mockito.when(studentRepositoryMock.findById(student.getId())).thenReturn(Optional.ofNullable(student));
        Mockito.when(studentRepositoryMock.save(Mockito.any(Student.class))).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.removeCourseForStudent(student, course),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(2)).findById(Mockito.any(Long.class));
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).save(Mockito.any(Student.class));
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void count_WhenCalledAndNotExistsStudents_ShouldReturnZero() {
        long resultMockito = ConstantsTest.ZERO_LONG;
        Mockito.when(studentRepositoryMock.count()).thenReturn(resultMockito);
        long resultService = studentService.count();
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void count_WhenCalledAndExistsStudents_ShouldReturnStudentsCount() {
        long resultMockito = 10L;
        Mockito.when(studentRepositoryMock.count()).thenReturn(resultMockito);
        long resultService = studentService.count();
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void count_WhenCalledAndNotExistsStudentsTable_ThenException() {
        Mockito.when(studentRepositoryMock.count()).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.count(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void getStudentsByCourse_WhenCalledWithNullAsCourseName_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> studentService.getStudentsByCourse(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(0)).getStudentsByCourse(Mockito.any());
        Mockito.verifyNoInteractions(studentRepositoryMock);
    }

    @Test
    void getStudentsByCourse_WhenCalledWithExistsCourseName_ThenReturnStudentsList() {
        String courseName = "LAW";
        List<Student> resultMockito = new ArrayList<>();
        Student student;
        student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        resultMockito.add(student);
        student = new Student("Green", "Victoria");
        student.setId(2L);
        student.setGroup(null);
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        resultMockito.add(student);
        student = new Student("Johnson", "Jack");
        student.setId(4L);
        student.setGroup(new Group(1L, "EM-32"));
        student.getCourses().add(new Course(3L, "ECONOMICS", "Description ECONOMICS"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        resultMockito.add(student);
        Mockito.when(studentRepositoryMock.getStudentsByCourse(courseName)).thenReturn(resultMockito);
        List<Student> resultService = studentService.getStudentsByCourse(courseName);
        assertFalse(resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).getStudentsByCourse(courseName);
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }

    @Test
    void getStudentsByCourse_WhenCalledAndThrowsDataAccessException_ThenException() {
        String courseName = "LAW";
        Mockito.when(studentRepositoryMock.getStudentsByCourse(courseName)).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> studentService.getStudentsByCourse(courseName),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(studentRepositoryMock, Mockito.times(1)).getStudentsByCourse(courseName);
        Mockito.verifyNoMoreInteractions(studentRepositoryMock);
    }
}
