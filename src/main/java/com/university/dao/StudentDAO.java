package com.university.dao;

import com.university.model.Student;
import com.university.util.Db;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class StudentDAO {
    public static final String[] HEADERS = {"ID", "Name", "Email", "Phone", "DOB", "Department", "Sem"};
    private static final String SELECT =
            "SELECT s.student_id, s.name, s.email, s.phone, s.dob, d.name, s.semester " +
            "FROM students s JOIN departments d ON d.dept_id=s.dept_id ";

    public void add(Student s) throws SQLException {
        Db.update("INSERT INTO students(name,email,phone,dob,dept_id,semester) VALUES(?,?,?,?,?,?)",
                s.name(), s.email(), s.phone(), Date.valueOf(s.dob()), s.deptId(), s.semester());
    }
    public List<Object[]> list() throws SQLException {
        return Db.query(SELECT + "ORDER BY s.student_id");
    }
    public List<Object[]> search(String keyword) throws SQLException {
        String k = "%" + keyword + "%";
        return Db.query(SELECT + "WHERE s.name LIKE ? OR s.email LIKE ? OR d.name LIKE ? ORDER BY s.student_id", k, k, k);
    }
    public int update(int id, String name, String phone, int semester) throws SQLException {
        return Db.update("UPDATE students SET name=?, phone=?, semester=? WHERE student_id=?", name, phone, semester, id);
    }
    public int delete(int id) throws SQLException {
        return Db.update("DELETE FROM students WHERE student_id=?", id);
    }
}
