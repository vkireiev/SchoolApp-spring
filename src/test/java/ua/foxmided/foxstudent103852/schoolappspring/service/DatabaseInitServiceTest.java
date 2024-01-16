package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { DatabaseInitService.class })
@EntityScan("ua.foxmided.foxstudent103852.schoolappspring.model")
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.generate-ddl=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class DatabaseInitServiceTest {

    @Autowired
    private DatabaseInitService databaseInitService;

    @Test
    @Sql(scripts = { "classpath:drop_tables.sql" })
    void isDatabaseFilled_WhenCalledAndTablesNotExists_ThenReturnFalse() {
        assertFalse(databaseInitService.isDatabaseFilled());
    }

    @Test
    @Sql(scripts = { "classpath:insert_except_courses.sql" })
    void isDatabaseFilled_WhenCalledAndCoursesTableIsEmpty_ThenReturnFalse() {
        assertFalse(databaseInitService.isDatabaseFilled());
    }

    @Test
    @Sql(scripts = { "classpath:insert_except_groups.sql" })
    void isDatabaseFilled_WhenCalledAndGroupsTableIsEmpty_ThenReturnFalse() {
        assertFalse(databaseInitService.isDatabaseFilled());
    }

    @Test
    @Sql(scripts = { "classpath:insert_except_students.sql" })
    void isDatabaseFilled_WhenCalledAndStudentsTableIsEmpty_ThenReturnFalse() {
        assertFalse(databaseInitService.isDatabaseFilled());
    }

    @Test
    @Sql(scripts = { "classpath:insert_testdata_into_courses.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_student_course.sql" })
    void isDatabaseFilled_WhenCalledAndTablesNotEmpty_ThenReturnTrue() {
        assertTrue(databaseInitService.isDatabaseFilled());
    }

    @Test
    @Sql(scripts = { "classpath:drop_tables.sql" })
    void clearDatabase_WhenCalledAndTablesNotExists_ThenReturnFalse() {
        assertFalse(databaseInitService.clearDatabase());
    }

    @Test
    void clearDatabase_WhenCalledAndTablesExists_ThenReturnTrue() {
        assertTrue(databaseInitService.clearDatabase());
    }
}
