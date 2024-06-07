package sba.sms.services;

import jakarta.persistence.TypedQuery;
import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.sqm.function.SelfRenderingOrderedSetAggregateFunctionSqlAstExpression;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {

    @Override
    public void createStudent(Student student) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(student);
        tx.commit();
        session.close();
        factory.close();
    }

    @Override
    public Student getStudentByEmail(String email) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Student student = (Student) session.get(Student.class, email);
        //System.out.println("Student found: " + student.toString());
        tx.commit();
        session.close();
        factory.close();
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        String sql = "SELECT * FROM student s";
        TypedQuery<Student> query = session.createQuery(sql, Student.class);
        List<Student> students = query.getResultList();
        for(Student s : students){
            System.out.println(s.toString());
        }
        tx.commit();
        session.close();
        factory.close();
        return students;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Student student = (Student) session.get(Student.class, email);
        if(student.getPassword().equals(password) || student.getEmail().equals(email))
            return true;
        else
            return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Student student = (Student) session.get(Student.class, email);
        Course course = (Course) session.get(Course.class, courseId);
        student.getCourses().add(course);
    }


    @Override
    public List<Course> getStudentCourses(String email){
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        List<Course> courses = new ArrayList<>();
        Student student = (Student) session.get(Student.class, email);
        for(Course c : student.getCourses()){
            courses.add(c);
        }
        return courses;
    }
}
