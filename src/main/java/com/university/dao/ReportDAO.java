package com.university.dao;

import com.university.util.Db;
import java.sql.SQLException;
import java.util.List;

public class ReportDAO {
    public List<Object[]> studentsPerDepartment() throws SQLException {
        return Db.query("SELECT d.name, COUNT(s.student_id) FROM departments d " +
                "LEFT JOIN students s ON s.dept_id=d.dept_id GROUP BY d.name ORDER BY 2 DESC");
    }
    public List<Object[]> courseEnrollmentCount() throws SQLException {
        return Db.query("SELECT c.code, c.title, COUNT(e.enrollment_id) FROM courses c " +
                "LEFT JOIN enrollments e ON e.course_id=c.course_id GROUP BY c.code, c.title ORDER BY 3 DESC");
    }
    public List<Object[]> topStudents() throws SQLException {
        return Db.query("SELECT student_id, name, department, courses_taken, avg_marks " +
                "FROM v_student_performance WHERE avg_marks IS NOT NULL ORDER BY avg_marks DESC LIMIT 10");
    }
}
