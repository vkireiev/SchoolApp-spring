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
import ua.foxmided.foxstudent103852.schoolappspring.model.Group;
import ua.foxmided.foxstudent103852.schoolappspring.repository.GroupRepository;
import ua.foxmided.foxstudent103852.schoolappspring.service.interfaces.GroupService;
import ua.foxmided.foxstudent103852.schoolappspring.util.EntityValidator;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${err.msg.service.jdbc.group.add.fail}")
    private String groupAddFailMessage;

    @Value("${err.msg.service.jdbc.group.count.fail}")
    private String groupCountFailMessage;

    @Value("${err.msg.service.jdbc.group.getall.fail}")
    private String groupGetAllFailMessage;

    @Value("${err.msg.service.jdbc.group.name.empty}")
    private String groupNameEmptyMessage;

    @Value("${err.msg.service.jdbc.group.students.quantity}")
    private String groupStudentsQuantityFailMessage;

    @Value("${err.msg.service.jdbc.group.students.get.fail}")
    private String groupStudentsGetFailMessage;

    private final GroupRepository groupRepository;
    private final EntityValidator entityValidator;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, EntityValidator entityValidator) {
        this.groupRepository = groupRepository;
        this.entityValidator = entityValidator;
    }

    @Override
    public Group add(Group group) {
        log.debug("Adding new Group-entity: {}", group);
        try {
            entityValidator.validate(group, groupAddFailMessage);
            return groupRepository.save(group);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(groupAddFailMessage, group), e);
        }
    }

    @Override
    public Optional<Group> get(Long groupId) {
        return Optional.empty();
    }

    @Override
    public List<Group> getAll() {
        try {
            return groupRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(groupGetAllFailMessage, e);
        }
    }

    @Override
    public Optional<Group> update(Group group) {
        return Optional.empty();
    }

    @Override
    public Optional<Group> delete(Group group) {
        return Optional.empty();
    }

    @Override
    public long count() {
        try {
            return groupRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(groupCountFailMessage, e);
        }
    }

    @Override
    public List<Group> getGroupsWithFewerStudents(long minStudentsQuantity) {
        if (minStudentsQuantity < 0) {
            throw new DataProcessingException(groupStudentsQuantityFailMessage);
        }
        try {
            return groupRepository.getGroupsWithFewerStudents(minStudentsQuantity);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(groupStudentsGetFailMessage, minStudentsQuantity), e);
        }
    }
}
