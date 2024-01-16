package ua.foxmided.foxstudent103852.schoolappspring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan("ua.foxmided.foxstudent103852.schoolappspring.model")
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddGroupWithNullName_ThenException() {
        Group newGroup = new Group(null);
        assertNull(this.testEntityManager.getId(newGroup));
        assertThrows(ValidationException.class, () -> groupRepository.save(newGroup),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddGroupWithEmptyName_ThenException() {
        Group newGroup = new Group(ConstantsTest.EMPTY_STRING);
        assertNull(this.testEntityManager.getId(newGroup));
        assertThrows(ValidationException.class, () -> groupRepository.save(newGroup),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddGroupWithNotValidName_ThenException() {
        Group newGroup = new Group(ConstantsTest.COURSE_NAME);
        assertNull(this.testEntityManager.getId(newGroup));
        assertThrows(ValidationException.class, () -> groupRepository.save(newGroup),
                ConstantsTest.VALIDATION_EXCEPTION_EXPECTED_MESSAGE);
        assertNull(this.testEntityManager.getId(newGroup));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddGroupWithValidName_ThenShoudAddAndReturnAddedEntity() {
        Group newGroup = new Group(ConstantsTest.GROUP_NAME);
        assertNull(this.testEntityManager.getId(newGroup));
        assertTrue(groupRepository.save(newGroup) instanceof Group);
        assertNotNull(this.testEntityManager.getId(newGroup));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void save_WhenAddGroupWithDuplicateName_ThenException() {
        Group newGroup = new Group(ConstantsTest.GROUP_NAME);
        Group duplicateGroup = new Group(ConstantsTest.GROUP_NAME);
        assertNull(this.testEntityManager.getId(newGroup));
        assertNull(this.testEntityManager.getId(duplicateGroup));
        groupRepository.save(newGroup);
        this.testEntityManager.flush();
        assertNotNull(this.testEntityManager.getId(newGroup));
        assertThrows(DataAccessException.class, () -> groupRepository.save(duplicateGroup),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
        assertNull(this.testEntityManager.getId(duplicateGroup));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findById_WhenGroupIdIsNull_ThenException() {
        assertThrows(DataAccessException.class, () -> groupRepository.findById(null),
                ConstantsTest.DATA_ACCESS_EXCEPTION_MESSAGE);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void findById_WhenNotExistsGroup_ThenReturnOptionalNull() {
        assertFalse(groupRepository.findById(0L).isPresent());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void findById_WhenExistsGroup_ThenReturnOptionalGroup() {
        Optional<Group> groupExpected = Optional.of(new Group(1L, "EM-32"));
        Optional<Group> groupReturned = groupRepository.findById(1L);
        assertTrue(groupReturned.isPresent());
        assertEquals(groupExpected, groupReturned);
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void getAll_WhenNotExistsGroups_ThenReturnEmptyListGroups() {
        assertThat(groupRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void getAll_WhenExistsGroups_ThenReturnListGroups() {
        List<Group> groupsExpected = Arrays.asList(
                new Group(1L, "EM-32"),
                new Group(2L, "UJ-33"),
                new Group(3L, "FE-22"),
                new Group(4L, "NU-88"),
                new Group(5L, "CI-37"),
                new Group(6L, "AR-49"),
                new Group(7L, "KK-73"),
                new Group(8L, "QR-95"),
                new Group(9L, "RI-47"),
                new Group(10L, "IC-83"));
        List<Group> groupsReturned = (List<Group>) groupRepository.findAll();
        assertThat(groupsReturned).isNotEmpty();
        assertEquals(groupsExpected.size(), groupsReturned.size());
        assertTrue(groupsExpected.containsAll(groupsReturned));
        assertTrue(groupsReturned.containsAll(groupsExpected));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void count_WhenNotExistsGroups_ThenReturnZero() {
        assertEquals(0, groupRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void count_WhenExistsGroups_ThenReturnCountGroups() {
        assertEquals(10L, groupRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    void getGroupsWithFewerStudents_WhenNotExistsGroups_ThenReturnEmptyListGroups() {
        assertTrue(groupRepository.getGroupsWithFewerStudents(2L).isEmpty());
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    void getGroupsWithFewerStudents_WhenNotExistsStudents_ThenReturnListWithAllExistsGroups() {
        List<Group> groupsExpected = Arrays.asList(
                new Group(1L, "EM-32"),
                new Group(2L, "UJ-33"),
                new Group(3L, "FE-22"),
                new Group(4L, "NU-88"),
                new Group(5L, "CI-37"),
                new Group(6L, "AR-49"),
                new Group(7L, "KK-73"),
                new Group(8L, "QR-95"),
                new Group(9L, "RI-47"),
                new Group(10L, "IC-83"));
        List<Group> groupsReturned = groupRepository.getGroupsWithFewerStudents(2L);
        assertEquals(groupsExpected.size(), groupsReturned.size());
        assertTrue(groupsExpected.containsAll(groupsReturned));
        assertTrue(groupsReturned.containsAll(groupsExpected));
    }

    @Test
    @Sql(scripts = { "classpath:clear_tables.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_groups.sql" })
    @Sql(scripts = { "classpath:insert_testdata_into_students.sql" })
    void getGroupsWithFewerStudents_WhenExistsGroups_ThenReturnListGroups() {
        List<Group> groupsExpected = Arrays.asList(
                // new Group(1L, "EM-32"), // this group has 3 students
                new Group(2L, "UJ-33"),
                new Group(3L, "FE-22"),
                new Group(4L, "NU-88"),
                new Group(5L, "CI-37"),
                new Group(6L, "AR-49"),
                new Group(7L, "KK-73"),
                new Group(8L, "QR-95"),
                new Group(9L, "RI-47"),
                new Group(10L, "IC-83"));
        List<Group> groupsReturned = groupRepository.getGroupsWithFewerStudents(2L);
        assertEquals(groupsExpected.size(), groupsReturned.size());
        assertTrue(groupsExpected.containsAll(groupsReturned));
        assertTrue(groupsReturned.containsAll(groupsExpected));
    }
}
