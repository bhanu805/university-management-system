package com.university.dao;

import com.university.util.Db;
import java.sql.SQLException;
import java.util.List;

public class EnrollmentDAO {
    public static final String[] HEADERS = {"Enroll ID", "Student", "Course", "Marks", "Grade"};
    private static final String SELECT =
            "SELECT e.enrollment_id, s.name, CONCAT(c.code,' - ',c.title), " +
            "COALESCE(e.marks,'-'), COALESCE(e.grade,'-') FROM enrollments e " +
            "JOIN students s ON s.student_id=e.student_id JOIN courses c ON c.course_id=e.course_id ";

    public static String gradeFor(double m) {
        if (m >= 90) return "A";
        if (m >= 80) return "B";
        if (m >= 70) return "C";
        if (m >= 60) return "D";
        if (m >= 50) return "E";
        return "F";
    }

    public void enroll(int studentId, int courseId) throws SQLException {
        Db.update("INSERT INTO enrollments(student_id,course_id) VALUES(?,?)", studentId, courseId);
    }
    public int assignMarks(int enrollmentId, double marks) throws SQLException {
        return Db.update("UPDATE enrollments SET marks=?, grade=? WHERE enrollment_id=?",
                marks, gradeFor(marks), enrollmentId);
    }
    public int drop(int enrollmentId) throws SQLException {
        return Db.update("DELETE FROM enrollments WHERE enrollment_id=?", enrollmentId);
    }
    public List<Object[]> list() throws SQLException {
        return Db.query(SELECT + "ORDER BY e.enrollment_id");
    }
    public List<Object[]> forStudent(int studentId) throws SQLException {
        return Db.query(SELECT + "WHERE e.student_id=?", studentId);
    }
}
