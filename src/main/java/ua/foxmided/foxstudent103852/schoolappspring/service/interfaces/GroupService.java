package ua.foxmided.foxstudent103852.schoolappspring.service.interfaces;

import java.util.List;

import ua.foxmided.foxstudent103852.schoolappspring.model.Group;

public interface GroupService extends CrudService<Group, Long> {
    long count();

    List<Group> getGroupsWithFewerStudents(long minStudentsQuantity);
}
