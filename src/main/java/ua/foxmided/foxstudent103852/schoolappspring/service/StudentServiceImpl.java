package ua.foxmided.foxstudent103852.schoolappspring.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;
import ua.foxmided.foxstudent103852.schoolappspring.repository.StudentRepository;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.schoolappspring.util.EntityValidator;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${err.msg.service.jdbc.student.add.fail}")
    private String studentAddFailMessage;

    @Value("${err.msg.service.jdbc.student.get.fail}")
    private String studentGetFailMessage;

    @Value("${err.msg.service.jdbc.student.count.fail}")
    private String studentCountFailMessage;

    @Value("${err.msg.service.jdbc.student.getall.fail}")
    private String studentGetAllFailMessage;

    @Value("${err.msg.service.jdbc.student.update.fail}")
    private String studentUpdateFailMessage;

    @Value("${err.msg.service.jdbc.student.delete.fail}")
    private String studentDeleteFailMessage;

    @Value("${err.msg.service.jdbc.student.delete.id.fail}")
    private String studentDeleteByIdFailMessage;

    @Value("${err.msg.service.jdbc.student.lastname.empty}")
    private String studentLastNameEmptyMessage;

    @Value("${err.msg.service.jdbc.student.firstname.empty}")
    private String studentFirstNameEmptyMessage;

    @Value("${err.msg.service.jdbc.student.courses.notinit}")
    private String studentCoursesNotInitMessage;

    @Value("${err.msg.service.jdbc.student.courses.edit.fail}")
    private String studentCoursesEditFailMessage;

    @Value("${err.msg.service.jdbc.student.course.assign.fail}")
    private String studentCourseAssignFailMessage;

    @Value("${err.msg.service.jdbc.student.course.id.assign.fail}")
    private String studentCourseAssignByIdFailMessage;

    @Value("${err.msg.service.jdbc.student.course.remove.fail}")
    private String studentCourseRemoveFailMessage;

    @Value("${err.msg.service.jdbc.student.course.id.remove.fail}")
    private String studentCourseRemoveByIdFailMessage;

    @Value("${err.msg.service.jdbc.student.get.course.name.empty}")
    private String studentCourseNameEmptyMessage;

    @Value("${err.msg.service.jdbc.student.get.course.fail}")
    private String studentGetByCourseFailMessage;

    private final StudentRepository studentRepository;
    private final EntityValidator entityValidator;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, EntityValidator entityValidator) {
        this.studentRepository = studentRepository;
        this.entityValidator = entityValidator;
    }

    @Override
    public Student add(Student student) {
        log.debug("Adding new Student-entity: {}", student);
        try {
            entityValidator.validate(student, studentAddFailMessage);
            return studentRepository.save(student);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(studentAddFailMessage, student), e);
        }
    }

    public Optional<Student> get(Long studentId) {
        try {
            entityValidator.nullCheck(studentId, studentGetFailMessage);
            return studentRepository.findById(studentId);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(studentGetFailMessage, studentId), e);
        }
    }

    @Override
    public List<Student> getAll() {
        try {
            return studentRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(studentGetAllFailMessage, e);
        }
    }

    public Optional<Student> update(Student student) {
        log.debug("Updating Student-entity: {}", student);
        try {
            entityValidator.validate(student, studentUpdateFailMessage);
            if (get(student.getId()).isPresent()) {
                studentRepository.save(student);
                return get(student.getId());
            }
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(studentUpdateFailMessage, student), e);
        }
    }

    @Override
    public Optional<Student> delete(Student student) {
        log.debug("Deleting Student-entity: {}", student);
        entityValidator.nullCheck(student, studentDeleteFailMessage);
        return delete(student.getId());
    }

    @Override
    public long count() {
        try {
            return studentRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(studentCountFailMessage, e);
        }
    }

    @Override
    public Optional<Student> delete(Long studentId) {
        log.debug("Deleting Student-entity by ID with {}", studentId);
        try {
            Optional<Student> student = get(studentId);
            if (student.isPresent()) {
                studentRepository.delete(student.get());
                return student;
            }
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(studentDeleteByIdFailMessage, studentId), e);
        }
    }

    @Override
    public List<Student> getStudentsByCourse(String courseName) {
        try {
            entityValidator.nullCheck(courseName, studentCourseNameEmptyMessage);
            return studentRepository.getStudentsByCourse(courseName);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(studentGetByCourseFailMessage, courseName), e);
        }
    }

    @Override
    public boolean addCourseForStudent(Student student, Course course) {
        log.debug("Adding Course for Student-entity: {}, {}", student, course);
        try {
            entityValidator.validate(student, studentUpdateFailMessage);
            entityValidator.nullCheck(course, studentCourseAssignFailMessage);
            entityValidator.nullCheck(course.getId(), studentCourseAssignByIdFailMessage);
            Optional<Student> updateStudent = get(student.getId());
            if (updateStudent.isPresent() && updateStudent.get().addCourse(course)) {
                update(updateStudent.get());
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            throw new DataProcessingException(studentCoursesEditFailMessage, e);
        }
    }

    @Override
    public boolean removeCourseForStudent(Student student, Course course) {
        log.debug("Removing Course for Student-entity: {}, {}", student, course);
        try {
            entityValidator.validate(student, studentUpdateFailMessage);
            entityValidator.nullCheck(course, studentCourseRemoveFailMessage);
            entityValidator.nullCheck(course.getId(), studentCourseRemoveByIdFailMessage);
            Optional<Student> updateStudent = get(student.getId());
            if (updateStudent.isPresent() && updateStudent.get().removeCourse(course)) {
                update(updateStudent.get());
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            throw new DataProcessingException(studentCoursesEditFailMessage, e);
        }
    }
}
