package com.university.dao;

import com.university.model.Course;
import com.university.util.Db;
import java.sql.SQLException;
import java.util.List;

public class CourseDAO {
    public static final String[] HEADERS = {"ID", "Code", "Title", "Credits", "Department", "Faculty"};

    public void add(Course c) throws SQLException {
        Db.update("INSERT INTO courses(code,title,credits,dept_id,faculty_id) VALUES(?,?,?,?,?)",
                c.code(), c.title(), c.credits(), c.deptId(), c.facultyId());
    }
    public List<Object[]> list() throws SQLException {
        return Db.query("SELECT c.course_id, c.code, c.title, c.credits, d.name, COALESCE(f.name,'-') " +
                "FROM courses c JOIN departments d ON d.dept_id=c.dept_id " +
                "LEFT JOIN faculty f ON f.faculty_id=c.faculty_id ORDER BY c.course_id");
    }
    public int assignFaculty(int courseId, int facultyId) throws SQLException {
        return Db.update("UPDATE courses SET faculty_id=? WHERE course_id=?", facultyId, courseId);
    }
    public int delete(int id) throws SQLException {
        return Db.update("DELETE FROM courses WHERE course_id=?", id);
    }
}
