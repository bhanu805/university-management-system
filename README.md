# University Management System

A console-based **Java + JDBC + MySQL** application for managing a university: departments, students, faculty, courses, enrollments, grades and reports.

## Features
- Login with role-based access (ADMIN can delete, STAFF cannot). Passwords stored as SHA-256 hashes.
- CRUD for Departments, Students, Faculty and Courses
- Assign faculty to courses
- Enroll students, enter marks (grade auto-calculated), drop courses, student report card
- Search students by name / email / department
- Reports: students per department, enrollments per course, top students by average marks
- All SQL uses `PreparedStatement` (SQL-injection safe)

## Tech stack
Java 17 · Maven · MySQL 8 · JDBC (mysql-connector-j)

## Project structure
```
university-management-system/
├── database/schema.sql            # tables, view, sample data
├── src/main/java/com/university/
│   ├── Main.java                  # console menus
│   ├── model/                     # Student, Faculty, Course records
│   ├── dao/                       # database access classes
│   └── util/                      # DB connection, JDBC helper, console helper
├── src/main/resources/db.properties.example
└── pom.xml
```

## Setup
1. Install **JDK 17+**, **Maven**, **MySQL 8**.
2. Create the database and sample data:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
3. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties` and set your MySQL user/password
   (or set env vars `DB_URL`, `DB_USER`, `DB_PASSWORD`).
4. Build and run:
   ```bash
   mvn clean package
   java -jar target/university-management-system-1.0.0.jar
   ```

## Default logins
| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin123 | ADMIN |
| staff    | staff123 | STAFF |

## ER overview
`departments` 1—N `students`, `faculty`, `courses` · `faculty` 1—N `courses` · `students` N—M `courses` via `enrollments`

## Upload to GitHub
```bash
cd university-management-system
git init
git add .
git commit -m "Initial commit: University Management System"
git branch -M main
git remote add origin https://github.com/<your-username>/university-management-system.git
git push -u origin main
```
> `db.properties` is git-ignored so your password is never uploaded; only the `.example` file is.

## Future ideas
Swing/JavaFX GUI, attendance module, timetable, Spring Boot REST API, JUnit tests.

## License
MIT
