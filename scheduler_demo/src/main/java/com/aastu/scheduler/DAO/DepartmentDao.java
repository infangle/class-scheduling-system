package com.aastu.scheduler.DAO;

import com.aastu.scheduler.models.Department;
import com.aastu.scheduler.utility.DatabaseUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    public void addDepartment(Department department) {
        String query = "INSERT INTO departments (name) VALUES (?)";
        try (   Connection connection = DatabaseUtility.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, department.getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Department getDepartmentById(int id) {
        String query = "SELECT * FROM departments WHERE id = ?";
        try (   Connection connection = DatabaseUtility.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Department(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM departments";
        try (   Connection connection = DatabaseUtility.getConnection();
                Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public void updateDepartment(Department department) {
        String query = "UPDATE departments SET name = ? WHERE id = ?";
        try (   Connection connection = DatabaseUtility.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, department.getName());
            stmt.setInt(2, department.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
