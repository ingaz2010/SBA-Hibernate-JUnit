package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Course is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'course' in the database. A Course object contains fields that represent course
 * information and a mapping of 'courses' that indicate an inverse or referencing side
 * of the relationship. Implement Lombok annotations to eliminate boilerplate code.
 */
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String instructor;

    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER, mappedBy = "courses")
    private Set<Student> students;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass() || this.id !=((Course) o).getId()
                || this.name != ((Course) o).getName()
                || this.instructor != ((Course) o).getInstructor()
                || this.students != ((Course) o).getStudents())
            return false;
        else
            return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
