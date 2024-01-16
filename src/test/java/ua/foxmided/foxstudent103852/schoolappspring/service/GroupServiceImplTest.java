package ua.foxmided.foxstudent103852.schoolappspring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ua.foxmided.foxstudent103852.schoolappspring.config.EntityValidatorConfig;
import ua.foxmided.foxstudent103852.schoolappspring.exception.ConstraintViolationException;
import ua.foxmided.foxstudent103852.schoolappspring.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.repository.GroupRepository;
import ua.foxmided.foxstudent103852.schoolappspring.util.ConstantsTest;

@SpringBootTest(classes = { GroupServiceImpl.class })
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { EntityValidatorConfig.class })
@TestPropertySource(locations = { "classpath:error-message.properties", "classpath:info-message.properties" })
class GroupServiceImplTest {

    @MockBean
    GroupRepository groupRepositoryMock;

    @Autowired
    GroupServiceImpl groupService;

    @Test
    void add_WhenCalledWithNullAsGroupEntity_ThenException() {
        assertThrows(ConstraintViolationException.class, () -> groupService.add(null),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).save(Mockito.any(Group.class));
        Mockito.verifyNoInteractions(groupRepositoryMock);
    }

    @Test
    void add_WhenCalledWithEmptyGroupName_ThenException() {
        Group group1 = new Group(null);
        assertThrows(ConstraintViolationException.class, () -> groupService.add(group1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).save(Mockito.any(Group.class));
        Mockito.verifyNoInteractions(groupRepositoryMock);

        Group group2 = new Group(ConstantsTest.EMPTY_STRING);
        assertThrows(ConstraintViolationException.class, () -> groupService.add(group2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).save(Mockito.any(Group.class));
        Mockito.verifyNoInteractions(groupRepositoryMock);
    }

    @Test
    void add_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(groupRepositoryMock.save(Mockito.any(Group.class))).thenThrow(BadSqlGrammarException.class);
        Group addGroup = new Group(ConstantsTest.GROUP_NAME);
        assertThrows(DataProcessingException.class, () -> groupService.add(addGroup),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void add_WhenCalledWithNotValidName_ThenException() {
        Group group1 = new Group(ConstantsTest.GROUP_NAME_NOT_VALID_1);
        assertThrows(ConstraintViolationException.class, () -> groupService.add(group1),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).save(Mockito.any(Group.class));
        Mockito.verifyNoInteractions(groupRepositoryMock);

        Group group2 = new Group(ConstantsTest.GROUP_NAME_NOT_VALID_2);
        assertThrows(ConstraintViolationException.class, () -> groupService.add(group2),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).save(Mockito.any(Group.class));
        Mockito.verifyNoInteractions(groupRepositoryMock);

        Group group3 = new Group(ConstantsTest.GROUP_NAME_NOT_VALID_3);
        assertThrows(ConstraintViolationException.class, () -> groupService.add(group3),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).save(Mockito.any(Group.class));
        Mockito.verifyNoInteractions(groupRepositoryMock);

        Group group4 = new Group(ConstantsTest.GROUP_NAME_NOT_VALID_4);
        assertThrows(ConstraintViolationException.class, () -> groupService.add(group4),
                ConstantsTest.CONSTRAINT_VIOLATION_EXCEPTION_EXPECTED_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).save(Mockito.any(Group.class));
        Mockito.verifyNoInteractions(groupRepositoryMock);
    }

    @Test
    void add_WhenCalledWithValidName_ThenAddAndReturnAddedEntity() {
        Group resultMockito = new Group(1L, ConstantsTest.GROUP_NAME);
        Mockito.when(groupRepositoryMock.save(Mockito.any(Group.class))).thenReturn(resultMockito);
        Group resultService = groupService.add(new Group(ConstantsTest.GROUP_NAME));
        assertTrue(resultService instanceof Group);
        assertEquals(resultMockito, resultService);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).save(Mockito.any(Group.class));
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndExistsGroups_ShouldReturnListGroups() {
        List<Group> resultMockito = Arrays.asList(
                new Group(1L, "EM-32"),
                new Group(2L, "UJ-33"),
                new Group(3L, "FE-22"));
        Mockito.when(groupRepositoryMock.findAll()).thenReturn(resultMockito);
        List<Group> resultService = groupService.getAll();
        assertEquals(false, resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndNotExistsGroups_ShouldReturnEmptyListGroups() {
        List<Group> resultMockito = Arrays.asList();
        Mockito.when(groupRepositoryMock.findAll()).thenReturn(resultMockito);
        List<Group> resultService = groupService.getAll();
        assertTrue(resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void getAll_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(groupRepositoryMock.findAll()).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> groupService.getAll(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void count_WhenCalledAndExistsGroups_ShouldReturnGroupsCount() {
        long resultMockito = 10L;
        Mockito.when(groupRepositoryMock.count()).thenReturn(resultMockito);
        long resultService = groupService.count();
        assertEquals(resultMockito, resultService);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void count_WhenCalledAndNotExistsGroups_ShouldReturnZero() {
        long resultMockito = ConstantsTest.ZERO_LONG;
        Mockito.when(groupRepositoryMock.count()).thenReturn(resultMockito);
        long resultService = groupService.count();
        assertEquals(resultMockito, resultService);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void count_WhenCalledAndNotExistsGroupsTable_ThenException() {
        Mockito.when(groupRepositoryMock.count()).thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> groupService.count(),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).count();
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void getGroupsWithFewerStudents_WhenCalledWithPositiveParameter_ShouldReturnNotEmptyListGroups() {
        List<Group> resultMockito = new ArrayList<>();
        resultMockito.add(new Group(1L, "EM-32"));
        Mockito.when(groupRepositoryMock.getGroupsWithFewerStudents(Mockito.longThat(argument -> (argument >= 0))))
                .thenReturn(resultMockito);
        List<Group> resultService = groupService.getGroupsWithFewerStudents(5L);
        assertFalse(resultService.isEmpty());
        assertEquals(resultMockito, resultService);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).getGroupsWithFewerStudents(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }

    @Test
    void getGroupsWithFewerStudents_WhenCalledWithNegativeParameter_ThenException() {
        List<Group> resultMockito = Arrays.asList();
        Mockito.when(groupRepositoryMock.getGroupsWithFewerStudents(Mockito.longThat(argument -> (argument < 0))))
                .thenReturn(resultMockito);
        assertThrows(DataProcessingException.class, () -> groupService.getGroupsWithFewerStudents(-5L),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(0)).getGroupsWithFewerStudents(Mockito.anyLong());
        Mockito.verifyNoInteractions(groupRepositoryMock);
    }

    @Test
    void getGroupsWithFewerStudents_WhenCalledAndThrowsDataAccessException_ThenException() {
        Mockito.when(groupRepositoryMock.getGroupsWithFewerStudents(Mockito.anyLong()))
                .thenThrow(BadSqlGrammarException.class);
        assertThrows(DataProcessingException.class, () -> groupService.getGroupsWithFewerStudents(5L),
                ConstantsTest.DATA_PROCESSING_EXCEPTION_MESSAGE);
        Mockito.verify(groupRepositoryMock, Mockito.times(1)).getGroupsWithFewerStudents(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(groupRepositoryMock);
    }
}
