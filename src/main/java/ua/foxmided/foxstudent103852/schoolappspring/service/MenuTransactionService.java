package ua.foxmided.foxstudent103852.schoolappspring.service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.CourseService;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.StudentService;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleReader;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConsoleWriter;
import ua.foxmided.foxstudent103852.schoolappspring.util.Constants;

@Service
@Transactional
public class MenuTransactionService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${inf.msg.menu.console.get.item.correct.response.message}")
    private String menuGetCorrectResponseItemMessage;

    @Value("${err.msg.menu.console.response.integer.fail}")
    private String menuResponseIntegerFailMessage;

    @Value("${inf.msg.menu.console.get.item1.response.message}")
    private String menuItem1GetResponseMessage;

    @Value("${inf.msg.menu.console.get.item2.response.message}")
    private String menuItem2GetResponseMessage;

    @Value("${inf.msg.menu.console.get.studentid.response.message}")
    private String menuGetStudentIdResponceMessage;

    @Value("${err.msg.menu.console.get.studentid.fail}")
    private String menuGetStudentIdFailMessage;

    @Value("${inf.msg.menu.console.get.lastname.response.message}")
    private String menuGetLastNameResponseMessage;

    @Value("${inf.msg.menu.console.get.firstname.response.message}")
    private String menuGetFirstNameResponseMessage;

    @Value("${inf.msg.menu.console.get.yesno.group.response.message}")
    private String menuGetYesNoGroupResponseMessage;

    @Value("${inf.msg.menu.console.get.groupid.response.message}")
    private String menuGetGroupIdResponseMessage;

    @Value("${err.msg.menu.console.get.groupbyid.fail}")
    private String menuGetGroupByIdFailMessage;

    @Value("${inf.msg.menu.console.item3.done}")
    private String menuItem3DoneMessage;

    @Value("${err.msg.menu.console.item3.fail}")
    private String menuItem3FailMessage;

    @Value("${inf.msg.menu.console.item4.done}")
    private String menuItem4DoneMessage;

    @Value("${err.msg.menu.console.item4.fail}")
    private String menuItem4FailMessage;

    @Value("${inf.msg.menu.console.item5.get.courseid.response.message}")
    private String menuItem5GetCourseIdResponseMessage;

    @Value("${inf.msg.menu.console.item5.done}")
    private String menuItem5DoneMessage;

    @Value("${err.msg.menu.console.item5.fail}")
    private String menuItem5FailMessage;

    @Value("${inf.msg.menu.console.item6.get.courseid.response.message}")
    private String menuItem6GetCourseIdResponseMessage;

    @Value("${inf.msg.menu.console.item6.done}")
    private String menuItem6DoneMessage;

    @Value("${err.msg.menu.console.item6.fail}")
    private String menuItem6FailMessage;

    private final CourseService courseService;
    private final GroupService groupService;
    private final StudentService studentService;
    private final ConsoleReader consoleReader;
    private final ConsoleWriter consoleWriter;

    @Autowired
    public MenuTransactionService(CourseService courseService, GroupService groupService, StudentService studentService,
            ConsoleReader consoleReader, ConsoleWriter consoleWriter) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentService = studentService;
        this.consoleReader = consoleReader;
        this.consoleWriter = consoleWriter;
    }

    public void printGroupsWithFewerStudents() throws IOException {
        printSelectedItemMenu(Constants.MENU_ITEM_1_MESSAGE);
        long minStudentsQuantity = consoleReader.readNumber(menuItem1GetResponseMessage,
                menuResponseIntegerFailMessage);
        log.info(Constants.LOG_PATTERN_MESSAGE_2, "Printing results for minStudentsQuantity=", minStudentsQuantity);
        printGroups(groupService.getGroupsWithFewerStudents(minStudentsQuantity));
    }

    public void printStudentsWithCourse() throws IOException {
        printSelectedItemMenu(Constants.MENU_ITEM_2_MESSAGE);
        printCourses(courseService.getAll());
        String courseName = consoleReader.readString(menuItem2GetResponseMessage);
        log.info(Constants.LOG_PATTERN_MESSAGE_3, "Printing results for courseName='", courseName, "'");
        printStudents(studentService.getStudentsByCourse(courseName));
    }

    public void addNewStudent() throws IOException {
        printSelectedItemMenu(Constants.MENU_ITEM_3_MESSAGE);
        String lastName = consoleReader.readString(menuGetLastNameResponseMessage);
        String firstName = consoleReader.readString(menuGetFirstNameResponseMessage);
        Student newStudent = new Student(lastName, firstName);
        if (consoleReader.readYesNo(menuGetYesNoGroupResponseMessage)) {
            List<Group> groups = groupService.getAll();
            Set<Long> groupsId = groups.stream().map(Group::getId).collect(Collectors.toSet());
            printGroups(groups);
            long groupId = consoleReader.readNumberFromSet(menuGetGroupIdResponseMessage, groupsId,
                    menuResponseIntegerFailMessage, menuGetCorrectResponseItemMessage);
            Optional<Group> selectedGroup = getById(groups, t -> t.getId() == groupId);
            if (selectedGroup.isPresent()) {
                newStudent.setGroup(selectedGroup.get());
            } else {
                log.info(Constants.LOG_PATTERN_MESSAGE_3, "Group(ID=", groupId, ") not exists");
                consoleWriter.printMessage(String.format(menuGetGroupByIdFailMessage, groupId));
            }
        }
        Student addedStudent = studentService.add(newStudent);
        if (Objects.nonNull(addedStudent.getId())) {
            log.info(Constants.LOG_PATTERN_MESSAGE_3, "Added student (ID=", addedStudent.getId(), ")");
            consoleWriter.printMessage(menuItem3DoneMessage);
            consoleWriter.printMessage(addedStudent.toString());
        } else {
            log.info(Constants.LOG_PATTERN_MESSAGE_3, "Failed to add student (", newStudent.toString(), ")");
            consoleWriter.printMessage(menuItem3FailMessage);
        }
    }

    public void deleteStudentById() throws IOException {
        printSelectedItemMenu(Constants.MENU_ITEM_4_MESSAGE);
        long studentId = consoleReader.readNumber(menuGetStudentIdResponceMessage, menuResponseIntegerFailMessage);
        Optional<Student> student = studentService.get(studentId);
        if (student.isPresent()) {
            consoleWriter.printMessage(student.get().toString());
            Optional<Student> deletedStudent = studentService.delete(studentId);
            if (deletedStudent.isPresent()) {
                log.info(Constants.LOG_PATTERN_MESSAGE_3, "Deleted student (ID=", studentId, ")");
                consoleWriter.printMessage(String.format(menuItem4DoneMessage, studentId));
            } else {
                log.info(Constants.LOG_PATTERN_MESSAGE_3, "Failed to delete student (ID=", studentId, ")");
                consoleWriter.printMessage(String.format(menuItem4FailMessage, studentId));
            }
        } else {
            log.info(Constants.LOG_PATTERN_MESSAGE_3, "Student(ID=", studentId, ") not exists");
            consoleWriter.printMessage(String.format(menuGetStudentIdFailMessage, studentId));
        }
    }

    public void addCourseForStudent() throws IOException {
        printSelectedItemMenu(Constants.MENU_ITEM_5_MESSAGE);
        long studentId = consoleReader.readNumber(menuGetStudentIdResponceMessage, menuResponseIntegerFailMessage);
        Optional<Student> student = studentService.get(studentId);
        if (student.isPresent()) {
            consoleWriter.printMessage(student.get().toString());
            List<Course> courses = courseService.getAll();
            courses.removeAll(student.get().getCourses());
            printCourses(courses);
            Set<Long> coursesId = courses.stream().map(Course::getId).collect(Collectors.toSet());
            long courseId = consoleReader.readNumberFromSet(menuItem5GetCourseIdResponseMessage, coursesId,
                    menuResponseIntegerFailMessage, menuGetCorrectResponseItemMessage);
            Optional<Course> addCourse = getById(courses, course -> course.getId() == courseId);
            if (addCourse.isPresent() && studentService.addCourseForStudent(student.get(), addCourse.get())) {
                log.info(Constants.LOG_PATTERN_MESSAGE_5,
                        "Assigned course(ID=", courseId, ") for student(ID=", studentId, ")");
                consoleWriter.printMessage(menuItem5DoneMessage);
            } else {
                log.info(Constants.LOG_PATTERN_MESSAGE_5,
                        "Failed to assign course(ID=", courseId, ") for student(ID=", studentId, ")");
                consoleWriter.printMessage(menuItem5FailMessage);
            }
            consoleWriter.printMessage(studentService.get(studentId).get().toString());
        } else {
            log.info(Constants.LOG_PATTERN_MESSAGE_3, "Student(ID=", studentId, ") not exists");
            consoleWriter.printMessage(String.format(menuGetStudentIdFailMessage, studentId));
        }
    }

    public void removeCourseForStudent() throws IOException {
        printSelectedItemMenu(Constants.MENU_ITEM_6_MESSAGE);
        long studentId = consoleReader.readNumber(menuGetStudentIdResponceMessage, menuResponseIntegerFailMessage);
        Optional<Student> student = studentService.get(studentId);
        if (student.isPresent()) {
            consoleWriter.printMessage(student.get().toString());
            List<Course> courses = student.get().getCourses();
            printCourses(courses);
            Set<Long> coursesId = courses.stream().map(Course::getId).collect(Collectors.toSet());
            long courseId = consoleReader.readNumberFromSet(menuItem6GetCourseIdResponseMessage, coursesId,
                    menuResponseIntegerFailMessage, menuGetCorrectResponseItemMessage);
            Optional<Course> removeCourse = getById(courses, course -> course.getId() == courseId);
            if (removeCourse.isPresent() && studentService.removeCourseForStudent(student.get(), removeCourse.get())) {
                log.info(Constants.LOG_PATTERN_MESSAGE_5,
                        "Removed course(ID=", courseId, ") for student(ID=", studentId, ")");
                consoleWriter.printMessage(menuItem6DoneMessage);
            } else {
                log.info(Constants.LOG_PATTERN_MESSAGE_5,
                        "Faild to remove course(ID=", courseId, ") for student(ID=", studentId, ")");
                consoleWriter.printMessage(menuItem6FailMessage);
            }
            consoleWriter.printMessage(studentService.get(studentId).get().toString());
        } else {
            log.info(Constants.LOG_PATTERN_MESSAGE_3, "Student(ID=", studentId, ") not exists");
            consoleWriter.printMessage(String.format(menuGetStudentIdFailMessage, studentId));
        }
    }

    private void printSelectedItemMenu(String menuItemMessage) {
        consoleWriter.printMessage(Constants.MENU_SELECTED_ITEM_PREFIX + menuItemMessage);
    }

    private void printGroups(List<Group> groups) {
        consoleWriter.printMessage(String.format(Constants.MENU_PRINT_GROUPS_PATTERN, groups.size()));
        groups.forEach(group -> consoleWriter.printMessage(group.toString()));
    }

    private void printCourses(List<Course> courses) {
        consoleWriter.printMessage(String.format(Constants.MENU_PRINT_COURSES_PATTERN, courses.size()));
        courses.forEach(course -> consoleWriter.printMessage(course.toString()));
    }

    private void printStudents(List<Student> students) {
        consoleWriter.printMessage(String.format(Constants.MENU_PRINT_STUDENTS_PATTERN, students.size()));
        students.forEach(student -> consoleWriter.printMessage(student.toString()));
    }

    private <T> Optional<T> getById(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .filter(predicate)
                .findAny();
    }
}
