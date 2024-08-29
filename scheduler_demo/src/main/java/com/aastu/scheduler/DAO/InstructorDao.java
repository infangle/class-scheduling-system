package com.aastu.scheduler.DAO;

import com.aastu.scheduler.models.Instructor;
import com.aastu.scheduler.utility.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorDao {
    public void addInstructor(Instructor instructor) {
        String sql = "INSERT INTO instructors (name, email, department_id,course_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, instructor.getName());
            pstmt.setString(2, instructor.getEmail());
            pstmt.setInt(3, instructor.getDepartmentId());
            pstmt.setInt(4, instructor.getCourseId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateInstructor(Instructor instructor) {
        String sql = "UPDATE instructors SET name = ?, email = ?, department_id = ?, course_id = ? WHERE id = ?";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, instructor.getName());
            pstmt.setString(2, instructor.getEmail());
            pstmt.setInt(3, instructor.getDepartmentId());
            pstmt.setInt(4, instructor.getCourseId());
            pstmt.setInt(5, instructor.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInstructor(int id) {
        String sql = "DELETE FROM instructors WHERE id = ?";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Instructor getInstructorById(int id) {
        String sql = "SELECT * FROM instructors WHERE id = ?";
        Instructor instructor = null;

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                instructor = new Instructor();
                instructor.setId(rs.getInt("id"));
                instructor.setName(rs.getString("name"));
                instructor.setEmail(rs.getString("email"));
                instructor.setDepartment(rs.getString("department_id"));
                instructor.setCourseId(rs.getInt("course_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instructor;
    }
 public boolean handleConflict(Instructor instructor) {
        String sql = "SELECT * FROM instructors WHERE name = ? AND email = ? AND department_id = ? AND course_id = ?";
        boolean conflict = false;

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, instructor.getName());
            pstmt.setString(2, instructor.getEmail());
            pstmt.setInt(3, instructor.getDepartmentId());
            pstmt.setInt(4, instructor.getCourseId());
            ResultSet rs = pstmt.executeQuery();
            conflict = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conflict;
    }
    public List<Instructor> getAllInstructors() {
        String sql = "SELECT i.id, i.name,i.email, d.id AS department_id, d.name AS department,c.id AS course_id, c.name AS course FROM instructors i\n" +
                "JOIN courses c ON i.course_id = c.id\n" +
                "JOIN departments d ON i.department_id = d.id;";
        List<Instructor> instructors = new ArrayList<>();

        try (Connection conn = DatabaseUtility.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setId(rs.getInt("id"));
                instructor.setName(rs.getString("name"));
                instructor.setEmail(rs.getString("email"));
                instructor.setDepartment(rs.getString("department"));
                instructor.setCourse(rs.getString("course"));
                instructor.setCourseId(rs.getInt("course_id"));
                instructor.setDepartmentId(rs.getInt("department_id"));
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instructors;
    }
   public boolean isInstructorTeachesCourse(int instructorId, int courseId) {
        String sql = "SELECT * FROM instructors WHERE id = ? AND course_id = ?";
        boolean teaches = false;

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, instructorId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            teaches = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teaches;
    }
}

