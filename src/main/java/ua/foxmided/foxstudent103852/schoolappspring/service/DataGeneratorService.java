package ua.foxmided.foxstudent103852.schoolappspring.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;

@Service
public class DataGeneratorService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final CourseService courseService;
    private final GroupService groupService;
    private final StudentService studentService;
    private final FileService fileService;

    @Autowired
    public DataGeneratorService(CourseService courseService, GroupService groupService,
            StudentService studentService, FileService fileService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentService = studentService;
        this.fileService = fileService;
    }

    public boolean fillDatabase() {
        log.info("Need to fill the database with random data");
        try {
            Set<Group> groups = getRandomGroups(Constants.GROUPS_COUNT,
                    Constants.GROUP_CHARACTERS,
                    Constants.GROUP_DIGITS);
            groups.forEach(groupService::add);
            List<Course> courses = getRandomCourses(Constants.COURSES_COUNT, Constants.COURSES_FILE);
            courses.forEach(courseService::add);
            Set<Student> students = getRandomStudents(Constants.STUDENTS_COUNT, Constants.LAST_NAMES_FILE,
                    Constants.FIRST_NAMES_FILE, courses, groups);
            students.forEach(studentService::add);
            log.info("The database was successfully filled with random data");
        } catch (DataProcessingException e) {
            log.error(Constants.LOG_PATTERN_2_EXCEPTION,
                    "Failed to fill the database with random data", e.getMessage(), e);
            return false;
        }
        return true;
    }

    public Set<Group> getRandomGroups(int groupsCount, String characters, String digits) {
        Set<Group> result = new HashSet<>();
        if ((groupsCount < 0) || isEmpty(characters) || isEmpty(digits)) {
            return result;
        }
        while (result.size() < groupsCount) {
            String group = Constants.EMPTY_STRING;
            group += characters.charAt(random.nextInt(characters.length()));
            group += characters.charAt(random.nextInt(characters.length()));
            group += Constants.GROUP_SEP;
            group += digits.charAt(random.nextInt(digits.length()));
            group += digits.charAt(random.nextInt(digits.length()));
            result.add(new Group(group));
        }
        return result;
    }

    public List<Course> getRandomCourses(int coursesCount, String coursesFile) {
        List<Course> result = new ArrayList<>();
        if ((coursesCount < 0) || isEmpty(coursesFile)) {
            return result;
        }
        List<String> courses = fileService.readFile(coursesFile);
        Collections.shuffle(courses);
        return courses.stream()
                .limit(coursesCount)
                .map(course -> new Course(course, Constants.COURSES_DESCRIPTION + Constants.SEP + course))
                .collect(Collectors.toList());
    }

    public Set<Student> getRandomStudents(int studentsCount, String lastNamesFile, String firstNamesFile,
            List<Course> courses, Set<Group> groups) {
        Set<Student> result = new HashSet<>();
        if ((studentsCount < 0) || isEmpty(lastNamesFile) || isEmpty(firstNamesFile)
                || isEmpty(courses) || isEmpty(groups)) {
            return result;
        }
        List<String> lastNames = fileService.readFile(lastNamesFile);
        List<String> firstNames = fileService.readFile(firstNamesFile);
        int possibleStudentsCount = lastNames.stream().collect(Collectors.toSet()).size();
        possibleStudentsCount = possibleStudentsCount * firstNames.stream().collect(Collectors.toSet()).size();
        possibleStudentsCount = Math.min(possibleStudentsCount, studentsCount);
        while (result.size() < possibleStudentsCount) {
            result.add(new Student(lastNames.get(random.nextInt(lastNames.size())),
                    firstNames.get(random.nextInt(firstNames.size()))));
        }
        result = assignCoursesForStudents(result, courses);
        result = assignGroupsForStudents(result, groups);
        return result;
    }

    private Set<Student> assignCoursesForStudents(Set<Student> students, List<Course> courses) {
        students.forEach(student -> {
            Collections.shuffle(courses);
            student.setCourses(courses.stream()
                    .limit(random.nextInt(Constants.MIN_COURSES_PER_STUDENT, (Constants.MAX_COURSES_PER_STUDENT + 1)))
                    .collect(Collectors.toList()));
        });
        return students;
    }

    private Set<Student> assignGroupsForStudents(Set<Student> students, Set<Group> groups) {
        List<Student> studentsCopy = new ArrayList<>(students);
        Collections.shuffle(studentsCopy);
        for (Group group : groups) {
            int studentsInGroup = random.nextInt((Constants.MIN_STUDENTS_IN_GROUP - 1),
                    (Constants.MAX_STUDENTS_IN_GROUP + 1));
            if (studentsInGroup >= Constants.MIN_STUDENTS_IN_GROUP && studentsCopy.size() >= studentsInGroup) {
                for (int i = 0; i < studentsInGroup; i++) {
                    Student student = studentsCopy.remove(0);
                    student.setGroup(group);
                }
            }
        }
        return students;
    }

    private boolean isEmpty(String object) {
        return ((Objects.isNull(object)) || object.isEmpty());
    }

    private boolean isEmpty(Collection<?> object) {
        return ((Objects.isNull(object)) || object.isEmpty());
    }
}
