package ua.foxmided.foxstudent103852.schoolappspring.util;

public final class ConstantsTest {
    public static final String EMPTY_STRING = "";
    public static final String NOT_EMPTY_STRING = "NOT_EMPTY_STRING";
    public static final long ZERO_LONG = 0L;
    public static final int ZERO_INT = 0;
    public static final int NEGATIVE_INT = -1;

    public static final String COURSE_NAME = "CourseName";
    public static final String COURSE_DESCRIPTION = "CourseDescription";
    public static final String COURSE_NAME_NOT_VALID_1 = "1";
    public static final String COURSE_NAME_NOT_VALID_2 = " ";
    public static final String COURSE_NAME_NOT_VALID_3 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
    public static final String COURSE_DESCRIPTION_NOT_VALID_1 = "1";
    public static final String COURSE_DESCRIPTION_NOT_VALID_2 = " ";
    public static final String COURSE_DESCRIPTION_NOT_VALID_3 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"
            + "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
            + "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20";
    public static final String GROUP_NAME = "TS-00";
    public static final String GROUP_NAME_NOT_VALID_1 = "1";
    public static final String GROUP_NAME_NOT_VALID_2 = " ";
    public static final String GROUP_NAME_NOT_VALID_3 = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20";
    public static final String GROUP_NAME_NOT_VALID_4 = "AAA00";
    public static final String STUDENT_LAST_NAME = "LastName";
    public static final String STUDENT_LAST_NAME_NOT_VALID_1 = "1";
    public static final String STUDENT_LAST_NAME_NOT_VALID_2 = " ";
    public static final String STUDENT_LAST_NAME_NOT_VALID_3 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
    public static final String STUDENT_FIRST_NAME = "FirstName";
    public static final String STUDENT_FIRST_NAME_NOT_VALID_1 = "1";
    public static final String STUDENT_FIRST_NAME_NOT_VALID_2 = " ";
    public static final String STUDENT_FIRST_NAME_NOT_VALID_3 = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
    public static final String EXISTS_COURSE_NAME = "COMPUTER SCIENCE";
    public static final String NOT_EXISTS_COURSE_NAME = "TestCourseName";

    public static final String GROUP_NAME_CHECK_PATTERN = "^[A-Z]{2}-[0-9]{2}$";

    public static final String FILE_VALID = "src/test/resources/file_valid.txt";
    public static final String FILE_NOT_EXISTS = "file_not_exists.txt";
    public static final String SQL_FILE_NOT_EXISTS = "file_not_exists.sql";
    public static final String SQL_FILE_WITH_ERRORS = "bad_sql_file.sql";
    public static final String SQL_FILE_VALID = "V1_4__INSERT INTO_courses.sql";

    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_GROUPS = "groups";
    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_STUDENT_COURSE = "student_course";

    public static final String MENU_ITEM_INVALID_VALUE = "invalid";

    public static final String MOCKITO_STRING_STUB_1 = "Mockito String Stub 1";
    public static final String MOCKITO_STRING_STUB_2 = "Mockito String Stub 2";
    public static final String MOCKITO_STRING_STUB_3 = "Mockito String Stub 3";
    public static final String MOCKITO_DATA_PROCESSING_EXCEPTION_MESSAGE = "Mockito(DataProcessingException.class)";

    public static final String DATA_ACCESS_EXCEPTION_MESSAGE = "DataAccessException was expected";
    public static final String DATA_PROCESSING_EXCEPTION_MESSAGE = "DataProcessingException was expected";
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED_MESSAGE = "IllegalArgumentException was expected";
    public static final String VALIDATION_EXCEPTION_EXPECTED_MESSAGE = "ValidationException was expected";
    public static final String NULL_POINTER_EXCEPTION_EXPECTED_MESSAGE = "NullPointerException was expected";
    public static final String CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE = "ConstraintViolationException was expected";

    private ConstantsTest() {
    }
}
