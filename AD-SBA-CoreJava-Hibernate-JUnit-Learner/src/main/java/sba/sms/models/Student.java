package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Student is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'student' in the database. A Student object contains fields that represent student
 * login credentials and a join table containing a registered student's email and course(s)
 * data. The Student class can be viewed as the owner of the bi-directional relationship.
 * Implement Lombok annotations to eliminate boilerplate code.
 */
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @Column(length = 50)
    private String email;
    @Column(name="name", length=50)
    private String name;
    @Column(name="password", length=50)
    private String password;

    @ToString.Exclude
    @ManyToMany(cascade={CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER)
    @JoinTable(name="student_courses",
               joinColumns = @JoinColumn(name = "student_email", referencedColumnName = "email"),
                inverseJoinColumns = @JoinColumn(name ="courses_id",referencedColumnName = "id"))
    private Set<Course> courses;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass() ||this.email != ((Student)o).getEmail() ||
                this.name != ((Student)o).getName() || this.password != ((Student)o).getPassword() ||
                this.courses != ((Student)o).getCourses())
            return false;
        else
            return true;
    }
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }



    }



