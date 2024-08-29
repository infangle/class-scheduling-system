package com.aastu.scheduler.DAO;

import com.aastu.scheduler.models.Student;
import com.aastu.scheduler.utility.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
        public Student addStudent(Student student) {
            String sql = "INSERT INTO students (id, name, email, phone, year, department_id, course_id)  VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseUtility.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, student.getId());
                pstmt.setString(2, student.getName());
                pstmt.setString(3, student.getEmail());
                pstmt.setString(4, student.getPhone());
                pstmt.setInt(5, student.getYear());
                pstmt.setInt(6, student.getDepartmentId());
                pstmt.setInt(7, student.getCourseId());
                pstmt.executeUpdate();
                return student;
            } catch (SQLException e) {
               e.printStackTrace();
               return null;
            }
        }

        public void updateStudent(String oldId,Student student) {
            String sql = "UPDATE students SET name = ?, email = ?, phone = ?,year = ?, department_id = ?, course_id = ?, id = ? WHERE id = ?";

            try (Connection conn = DatabaseUtility.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, student.getName());
                pstmt.setString(2, student.getEmail());
                pstmt.setString(3, student.getPhone());
                pstmt.setInt(4, student.getYear());
                pstmt.setInt(5, student.getDepartmentId());
                pstmt.setInt(6, student.getCourseId());
                pstmt.setString(7, student.getId());
                pstmt.setString(8, oldId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteStudent(String id) {
            System.out.println(id);
            String sql = "DELETE FROM students WHERE id = ?";

            try (Connection conn = DatabaseUtility.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public Student getStudentById(String id) {
            String sql = "SELECT * FROM students WHERE id = ?";
            Student student = null;

            try (Connection conn = DatabaseUtility.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));
                    student.setYear(rs.getInt("year"));
                    student.setDepartmentId(rs.getInt("department_id"));
                    student.setCourseId(rs.getInt("course_id"));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return student;
        }
public boolean handleConflict( Student student) {
    System.out.println(student.getId());
            String sql = "SELECT * FROM students WHERE id = ?";
            Student student1 = null;

            try (Connection conn = DatabaseUtility.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, student.getId());
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    student1 = new Student();
                    student1.setId(rs.getString("id"));
                    student1.setName(rs.getString("name"));
                    student1.setEmail(rs.getString("email"));
                    student1.setPhone(rs.getString("phone"));
                    student1.setYear(rs.getInt("year"));
                    student1.setDepartment(rs.getString("department_id"));
                    student1.setCourseId(rs.getInt("course_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
          return student1 != null;
}
        public List<Student> getAllStudents() {
            String sql = "SELECT s.id, s.name, s.email, s.phone , s.year, d.id AS department_id, d.name as department,c.id AS course_id, c.name course FROM students s \n" +
                    "JOIN departments d ON s.department_id = d.id\n" +
                    "JOIN courses c ON s.course_id = c.id";
            List<Student> students = new ArrayList<>();
            try (Connection conn = DatabaseUtility.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));
                    student.setYear(rs.getInt("year"));
                    student.setDepartment(rs.getString("department"));
                    student.setCourseName(rs.getString("course"));
                    student.setCourseId(rs.getInt("course_id"));
                    student.setDepartmentId(rs.getInt("department_id"));
                    students.add(student);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return students;
        }
    }


