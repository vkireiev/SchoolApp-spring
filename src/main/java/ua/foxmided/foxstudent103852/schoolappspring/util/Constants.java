package ua.foxmided.foxstudent103852.schoolappspring.util;

import ua.foxmided.foxstudent103852.schoolappspring.model.Course;
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.model.Student;

public final class Constants {
    public static final String EMPTY_STRING = "";
    public static final long ZERO_LONG = 0L;

    public static final String LOG_PATTERN_MESSAGE_2 = "{}{}";
    public static final String LOG_PATTERN_MESSAGE_3 = "{}{}{}";
    public static final String LOG_PATTERN_MESSAGE_4 = "{}{}{}{}";
    public static final String LOG_PATTERN_MESSAGE_5 = "{}{}{}{}{}";
    public static final String LOG_PATTERN_2_MESSAGE = "{}, {}";
    public static final String LOG_PATTERN_2_EXCEPTION = "{} <- {}";
    public static final String LOG_PATTERN_3_EXCEPTION = "{} <- {} <- {}";

    public static final String COURSE_ENTITY = Course.class.getSimpleName();
    public static final String GROUP_ENTITY = Group.class.getSimpleName();
    public static final String STUDENT_ENTITY = Student.class.getSimpleName();

    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_GROUPS = "groups";
    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_STUDENT_COURSE = "student_course";

    public static final String QUERY_GET_ALL = "FROM %s";
    public static final String QUERY_COUNT = "SELECT COUNT(*) FROM %s";

    public static final String SQL_SELECT_COUNT = "SELECT COUNT(*) FROM %s;";
    public static final String SQL_TRANCATE_TABLE_CASCADE = "TRUNCATE TABLE %s CASCADE;";

    public static final String DBINIT_DROP_TABLES_SQL = "drop_tables.sql";
    public static final String DBINIT_CREATE_TABLES_SQL = "create_tables.sql";
    public static final String DBINIT_CLEAR_TABLES_SQL = "clear_tables.sql";
    public static final String DBINIT_METADATA_TABLE_TYPE = "TABLE";
    public static final String DBINIT_METADATA_COLUMN_TABLE_NAME = "TABLE_NAME";
    public static final long DBINIT_FILLED_VALUE = 0;

    public static final String LAST_NAMES_FILE = "src/main/resources/last_names.txt";
    public static final String FIRST_NAMES_FILE = "src/main/resources/first_names.txt";
    public static final String COURSES_FILE = "src/main/resources/courses.txt";
    public static final String COURSES_DESCRIPTION = "Description";
    public static final String SEP = " ";
    public static final String GROUP_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String GROUP_DIGITS = "0123456789";
    public static final String GROUP_SEP = "-";
    public static final int GROUPS_COUNT = 10;
    public static final int MIN_STUDENTS_IN_GROUP = 10;
    public static final int MAX_STUDENTS_IN_GROUP = 30;
    public static final int COURSES_COUNT = 10;
    public static final int STUDENTS_COUNT = 200;
    public static final int MIN_COURSES_PER_STUDENT = 1;
    public static final int MAX_COURSES_PER_STUDENT = 3;

    public static final String MENU_PRINT_STUDENTS_PATTERN = "Students [%s]";
    public static final String MENU_PRINT_GROUPS_PATTERN = "Groups [%s]";
    public static final String MENU_PRINT_COURSES_PATTERN = "Courses [%s]";
    public static final String MENU_TITLE_MESSAGE = "Main menu: ";
    public static final String MENU_ITEM_1_MESSAGE = "a. Find all groups with less or equal students' number";
    public static final String MENU_ITEM_2_MESSAGE = "b. Find all students related to the course with the given name";
    public static final String MENU_ITEM_3_MESSAGE = "c. Add a new student";
    public static final String MENU_ITEM_4_MESSAGE = "d. Delete a student by the STUDENT_ID";
    public static final String MENU_ITEM_5_MESSAGE = "e. Add a student to the course (from a list)";
    public static final String MENU_ITEM_6_MESSAGE = "f. Remove the student from one of their courses";
    public static final String MENU_EXIT_MESSAGE = "Goodbye!";
    public static final String MENU_EXIT_VALUE = "exit";
    public static final String MENU_ITEM_1_VALUE = "a";
    public static final String MENU_ITEM_2_VALUE = "b";
    public static final String MENU_ITEM_3_VALUE = "c";
    public static final String MENU_ITEM_4_VALUE = "d";
    public static final String MENU_ITEM_5_VALUE = "e";
    public static final String MENU_ITEM_6_VALUE = "f";
    public static final String MENU_SELECTED_ITEM_PREFIX = ">> ";
    public static final String MENU_ITEM_Y_VALUE = "y";
    public static final String MENU_ITEM_YES_VALUE = "yes";
    public static final String MENU_ITEM_N_VALUE = "n";
    public static final String MENU_ITEM_NO_VALUE = "no";

    public static final String ERR_MSG_COURSE_ADD = "Unable to add course to database: %s";
    public static final String ERR_MSG_GROUP_ADD = "Unable to add group to database: %s";
    public static final String ERR_MSG_STUDENT_ADD = "Unable to add student's data to database: %s";
    public static final String ERR_MSG_STUDENT_UPDATE = "Unable to update student's data in database: %s";
    public static final String ERR_MSG_STUDENT_DELETE = "Unable to delete student's data in database: %s";

    private Constants() {
    }
}
