package ua.foxmided.foxstudent103852.schoolappspring.service.interfaces;

import java.util.List;
import java.util.Optional;

import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;

public interface StudentService extends CrudService<Student, Long> {
    long count();

    Optional<Student> delete(Long studentId);

    List<Student> getStudentsByCourse(String courseName);

    boolean addCourseForStudent(Student student, Course course);

    boolean removeCourseForStudent(Student student, Course course);
}
