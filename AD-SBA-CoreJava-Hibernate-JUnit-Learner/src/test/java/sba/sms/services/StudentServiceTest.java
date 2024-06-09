package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    //creates Student and checks if it has been created and not null
    @ParameterizedTest
    @CsvSource({
            "anna@gmail.com, AnnaSmith, password",
            "henry@gmail.com, Henry Jones, 12345"
    })
    void testCreateStudent(String email, String name, String password) {
        Student s = new Student();
        s.setEmail(email);
        s.setPassword(password);
        s.setName(name);
        sc.createStudent(s);
        Assertions.assertThat(s).isNotNull();
    }

    //creates a student and extracts compares Student objects
    @Test
    void testGetStudentByEmail(){
        Student s = new Student("jane.com", "Jane Jones", "password");
        sc.createStudent(s);
        Assertions.assertThat(s.equals(sc.getStudentByEmail("megan@gmail.com")));
    }

    //Creates a students and tests validateStudent method
    @Test
    void testValidateStudent(){
        Student s = new Student();
        String email = "megan@gmail.com";
        String passwordCorrect = "password";
        String passwordIncorrect = "12345";
        s.setEmail(email);
        s.setName("Megan Smith");
        s.setPassword(passwordCorrect);
        sc.createStudent(s);

        assertTrue(sc.validateStudent(email, passwordCorrect)); //tests with correct password, should pass
        assertFalse(sc.validateStudent(email, passwordIncorrect)); //test with incorrect password, should fail

    }

    // Creates Student and a course, registers student to that course
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
        assertThat(s2.getCourses().contains(cs.getCourseById(1))); //checks if student's courses contain newly added course
    }

    // tests getStudentCourses method. Creates Student, 2 courses and registers student for those two courses
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
        assertTrue(s2.getCourses().size()==2); //checks the size of list of courses, should be 2
        assertThat(s2.getCourses().contains(cs.getCourseById(1))); //checks if list of courses contains each added course
        assertThat(s2.getCourses().contains(cs.getCourseById(2)));
        assertThat(s2.getCourses().equals(courses2)); //checks if extracted list of courses equals to list of new added courses
    }

    //creates 3 students
    @Test
    void testGetStudents(){
        sc.createStudent(new Student("boris@gmail.com", "Boris Jones", "password"));
        sc.createStudent(new Student("jenna@gmail.com", "Jenna Smith", "password"));
        sc.createStudent(new Student("leslie@gmail.com", "Leslie Jones", "password"));

        //since we do not delete Student entries after each test, they stay in memory during running all tests.
        //when running all tests together we count new createsStudents and previously created entries:
        //counts in previous tests cases objects: 2 for testCreateStudent and 1 for testValidateStudent(), plus 3 created in this test
        assertTrue(sc.getAllStudents().size() ==6);

        //if test runs separately, we will count only new created entries: only 3
        //uncomment if test runs separately from other tests
        //assertTrue(sc.getAllStudents().size()==3);
    }

}