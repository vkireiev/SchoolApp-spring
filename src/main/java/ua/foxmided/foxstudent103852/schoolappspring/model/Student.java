package ua.foxmided.foxstudent103852.schoolappspring.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(columnNames = { "last_name", "first_name" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id", "firstName", "lastName" })
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = Group.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT), nullable = true)
    private Group group;

    @NotNull(message = "{student.field.lastname.notnull}")
    @NotBlank(message = "{student.field.lastname.notblank}")
    @Size(min = 3, max = 35, message = "{student.field.lastname.size}")
    @Column(name = "last_name", nullable = false, length = 35)
    private String lastName;

    @NotNull(message = "{student.field.firstname.notnull}")
    @NotBlank(message = "{student.field.firstname.notblank}")
    @Size(min = 3, max = 35, message = "{student.field.firstname.size}")
    @Column(name = "first_name", nullable = false, length = 35)
    private String firstName;

    @NotNull(message = "{student.field.courses.notnull}")
    @ManyToMany(targetEntity = Course.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "student_id", "course_id" }))
    private @NonNull List<Course> courses = new ArrayList<>();

    public Student(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Student: (ID = " + id + ") ");
        result.append(lastName + " " + firstName + ", group = ");
        if (group != null) {
            result.append(group.getName());
        } else {
            result.append("n/a");
        }
        result.append(", courses: [");
        if ((courses != null) && (!courses.isEmpty())) {
            result.append(courses.stream()
                    .map(course -> course.getName() + " (ID = " + course.getId() + ")")
                    .collect(Collectors.joining(", ")));
        } else {
            result.append("n/a");
        }
        result.append("]");
        return result.toString();
    }

    public boolean addCourse(@NonNull Course course) {
        if (this.courses == null || this.courses.contains(course)) {
            return false;
        }
        return this.courses.add(course);
    }

    public boolean removeCourse(@NonNull Course course) {
        if (this.courses == null || !this.courses.contains(course)) {
            return false;
        }
        return this.courses.remove(course);
    }
}
