    package com.aastu.scheduler.DAO;
    import com.aastu.scheduler.models.Course;
    import com.aastu.scheduler.utility.DatabaseUtility;

    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;
    public class CourseDao {
            public void addCourse(Course course) {
                String sql = "INSERT INTO courses (name, description, credits) VALUES (?, ?, ?)";

                try (Connection conn = DatabaseUtility.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, course.getName());
                    pstmt.setString(2, course.getDescription());
                    pstmt.setInt(3, course.getCredits());
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void updateCourse(Course course) {
                String sql = "UPDATE courses SET name = ?, description = ?, credits = ? WHERE id = ?";

                try (Connection conn = DatabaseUtility.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, course.getName());
                    pstmt.setString(2, course.getDescription());
                    pstmt.setInt(3, course.getCredits());
                    pstmt.setInt(4, course.getId());
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void deleteCourse(int id) {
                String sql = "DELETE FROM courses WHERE id = ?";

                try (Connection conn = DatabaseUtility.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public Course getCourseById(int id) {
                String sql = "SELECT * FROM courses WHERE id = ?";
                Course course = null;

                try (Connection conn = DatabaseUtility.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        course = new Course();
                        course.setId(rs.getInt("id"));
                        course.setName(rs.getString("name"));
                        course.setDescription(rs.getString("description"));
                        course.setCredits(rs.getInt("credits"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return course;
            }

            public boolean handleConflict(Course course) {
                String sql = "SELECT * FROM courses WHERE course_id = ?";

                try (Connection conn = DatabaseUtility.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, course.getId());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return false;
            }
            public List<Course> getAllCourses() {
                String sql = "SELECT * FROM courses";
                List<Course> courses = new ArrayList<>();

                try (Connection conn = DatabaseUtility.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    while (rs.next()) {
                        Course course = new Course();
                        course.setId(rs.getInt("id"));
                        course.setName(rs.getString("name"));
                        course.setDescription(rs.getString("description"));
                        course.setCredits(rs.getInt("credits"));
                        courses.add(course);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return courses;
            }
        }
