package com.university.dao;

import com.university.model.Faculty;
import com.university.util.Db;
import java.sql.SQLException;
import java.util.List;

public class FacultyDAO {
    public static final String[] HEADERS = {"ID", "Name", "Email", "Phone", "Department", "Designation"};

    public void add(Faculty f) throws SQLException {
        Db.update("INSERT INTO faculty(name,email,phone,dept_id,designation) VALUES(?,?,?,?,?)",
                f.name(), f.email(), f.phone(), f.deptId(), f.designation());
    }
    public List<Object[]> list() throws SQLException {
        return Db.query("SELECT f.faculty_id, f.name, f.email, f.phone, d.name, f.designation " +
                "FROM faculty f JOIN departments d ON d.dept_id=f.dept_id ORDER BY f.faculty_id");
    }
    public int update(int id, String name, String phone, String designation) throws SQLException {
        return Db.update("UPDATE faculty SET name=?, phone=?, designation=? WHERE faculty_id=?", name, phone, designation, id);
    }
    public int delete(int id) throws SQLException {
        return Db.update("DELETE FROM faculty WHERE faculty_id=?", id);
    }
}
