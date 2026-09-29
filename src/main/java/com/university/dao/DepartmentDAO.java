package com.university.dao;

import com.university.util.Db;
import java.sql.SQLException;
import java.util.List;

public class DepartmentDAO {
    public static final String[] HEADERS = {"ID", "Department", "Head of Dept", "Students", "Faculty"};

    public void add(String name, String hod) throws SQLException {
        Db.update("INSERT INTO departments(name,hod) VALUES(?,?)", name, hod);
    }
    public List<Object[]> list() throws SQLException {
        return Db.query("SELECT d.dept_id, d.name, d.hod, " +
                "(SELECT COUNT(*) FROM students s WHERE s.dept_id=d.dept_id), " +
                "(SELECT COUNT(*) FROM faculty f WHERE f.dept_id=d.dept_id) " +
                "FROM departments d ORDER BY d.dept_id");
    }
    public int update(int id, String name, String hod) throws SQLException {
        return Db.update("UPDATE departments SET name=?, hod=? WHERE dept_id=?", name, hod, id);
    }
    public int delete(int id) throws SQLException {
        return Db.update("DELETE FROM departments WHERE dept_id=?", id);
    }
}
