package ua.foxmided.foxstudent103852.schoolappspring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id", "name" })
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "{course.field.name.notnull}")
    @NotBlank(message = "{course.field.name.notblank}")
    @Size(min = 3, max = 50, message = "{course.field.name.size}")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotNull(message = "{course.field.description.notnull}")
    @NotBlank(message = "{course.field.description.notblank}")
    @Size(min = 3, max = 255, message = "{course.field.description.size}")
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return " * " + name + ", " + description + " (ID = " + id + ")";
    }
}
