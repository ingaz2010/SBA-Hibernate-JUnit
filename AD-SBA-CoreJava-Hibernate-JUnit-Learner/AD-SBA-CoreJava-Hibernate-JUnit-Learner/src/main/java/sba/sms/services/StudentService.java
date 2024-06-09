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
        return null;
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
//        String sql = "FROM Student s WHERE email = :email";
//        TypedQuery<Student> query = session.createQuery(sql, Student.class);
//        query.setParameter("email", email);
//        Student s = (Student) query.getSingleResult();
        tx.commit();
        session.close();
        return s;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Student student = getStudentByEmail(email);
        if(student.getPassword().equals(password) && student.getEmail().equals(email))
            return true;
        else
            return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Student student = getStudentByEmail(email);
        Course course = session.get(Course.class, courseId);
        student.getCourses().add(course);
        session.merge(student);
        tx.commit();
        session.close();

    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<Course> courses = new ArrayList<>();
        //String sql = "FROM Course c WHERE c.email = :email";
        Student student = getStudentByEmail(email);
        for(Course course : student.getCourses()) {
            courses.add(course);
        }
        return courses;
    }
}
