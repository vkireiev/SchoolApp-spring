package ua.foxmided.foxstudent103852.schoolappspring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.name = UPPER(?1)")
    List<Student> getStudentsByCourse(String courseName);

    default Long update(Student student) {
        findById(student.getId()).orElseThrow(
                () -> new DataProcessingException(String.format(Constants.ERR_MSG_STUDENT_UPDATE, student)));
        Student updatedStudent = save(student);
        return updatedStudent.getId();
    }
}
