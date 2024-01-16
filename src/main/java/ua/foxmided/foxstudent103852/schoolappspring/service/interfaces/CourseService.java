package ua.foxmided.foxstudent103852.schoolappspring.service.interfaces;

import ua.foxmided.foxstudent103852.schoolappspring.model.Course;

public interface CourseService extends CrudService<Course, Long> {
    long count();
}
