package com.aastu.scheduler.DAO;

import com.aastu.scheduler.models.Classroom;
import com.aastu.scheduler.utility.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassroomDao {
    public void addClassroom(Classroom classroom) {
        String sql = "INSERT INTO classrooms (name, capacity, location) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, classroom.getName());
            pstmt.setInt(2, classroom.getCapacity());
            pstmt.setString(3, classroom.getLocation());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClassroom(Classroom classroom) {
        String sql = "UPDATE classrooms SET name = ?, capacity = ?, location = ? WHERE id = ?";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, classroom.getName());
            pstmt.setInt(2, classroom.getCapacity());
            pstmt.setString(3, classroom.getLocation());
            pstmt.setInt(4, classroom.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClassroom(int id) {
        String sql = "DELETE FROM classrooms WHERE id = ?";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Classroom getClassroomById(int id) {
        String sql = "SELECT * FROM classrooms WHERE id = ?";
        Classroom classroom = null;

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                classroom = new Classroom();
                classroom.setId(rs.getInt("id"));
                classroom.setName(rs.getString("name"));
                classroom.setCapacity(rs.getInt("capacity"));
                classroom.setLocation(rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classroom;
    }
public boolean handleConflict(Classroom classroom) {
        String sql = "SELECT * FROM classrooms WHERE id = ?";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, classroom.getId());

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Classroom> getAllClassrooms() {
        String sql = "SELECT * FROM classrooms";
        List<Classroom> classrooms = new ArrayList<>();

        try (Connection conn = DatabaseUtility.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Classroom classroom = new Classroom();
                classroom.setId(rs.getInt("id"));
                classroom.setName(rs.getString("name"));
                classroom.setCapacity(rs.getInt("capacity"));
                classroom.setLocation(rs.getString("location"));
                classrooms.add(classroom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classrooms;
    }
}
