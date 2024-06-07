package sba.sms.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI{

    @Override
    public void createCourse(Course course) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(course);
        tx.commit();
        session.close();
        factory.close();
    }

    @Override
    public Course getCourseById(int courseId) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
       Course course = (Course) session.get(Course.class, courseId);
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        List<Course> courses = session.createQuery("from Course").list();
        tx.commit();
        session.close();
        factory.close();
        return courses;
    }
}
