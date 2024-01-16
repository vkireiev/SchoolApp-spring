package ua.foxmided.foxstudent103852.schoolappspring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxmided.foxstudent103852.schoolappspring.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT gr FROM Group gr LEFT JOIN gr.students st GROUP BY gr.id HAVING COALESCE(COUNT(st.id), 0) <= ?1")
    List<Group> getGroupsWithFewerStudents(long minStudentsQuantity);
}
