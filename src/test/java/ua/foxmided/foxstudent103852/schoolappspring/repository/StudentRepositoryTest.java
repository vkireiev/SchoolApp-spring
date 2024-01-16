package ua.foxmided.foxstudent103852.schoolappspring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan("ua.foxmided.foxstudent103852.schoolappspring.model")
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class StudentRepositoryTest {
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddStudentWithNullLastName_ThenException() {
        Student newStudent = new Student(null, ConstantsTest.STUDENT_FIRST_NAME);
        assertNull(this.testEntityManager.getId(newStudent));
        assertThrows(ValidationException.class, () -> studentRepository.save(newStudent),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddStudentWithNullFirstName_ThenException() {
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, null);
        assertNull(this.testEntityManager.getId(newStudent));
        assertThrows(ValidationException.class, () -> studentRepository.save(newStudent),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddStudentWithNullGroup_ThenShoudAddAndReturnAddedEntity() {
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(null);
        assertNull(this.testEntityManager.getId(newStudent));
        assertTrue(studentRepository.save(newStudent) instanceof Student);
        assertNotNull(this.testEntityManager.getId(newStudent));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void save_WhenAddStudentWithExistsGroup_ThenShoudAddAndReturnAddedEntity() {
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(new Group(3L, "FE-22"));
        assertNull(this.testEntityManager.getId(newStudent));
        assertTrue(studentRepository.save(newStudent) instanceof Student);
        assertNotNull(this.testEntityManager.getId(newStudent));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddStudentWithNonExistsGroup_ThenException() {
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(new Group(12L, ConstantsTest.GROUP_NAME));
        assertNull(this.testEntityManager.getId(newStudent));
        assertThrows(DataAccessException.class, () -> studentRepository.save(newStudent),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(newStudent));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void save_WhenAddStudentWithExistsGroupAndExistsCourses_ThenShoudAddAndReturnAddedEntity() {
        int studentsCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(new Group(3L, "FE-22"));
        newStudent.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        newStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        assertNull(this.testEntityManager.getId(newStudent));
        newStudent = studentRepository.save(newStudent);
        testEntityManager.flush();
        assertTrue(newStudent instanceof Student);
        assertNotNull(this.testEntityManager.getId(newStudent));
        int studentsCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        assertEquals((studentsCountBeforeAdd + 1), studentsCountAfterAdd);
        assertEquals((coursesCountBeforeAdd + newStudent.getCourses().size()), coursesCountAfterAdd);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void save_WhenAddStudentWithDuplicatesLastNameAndFirstName_ThenException() {
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        Student duplicateStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(new Group(3L, "FE-22"));
        newStudent.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        newStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        assertNull(this.testEntityManager.getId(newStudent));
        assertNull(this.testEntityManager.getId(duplicateStudent));
        studentRepository.save(newStudent);
        testEntityManager.flush();
        assertNotNull(this.testEntityManager.getId(newStudent));
        assertThrows(DataAccessException.class, () -> {
            studentRepository.save(duplicateStudent);
            testEntityManager.flush();
        }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(duplicateStudent));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void save_WhenAddStudentWithExistsGroupAndDuplicateCourses_ThenException() {
        int studentsCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(new Group(3L, "FE-22"));
        newStudent.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        newStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        newStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        newStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        assertNull(this.testEntityManager.getId(newStudent));
        assertThrows(DataAccessException.class, () -> {
            studentRepository.save(newStudent);
            testEntityManager.flush();
        }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        int studentsCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        assertEquals(studentsCountBeforeAdd, studentsCountAfterAdd);
        assertEquals(coursesCountBeforeAdd, coursesCountAfterAdd);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void save_WhenAddStudentWithExistsGroupAndNonExistsCourse_ThenException() {
        int studentsCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(new Group(3L, "FE-22"));
        newStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        newStudent.getCourses().add(new Course(12L, ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        assertNull(this.testEntityManager.getId(newStudent));
        assertThrows(DataAccessException.class, () -> studentRepository.save(newStudent),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        int studentsCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        assertEquals(studentsCountBeforeAdd, studentsCountAfterAdd);
        assertEquals(coursesCountBeforeAdd, coursesCountAfterAdd);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void save_WhenAddStudentWithNullGroupAndNonExistsCourse_ThenException() {
        int studentsCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountBeforeAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        Student newStudent = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        newStudent.setGroup(null);
        newStudent.getCourses().add(new Course(4L, "GEOGRAPHY", "Description GEOGRAPHY"));
        newStudent.getCourses().add(new Course(12L, ConstantsTest.COURSE_NAME, ConstantsTest.COURSE_DESCRIPTION));
        assertNull(this.testEntityManager.getId(newStudent));
        assertThrows(DataAccessException.class, () -> studentRepository.save(newStudent),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        int studentsCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountAfterAdd = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        assertEquals(studentsCountBeforeAdd, studentsCountAfterAdd);
        assertEquals(coursesCountBeforeAdd, coursesCountAfterAdd);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findById_WhenStudentIdIsNull_ThenException() {
        assertThrows(DataAccessException.class, () -> studentRepository.findById(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findById_WhenNotExistsStudent_ThenReturnOptionalNull() {
        assertFalse(studentRepository.findById(0L).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void findById_WhenExistsStudent_ThenReturnOptionalStudent() {
        Optional<Student> studentExpected = Optional.of(new Student("Walker", "Logan"));
        studentExpected.get().setId(5L);
        studentExpected.get().setGroup(new Group(3L, "FE-22"));
        studentExpected.get().getCourses().add(new Course(10L, "SOCIOLOGY", "Description SOCIOLOGY"));
        studentExpected.get().getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        Optional<Student> studentReturned = studentRepository.findById(5L);
        assertEquals(studentExpected.get(), studentReturned.get());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findAll_WhenNotExistsStudents_ThenReturnEmptyListStudents() {
        assertThat(studentRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void getAll_WhenExistsStudents_ThenReturnListStudents() {
        List<Student> studentsExpected = new ArrayList<>();
        Student student;
        student = new Student("Henris", "Mia");
        student.setId(1L);
        student.setGroup(new Group(2L, "UJ-33"));
        student.getCourses().add(new Course(6L, "LAW", "Description LAW"));
        student.getCourses().add(new Course(8L, "MEDICINE", "Description MEDICINE"));
        studentsExpected.add(student);
        student = new Student("Green", "Victoria");
        student.setId(2L);
        student.setGroup(null);
        student.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        studentsExpected.add(student);
        student = new Student("Hoig", "Adrian");
        student.setId(3L);
        student.setGroup(new Group(7L, "KK-73"));
        student.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        student.getCourses().add(new Course(9L, "PHILOSOPHY", "Description PHILOSOPHY"));
        studentsExpected.add(student);
        student = new Student("Johnson", "Jack");
        student.setId(4L);
        student.setGroup(new Group(1L, "EM-32"));
        student.getCourses().add(new Course(3L, "ECONOMICS", "Description ECONOMICS"));
        studentsExpected.add(student);
        student = new Student("Walker", "Logan");
        student.setId(5L);
        student.setGroup(new Group(3L, "FE-22"));
        student.getCourses().add(new Course(10L, "SOCIOLOGY", "Description SOCIOLOGY"));
        student.getCourses().add(new Course(7L, "MATHEMATICS", "Description MATHEMATICS"));
        studentsExpected.add(student);
        student = new Student("Perez", "Jackson");
        student.setId(6L);
        student.setGroup(new Group(1L, "EM-32"));
        studentsExpected.add(student);
        student = new Student("Perez", "Samuel");
        student.setId(7L);
        student.setGroup(new Group(2L, "UJ-33"));
        studentsExpected.add(student);
        student = new Student("Miller", "Olivia");
        student.setId(8L);
        student.setGroup(null);
        studentsExpected.add(student);
        student = new Student("Thompson", "Scarlett");
        student.setId(9L);
        student.setGroup(new Group(1L, "EM-32"));
        studentsExpected.add(student);
        List<Student> studentsReturned = (List<Student>) studentRepository.findAll();
        assertThat(studentsReturned).isNotEmpty();
        assertEquals(studentsExpected.size(), studentsReturned.size());
        assertTrue(studentsExpected.containsAll(studentsReturned));
        assertTrue(studentsReturned.containsAll(studentsExpected));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateNotExistsStudent_ThenException() {
        Student studentForUpdate = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        studentForUpdate.setId(15L);
        studentForUpdate.setGroup(new Group(2L, "UJ-33"));
        studentForUpdate.getCourses().add(new Course(9L, "PHILOSOPHY", "Description PHILOSOPHY"));
        studentForUpdate.getCourses().add(new Course(10L, "SOCIOLOGY", "Description SOCIOLOGY"));
        studentForUpdate.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        assertThrows(DataProcessingException.class, () -> studentRepository.update(studentForUpdate),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentOnAnotherExistsStudent_ThenException() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        long existsStudentId = 2L;
        long updateStudentId = 4L;

        Optional<Student> existsStudent = transactionTemplate.execute(status -> {
            Optional<Student> student = studentRepository.findById(existsStudentId);
            assertTrue(student.isPresent());
            return student;
        });
        Optional<Student> studentForUpdate = transactionTemplate.execute(status -> {
            Optional<Student> student = studentRepository.findById(updateStudentId);
            assertTrue(student.isPresent());
            return student;
        });

        assertNotEquals(existsStudent.get().getLastName(), studentForUpdate.get().getLastName());
        assertNotEquals(existsStudent.get().getFirstName(), studentForUpdate.get().getFirstName());
        studentForUpdate.get().setLastName(existsStudent.get().getLastName());
        studentForUpdate.get().setFirstName(existsStudent.get().getFirstName());
        assertThrows(DataAccessException.class, () -> studentRepository.update(studentForUpdate.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndChangeLastName_ThenShoudUpdateAndReturnEntityId() {
        long updateStudentId = 4L;
        Optional<Student> studentForUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentForUpdate.isPresent());
        String lastNameStudentForUpdate = studentForUpdate.get().getLastName();
        assertNotEquals(ConstantsTest.STUDENT_LAST_NAME, studentForUpdate.get().getLastName());
        studentForUpdate.get().setLastName(ConstantsTest.STUDENT_LAST_NAME);
        assertEquals(updateStudentId, studentRepository.update(studentForUpdate.get()));
        Optional<Student> studentAfterUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(lastNameStudentForUpdate, studentAfterUpdate.get().getLastName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndChangeFirstName_ThenShoudUpdateAndReturnEntityId() {
        long updateStudentId = 2L;
        Optional<Student> studentForUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentForUpdate.isPresent());
        String firstNameStudentForUpdate = studentForUpdate.get().getFirstName();
        assertNotEquals(ConstantsTest.STUDENT_FIRST_NAME, studentForUpdate.get().getFirstName());
        studentForUpdate.get().setFirstName(ConstantsTest.STUDENT_FIRST_NAME);
        assertEquals(updateStudentId, studentRepository.update(studentForUpdate.get()));
        Optional<Student> studentAfterUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotEquals(firstNameStudentForUpdate, studentAfterUpdate.get().getFirstName());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndChangeGroupToNull_ThenShoudUpdateAndReturnEntityId() {
        long updateStudentId = 4L;
        Optional<Student> studentForUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentForUpdate.isPresent());
        assertNotNull(studentForUpdate.get().getGroup());
        Group groupStudentForUpdate = studentForUpdate.get().getGroup();
        studentForUpdate.get().setGroup(null);
        assertEquals(updateStudentId, studentRepository.update(studentForUpdate.get()));
        Optional<Student> studentAfterUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNull(studentAfterUpdate.get().getGroup());
        assertNotEquals(groupStudentForUpdate, studentAfterUpdate.get().getGroup());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndChangeGroup_ThenShoudUpdateAndReturnEntityId() {
        long updateStudentId = 4L;
        Optional<Student> studentForUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentForUpdate.isPresent());
        assertNotNull(studentForUpdate.get().getGroup());
        Group groupStudentForUpdate = studentForUpdate.get().getGroup();
        studentForUpdate.get().setGroup(new Group(3L, "FE-22"));
        assertEquals(updateStudentId, studentRepository.update(studentForUpdate.get()));
        Optional<Student> studentAfterUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentAfterUpdate.isPresent());
        assertNotNull(studentAfterUpdate.get().getGroup());
        assertNotEquals(groupStudentForUpdate, studentAfterUpdate.get().getGroup());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndAddCourse_ThenShoudUpdateAndReturnEntityId() {
        long updateStudentId = 3L;
        Optional<Student> studentForUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentForUpdate.isPresent());
        assertFalse(studentForUpdate.get().getCourses().isEmpty());
        List<Course> coursesStudentForUpdate = new ArrayList<>(studentForUpdate.get().getCourses());
        Course newStudentCourse = new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE");
        assertFalse(studentForUpdate.get().getCourses().contains(newStudentCourse));
        studentForUpdate.get().getCourses().add(newStudentCourse);
        assertTrue(studentForUpdate.get().getCourses().contains(newStudentCourse));
        assertEquals(updateStudentId, studentRepository.update(studentForUpdate.get()));
        Optional<Student> studentAfterUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentAfterUpdate.isPresent());
        assertFalse(coursesStudentForUpdate.containsAll(studentAfterUpdate.get().getCourses()));
        assertTrue(studentAfterUpdate.get().getCourses().containsAll(coursesStudentForUpdate));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndAddDuplicateCourse_ThenException() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        long updateStudentId = 3L;

        Optional<Student> studentForUpdate = transactionTemplate.execute(status -> {
            Optional<Student> student = studentRepository.findById(updateStudentId);
            assertTrue(student.isPresent());
            assertFalse(student.get().getCourses().isEmpty());
            return student;
        });

        Course courseForAdd = new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE");
        assertTrue(studentForUpdate.get().getCourses().contains(courseForAdd));
        studentForUpdate.get().getCourses().add(courseForAdd);

        assertThrows(DataAccessException.class, () -> {
            studentRepository.update(studentForUpdate.get());
            testEntityManager.flush();
        }, ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndAddDuplicateCourses_ThenException() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        long updateStudentId = 3L;

        Optional<Student> studentForUpdate = transactionTemplate.execute(status -> {
            Optional<Student> student = studentRepository.findById(updateStudentId);
            assertTrue(student.isPresent());
            assertFalse(student.get().getCourses().isEmpty());
            return student;
        });

        List<Course> newStudentCourse = Arrays.asList(
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"),
                new Course(1L, "ARCHITECTURE", "Description ARCHITECTURE"));
        assertTrue(newStudentCourse.size() > 1);
        assertTrue(studentForUpdate.get().getCourses().addAll(newStudentCourse));

        assertThrows(DataAccessException.class, () -> studentRepository.update(studentForUpdate.get()),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void update_WhenUpdateExistsStudentAndRemoveCourse_ThenException() {
        long updateStudentId = 3L;
        Optional<Student> studentForUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentForUpdate.isPresent());
        assertFalse(studentForUpdate.get().getCourses().isEmpty());
        List<Course> coursesStudentForUpdate = new ArrayList<>(studentForUpdate.get().getCourses());
        studentForUpdate.get().getCourses().remove(0);
        assertFalse(studentForUpdate.get().getCourses().isEmpty());
        Long updatedStudentId = studentRepository.update(studentForUpdate.get());
        testEntityManager.flush();
        assertEquals(updateStudentId, updatedStudentId);
        Optional<Student> studentAfterUpdate = studentRepository.findById(updateStudentId);
        assertTrue(studentAfterUpdate.isPresent());
        assertTrue(coursesStudentForUpdate.containsAll(studentAfterUpdate.get().getCourses()));
        assertFalse(studentAfterUpdate.get().getCourses().containsAll(coursesStudentForUpdate));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void delete_WhenDeleteAndStudentIsNull_ThenException() {
        assertThrows(DataAccessException.class, () -> studentRepository.delete(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void delete_WhenDeleteNotExistsStudent_ThenNothingWillBeDeleted() {
        int studentsCountBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        Student studentForDelete = new Student(ConstantsTest.STUDENT_LAST_NAME, ConstantsTest.STUDENT_FIRST_NAME);
        studentForDelete.setId(15L);
        assertFalse(studentRepository.findById(studentForDelete.getId()).isPresent());
        studentRepository.delete(studentForDelete);
        int studentsCountAfterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountAfterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        assertEquals(studentsCountBeforeDelete, studentsCountAfterDelete);
        assertEquals(coursesCountBeforeDelete, coursesCountAfterDelete);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void delete_WhenDeleteExistsStudent_ThenDeleteAndReturnTrue() {
        long studentIdForDelete = 3L;
        int studentsCountBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        Optional<Student> studentForDelete = studentRepository.findById(studentIdForDelete);
        assertTrue(studentForDelete.isPresent());
        assertFalse(studentForDelete.get().getCourses().isEmpty());
        studentRepository.delete(studentForDelete.get());
        testEntityManager.flush();
        assertFalse(studentRepository.findById(studentIdForDelete).isPresent());
        int studentsCountAfterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENTS);
        int coursesCountAfterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, ConstantsTest.TABLE_STUDENT_COURSE);
        assertNotEquals(studentsCountBeforeDelete, studentsCountAfterDelete);
        assertNotEquals(coursesCountBeforeDelete, coursesCountAfterDelete);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsStudents_ThenReturnZero() {
        assertEquals(0, studentRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void count_WhenExistsStudents_ThenReturnCountStudents() {
        assertEquals(9L, studentRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void getStudentsByCourse_WhenNotExistsStudents_ThenReturnEmptyListStudents() {
        assertTrue(studentRepository.getStudentsByCourse(ConstantsTest.EXISTS_COURSE_NAME).isEmpty());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void getStudentsByCourse_WhenNotExistsStudentsWithSuchCourse_ThenReturnEmptyListStudents() {
        assertTrue(studentRepository.getStudentsByCourse(ConstantsTest.NOT_EXISTS_COURSE_NAME).isEmpty());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void getStudentsByCourse_WhenExistsStudentsWithSuchCourse_ThenReturnListStudents() {
        List<Student> studentsExpected = new ArrayList<>();
        Student student;
        student = new Student("Green", "Victoria");
        student.setId(2L);
        student.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        studentsExpected.add(student);
        student = new Student("Hoig", "Adrian");
        student.setId(3L);
        student.setGroup(new Group(7L, "KK-73"));
        student.getCourses().add(new Course(2L, "COMPUTER SCIENCE", "Description COMPUTER SCIENCE"));
        student.getCourses().add(new Course(9L, "PHILOSOPHY", "Description PHILOSOPHY"));
        studentsExpected.add(student);
        List<Student> studentsReturned = studentRepository.getStudentsByCourse(ConstantsTest.EXISTS_COURSE_NAME);
        assertEquals(studentsExpected.size(), studentsReturned.size());
        assertTrue(studentsExpected.containsAll(studentsReturned));
        assertTrue(studentsReturned.containsAll(studentsExpected));
    }
}
