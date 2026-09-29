-- University Management System : schema + sample data
DROP DATABASE IF EXISTS university_db;
CREATE DATABASE university_db CHARACTER SET utf8mb4;
USE university_db;

CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash CHAR(64) NOT NULL,           -- SHA-256 hex
  role ENUM('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF'
);

CREATE TABLE departments (
  dept_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  hod VARCHAR(100)
);

CREATE TABLE students (
  student_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  phone VARCHAR(20),
  dob DATE,
  dept_id INT NOT NULL,
  semester INT NOT NULL DEFAULT 1,
  admission_date DATE DEFAULT (CURRENT_DATE),
  FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE RESTRICT
);

CREATE TABLE faculty (
  faculty_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  phone VARCHAR(20),
  dept_id INT NOT NULL,
  designation VARCHAR(60),
  FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE RESTRICT
);

CREATE TABLE courses (
  course_id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(20) NOT NULL UNIQUE,
  title VARCHAR(120) NOT NULL,
  credits INT NOT NULL CHECK (credits BETWEEN 1 AND 10),
  dept_id INT NOT NULL,
  faculty_id INT NULL,
  FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE RESTRICT,
  FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE SET NULL
);

CREATE TABLE enrollments (
  enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  marks DECIMAL(5,2) NULL,
  grade CHAR(1) NULL,
  enrolled_on DATE DEFAULT (CURRENT_DATE),
  UNIQUE (student_id, course_id),
  FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
  FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Handy view for reports
CREATE VIEW v_student_performance AS
SELECT s.student_id, s.name, d.name AS department,
       COUNT(e.enrollment_id) AS courses_taken,
       ROUND(AVG(e.marks),2) AS avg_marks
FROM students s
JOIN departments d ON d.dept_id = s.dept_id
LEFT JOIN enrollments e ON e.student_id = s.student_id
GROUP BY s.student_id, s.name, d.name;

-- ---------- Sample data ----------
INSERT INTO users (username, password_hash, role) VALUES
 ('admin', SHA2('admin123',256), 'ADMIN'),
 ('staff', SHA2('staff123',256), 'STAFF');

INSERT INTO departments (name, hod) VALUES
 ('Computer Science','Dr. Anil Sharma'),
 ('Electronics','Dr. Meera Iyer'),
 ('Mechanical','Dr. Rahul Verma'),
 ('Business Administration','Dr. Sunita Rao');

INSERT INTO faculty (name,email,phone,dept_id,designation) VALUES
 ('Dr. Anil Sharma','anil@uni.edu','9800000001',1,'Professor'),
 ('Priya Nair','priya@uni.edu','9800000002',1,'Assistant Professor'),
 ('Dr. Meera Iyer','meera@uni.edu','9800000003',2,'Professor'),
 ('Rahul Verma','rahul@uni.edu','9800000004',3,'Associate Professor');

INSERT INTO courses (code,title,credits,dept_id,faculty_id) VALUES
 ('CS101','Programming in Java',4,1,1),
 ('CS201','Database Systems',4,1,2),
 ('EC101','Basic Electronics',3,2,3),
 ('ME101','Engineering Mechanics',3,3,4);

INSERT INTO students (name,email,phone,dob,dept_id,semester) VALUES
 ('Aarav Singh','aarav@student.edu','9700000001','2004-05-11',1,3),
 ('Diya Patel','diya@student.edu','9700000002','2004-08-23',1,3),
 ('Kabir Das','kabir@student.edu','9700000003','2003-12-02',2,5),
 ('Sneha Roy','sneha@student.edu','9700000004','2004-03-17',3,3);

INSERT INTO enrollments (student_id,course_id,marks,grade) VALUES
 (1,1,88,'B'),(1,2,93,'A'),(2,1,76,'C'),(3,3,81,'B'),(4,4,67,'D');
