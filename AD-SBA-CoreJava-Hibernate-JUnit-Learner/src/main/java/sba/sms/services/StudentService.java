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

import java.sql.ResultSet;
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
    public List<Student> getAllStudents() {
       Session session = HibernateUtil.getSessionFactory().openSession();
       Transaction tx = session.beginTransaction();
       List<Student> students = session.createQuery("from Student").list();
       tx.commit();
       session.close();
       return students;

    }

    @Override
    public void createStudent(Student student) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(student);
        tx.commit();
        session.close();

    }

    @Override
    public Student getStudentByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Student s = session.get(Student.class, email);
        tx.commit();
        session.close();
        return s;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Student student = getStudentByEmail(email); //finds student record using getStudentByEmail and then compares password
        if(student.getPassword().equals(password) && student.getEmail().equals(email))
            return true;
        else
            return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Student student = getStudentByEmail(email); //finds student by email
        Course course = session.get(Course.class, courseId); //finds course using id
        student.getCourses().add(course);     //add course to the list of student's courses
        session.merge(student);      //update student
        tx.commit();
        session.close();

    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<Course> courses = new ArrayList<>();
        Student student = getStudentByEmail(email); //finds student by email
        for(Course course : student.getCourses()) { //extracts each course on student's record and copies to new list
            courses.add(course);
        }
        return courses;
    }
}
