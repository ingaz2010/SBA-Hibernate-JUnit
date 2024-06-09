package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


class StudentServiceTest {

    StudentService sc = new StudentService();
    @ParameterizedTest
    @CsvSource({
            "anna@gmail.com, password",
            "henry@gmail.com, 12345"
    })
    void testCreateStudent(String email, String password) {
        Student s = new Student();
        s.setEmail(email);
        s.setPassword(password);
        sc.createStudent(s);
        Assertions.assertThat(s).isNotNull();
    }

    @Test
    void testGetStudentByEmail(){
        Student s = new Student("jane.com", "Jane Jones", "password");
        sc.createStudent(s);
        Assertions.assertThat(s.equals(sc.getStudentByEmail("megan@gmail.com")));
    }

    @Test
    void testValidateStudent(){
        Student s = new Student();
        String email = "megan@gmail.com";
        String passwordCorrect = "password";
        String passwordIncorrect = "12345";
        s.setEmail(email);
        s.setPassword(passwordCorrect);
        sc.createStudent(s);

        assertTrue(sc.validateStudent(email, passwordCorrect));
        assertFalse(sc.validateStudent(email, passwordIncorrect));

    }

    @Test
    void testRegisterStudentToCourse(){
        Student s = new Student("mary@gmail.com", "Mary Jones", "password");
        sc.createStudent(s);
        String email = s.getEmail();

        Course c = new Course();
        c.setName("Java");
        c.setInstructor("R. Williams");
        CourseService cs = new CourseService();
        cs.createCourse(c);

        sc.registerStudentToCourse(email, 1);

        Student s2 = sc.getStudentByEmail(email);
        assertThat(s2.getCourses().contains(cs.getCourseById(1)));
    }

    @Test
    void testGetStudentCourses(){
        Student s = new Student("john@gmail.com", "John Smith", "password");
        sc.createStudent(s);
        String email = s.getEmail();

        Course c = new Course();
        c.setName("Java");
        c.setInstructor("R. Williams");
        CourseService cs = new CourseService();
        cs.createCourse(c);

        Course c1 = new Course("SQL", "J. Doe");
        cs.createCourse(c1);

        sc.registerStudentToCourse(email, 1);
        sc.registerStudentToCourse(email, 2);

        Student s2 = sc.getStudentByEmail("john@gmail.com");
        List<Course> courses2 = new ArrayList<>();
        courses2.add(cs.getCourseById(1));
        courses2.add(cs.getCourseById(2));
        assertTrue(s2.getCourses().size()==2);
        assertThat(s2.getCourses().contains(cs.getCourseById(1)));
        assertThat(s2.getCourses().contains(cs.getCourseById(2)));
        assertThat(s2.getCourses().equals(courses2));
    }


}