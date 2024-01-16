package ua.foxmided.foxstudent103852.schoolappspring.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "groups", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id", "name" })
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "{group.field.name.notnull}")
    @NotBlank(message = "{group.field.name.notblank}")
    @Size(min = 5, max = 5, message = "{group.field.name.size}")
    @Pattern(regexp = "^[A-Z]{2}-[0-9]{2}$", message = "{group.field.name.pattern}")
    @Column(name = "name", nullable = false, length = 8)
    private String name;

    @NotNull(message = "{group.field.students.notnull}")
    @OneToMany(targetEntity = Student.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Set<Student> students = new HashSet<>();

    public Group(String name) {
        this.name = name;
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return " * " + name + " (ID = " + id + ")";
    }
}
