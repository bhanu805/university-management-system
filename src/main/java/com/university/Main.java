package com.university;

import com.university.dao.*;
import com.university.model.*;
import com.university.util.Console;

import java.sql.SQLException;

public class Main {
    private static final DepartmentDAO depts = new DepartmentDAO();
    private static final StudentDAO students = new StudentDAO();
    private static final FacultyDAO faculty = new FacultyDAO();
    private static final CourseDAO courses = new CourseDAO();
    private static final EnrollmentDAO enrolls = new EnrollmentDAO();
    private static final ReportDAO reports = new ReportDAO();

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   UNIVERSITY MANAGEMENT SYSTEM");
        System.out.println("=====================================");
        String role = null;
        try {
            for (int tries = 0; tries < 3 && role == null; tries++) {
                role = new AuthDAO().login(Console.str("Username"), Console.str("Password"));
                if (role == null) System.out.println("Invalid credentials.");
            }
        } catch (SQLException e) {
            System.out.println("Cannot connect to database: " + e.getMessage());
            System.out.println("Check src/main/resources/db.properties and that MySQL is running.");
            return;
        }
        if (role == null) { System.out.println("Too many failed attempts."); return; }
        System.out.println("Welcome! Logged in as " + role);

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Departments\n2. Students\n3. Faculty\n4. Courses\n5. Enrollments & Grades\n6. Reports\n0. Exit");
            int ch = Console.integer("Choice");
            try {
                switch (ch) {
                    case 1 -> departmentMenu(role);
                    case 2 -> studentMenu(role);
                    case 3 -> facultyMenu(role);
                    case 4 -> courseMenu(role);
                    case 5 -> enrollmentMenu();
                    case 6 -> reportMenu();
                    case 0 -> { System.out.println("Goodbye!"); return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean canDelete(String role) {
        if (!"ADMIN".equals(role)) System.out.println("Only ADMIN can delete records.");
        return "ADMIN".equals(role);
    }
    private static void result(int n) { System.out.println(n > 0 ? "Done." : "No matching record found."); }

    private static void departmentMenu(String role) throws SQLException {
        System.out.println("\n[Departments] 1.Add 2.List 3.Update 4.Delete 0.Back");
        switch (Console.integer("Choice")) {
            case 1 -> { depts.add(Console.str("Name"), Console.str("Head of dept")); System.out.println("Added."); }
            case 2 -> Console.table(DepartmentDAO.HEADERS, depts.list());
            case 3 -> result(depts.update(Console.integer("Dept ID"), Console.str("New name"), Console.str("New HOD")));
            case 4 -> { if (canDelete(role)) result(depts.delete(Console.integer("Dept ID"))); }
            default -> { }
        }
    }

    private static void studentMenu(String role) throws SQLException {
        System.out.println("\n[Students] 1.Add 2.List 3.Search 4.Update 5.Delete 0.Back");
        switch (Console.integer("Choice")) {
            case 1 -> {
                Console.table(DepartmentDAO.HEADERS, depts.list());
                students.add(new Student(Console.str("Name"), Console.str("Email"), Console.str("Phone"),
                        Console.str("DOB (yyyy-MM-dd)"), Console.integer("Department ID"), Console.integer("Semester")));
                System.out.println("Student added.");
            }
            case 2 -> Console.table(StudentDAO.HEADERS, students.list());
            case 3 -> Console.table(StudentDAO.HEADERS, students.search(Console.str("Keyword")));
            case 4 -> result(students.update(Console.integer("Student ID"), Console.str("New name"),
                    Console.str("New phone"), Console.integer("New semester")));
            case 5 -> { if (canDelete(role)) result(students.delete(Console.integer("Student ID"))); }
            default -> { }
        }
    }

    private static void facultyMenu(String role) throws SQLException {
        System.out.println("\n[Faculty] 1.Add 2.List 3.Update 4.Delete 0.Back");
        switch (Console.integer("Choice")) {
            case 1 -> {
                Console.table(DepartmentDAO.HEADERS, depts.list());
                faculty.add(new Faculty(Console.str("Name"), Console.str("Email"), Console.str("Phone"),
                        Console.integer("Department ID"), Console.str("Designation")));
                System.out.println("Faculty added.");
            }
            case 2 -> Console.table(FacultyDAO.HEADERS, faculty.list());
            case 3 -> result(faculty.update(Console.integer("Faculty ID"), Console.str("New name"),
                    Console.str("New phone"), Console.str("New designation")));
            case 4 -> { if (canDelete(role)) result(faculty.delete(Console.integer("Faculty ID"))); }
            default -> { }
        }
    }

    private static void courseMenu(String role) throws SQLException {
        System.out.println("\n[Courses] 1.Add 2.List 3.Assign faculty 4.Delete 0.Back");
        switch (Console.integer("Choice")) {
            case 1 -> {
                Console.table(DepartmentDAO.HEADERS, depts.list());
                courses.add(new Course(Console.str("Code"), Console.str("Title"), Console.integer("Credits"),
                        Console.integer("Department ID"), null));
                System.out.println("Course added.");
            }
            case 2 -> Console.table(CourseDAO.HEADERS, courses.list());
            case 3 -> result(courses.assignFaculty(Console.integer("Course ID"), Console.integer("Faculty ID")));
            case 4 -> { if (canDelete(role)) result(courses.delete(Console.integer("Course ID"))); }
            default -> { }
        }
    }

    private static void enrollmentMenu() throws SQLException {
        System.out.println("\n[Enrollments] 1.Enroll student 2.Enter marks 3.Drop 4.List all 5.Student report card 0.Back");
        switch (Console.integer("Choice")) {
            case 1 -> { enrolls.enroll(Console.integer("Student ID"), Console.integer("Course ID")); System.out.println("Enrolled."); }
            case 2 -> {
                int id = Console.integer("Enrollment ID");
                double m = Console.decimal("Marks (0-100)");
                if (m < 0 || m > 100) System.out.println("Marks must be 0-100.");
                else result(enrolls.assignMarks(id, m));
            }
            case 3 -> result(enrolls.drop(Console.integer("Enrollment ID")));
            case 4 -> Console.table(EnrollmentDAO.HEADERS, enrolls.list());
            case 5 -> Console.table(EnrollmentDAO.HEADERS, enrolls.forStudent(Console.integer("Student ID")));
            default -> { }
        }
    }

    private static void reportMenu() throws SQLException {
        System.out.println("\n[Reports] 1.Students per department 2.Enrollments per course 3.Top students 0.Back");
        switch (Console.integer("Choice")) {
            case 1 -> Console.table(new String[]{"Department", "Students"}, reports.studentsPerDepartment());
            case 2 -> Console.table(new String[]{"Code", "Title", "Enrolled"}, reports.courseEnrollmentCount());
            case 3 -> Console.table(new String[]{"ID", "Name", "Department", "Courses", "Avg Marks"}, reports.topStudents());
            default -> { }
        }
    }
}
