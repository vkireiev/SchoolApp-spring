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
import ua.foxmided.foxstudent103852.schoolappspring.repository.CourseRepository;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.schoolappspring.util.EntityValidator;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${err.msg.service.jdbc.course.add.fail}")
    private String courseAddFailMessage;

    @Value("${err.msg.service.jdbc.course.name.empty}")
    private String courseNameEmptyMessage;

    @Value("${err.msg.service.jdbc.course.description.empty}")
    private String courseDescriptionEmptyMessage;

    @Value("${err.msg.service.jdbc.course.count.fail}")
    private String courseCountFailMessage;

    @Value("${err.msg.service.jdbc.course.getall.fail}")
    private String courseGetAllFailMessage;

    private final CourseRepository courseRepository;
    private final EntityValidator entityValidator;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, EntityValidator entityValidator) {
        this.courseRepository = courseRepository;
        this.entityValidator = entityValidator;
    }

    @Override
    public Course add(Course course) {
        log.debug("Adding new Course-entity: {}", course);
        try {
            entityValidator.validate(course, courseAddFailMessage);
            return courseRepository.save(course);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(courseAddFailMessage, course), e);
        }
    }

    @Override
    public Optional<Course> get(Long courseId) {
        return Optional.empty();
    }

    @Override
    public List<Course> getAll() {
        try {
            return courseRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(courseGetAllFailMessage, e);
        }
    }

    @Override
    public Optional<Course> update(Course course) {
        return Optional.empty();
    }

    @Override
    public Optional<Course> delete(Course course) {
        return Optional.empty();
    }

    @Override
    public long count() {
        try {
            return courseRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(courseCountFailMessage, e);
        }
    }
}
