package com.aastu.scheduler.DAO;

import com.aastu.scheduler.models.Classroom;
import com.aastu.scheduler.models.Course;
import com.aastu.scheduler.models.Instructor;
import com.aastu.scheduler.models.Schedule;
import com.aastu.scheduler.utility.DatabaseUtility;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScheduleDao {

    public void addSchedule(Schedule schedule) {
        System.out.println(schedule.getCourseId() + " " + schedule.getInstructorId() + " " + schedule.getClassroomId() + " " + schedule.getStartTime() + " " + schedule.getEndTime() + " " + schedule.getDayOfWeek() + " " + schedule.getYear());
        String sql = "INSERT INTO schedules (course_id, instructor_id, classroom_id, start_time, end_time, day_of_week,year) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getCourseId());
            pstmt.setInt(2, schedule.getInstructorId());
            pstmt.setInt(3, schedule.getClassroomId());
            pstmt.setTime(4, Time.valueOf(schedule.getStartTime()));
            pstmt.setTime(5, Time.valueOf(schedule.getEndTime()));
            pstmt.setString(6, dayOfWeekFormatter(schedule.getDayOfWeek()));
            pstmt.setInt(7,schedule.getYear());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSchedule(Schedule schedule) {
        System.out.println(schedule.getCourseId() + " " + schedule.getInstructorId() + " " + schedule.getClassroomId() + " " + schedule.getStartTime() + " " + schedule.getEndTime() + " " + schedule.getDayOfWeek() + " " + schedule.getYear());

        String sql = "UPDATE schedules SET course_id = ?, instructor_id = ?, classroom_id = ?, start_time = ?, end_time = ?, day_of_week = ?, year = ? WHERE id = ?";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getCourseId());
            pstmt.setInt(2, schedule.getInstructorId());
            pstmt.setInt(3, schedule.getClassroomId());
            pstmt.setTime(4, Time.valueOf(schedule.getStartTime()));
            pstmt.setTime(5, Time.valueOf(schedule.getEndTime()));
            pstmt.setString(6, dayOfWeekFormatter(schedule.getDayOfWeek()));
            pstmt.setInt(7,schedule.getYear());
            pstmt.setInt(8, schedule.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSchedule(int id) {
        String sql = "DELETE FROM schedules WHERE id = ?";

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Schedule getScheduleById(int id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        Schedule schedule = null;

        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                schedule = new Schedule();
                schedule.setId(rs.getInt("id"));
                schedule.setCourseId(rs.getInt("course_id"));
                schedule.setInstructorId(rs.getInt("instructor_id"));
                schedule.setClassroomId(rs.getInt("classroom_id"));
                schedule.setStartTime(rs.getTime("start_time").toLocalTime());
                schedule.setEndTime(rs.getTime("end_time").toLocalTime());
                schedule.setDayOfWeek(rs.getString("day_of_week"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedule;
    }

    public boolean hasConflict(int classroomId,int instructorId, String dayOfWeek, LocalTime startTime, LocalTime endTime,int year) throws SQLException {
        System.out.println("Checking Conflict");
        dayOfWeek = dayOfWeekFormatter(dayOfWeek);
        String query = "SELECT COUNT(*) FROM schedules WHERE (classroom_id = ? OR instructor_id = ?) AND year = ? AND day_of_week = ? " +
                "AND ((start_time < ? AND end_time > ?) OR (start_time >= ? AND end_time <= ?)) ";
        try ( Connection conn = DatabaseUtility.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, classroomId);
            pstmt.setInt(2, instructorId);
            pstmt.setInt(3,year);
            pstmt.setString(4, dayOfWeek);
            pstmt.setTime(5, Time.valueOf(endTime));
            pstmt.setTime(6, Time.valueOf(startTime));
            pstmt.setTime(7, Time.valueOf(startTime));
            pstmt.setTime(8, Time.valueOf(endTime));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    public List<Schedule> getAllSchedules() {
        String sql = "SELECT\n" +
                "    s.id,\n" +
                "    c.id AS course_id, c.name AS course_name,\n" +
                "    m.id AS instructor_id, m.instructor AS instructor_name,\n" +
                "    r.id AS classroom_id, r.name AS classroom_name,\n" +
                "    s.start_time,\n" +
                "    s.end_time,\n" +
                "    s.day_of_week,\n" +
                "    m.department,\n" +
                "    s.year\n" +
                "FROM schedules s\n" +
                "JOIN courses c ON s.course_id = c.id\n" +
                "JOIN (SELECT  i.id,i.name AS instructor, d.name AS department FROM instructors i JOIN departments d ON i.department_id = d.id) m ON s.instructor_id = m.id\n" +
                "JOIN classrooms r ON s.classroom_id = r.id";
        List<Schedule> schedules = new ArrayList<>();

        try (Connection conn = DatabaseUtility.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(rs.getInt("id"));
                schedule.setCourseName(rs.getString("course_name"));
                schedule.setInstructorName(rs.getString("instructor_name"));
                schedule.setClassroomName(rs.getString("classroom_name"));
                schedule.setStartTime(rs.getTime("start_time").toLocalTime());
                schedule.setEndTime(rs.getTime("end_time").toLocalTime());
                schedule.setDayOfWeek(rs.getString("day_of_week"));
                schedule.setDepartment(rs.getString("department"));
                schedule.setYear(rs.getInt("year"));
                schedule.setCourseId(rs.getInt("course_id"));
                schedule.setInstructorId(rs.getInt("instructor_id"));
                schedule.setClassroomId(rs.getInt("classroom_id"));

                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }
    public  String dayOfWeekFormatter(String dateString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }


    /////////
    public List<Schedule> getSchedulesForInstructor(int instructorId, String day) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM generatedSchedules WHERE instructor_id = ? AND day_of_week = ?";
        try (  Connection conn = DatabaseUtility.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, instructorId);
            stmt.setString(2, day);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Schedule schedule = new Schedule(
                            rs.getInt("id"),

                            fetchCourseById(rs.getInt("course_id")),
                            fetchInstructorById(rs.getInt("instructor_id")),
                            fetchClassroomById(rs.getInt("classroom_id")),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime(),
                            rs.getString("day_of_week"),
                            rs.getInt("year")
                    );
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }
    public List<Schedule> getSchedulesForClassroom(int classroomId, String day) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM generatedSchedules WHERE classroom_id = ? AND day_of_week = ?";
        try (    Connection conn = DatabaseUtility.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, classroomId);
            stmt.setString(2, day);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Schedule schedule = new Schedule(
                            rs.getInt("id"),
                            fetchCourseById(rs.getInt("course_id")),
                            fetchInstructorById(rs.getInt("instructor_id")),
                            fetchClassroomById(rs.getInt("classroom_id")),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime(),
                            rs.getString("day_of_week"),
                            rs.getInt("year")
                    );
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }
    private int fetchCourseById(int courseId) {
        CourseDao courseDao = new CourseDao();
          Course course = courseDao.getCourseById(courseId);
        return course.getId();
    }

    private int fetchInstructorById(int instructorId) {
        InstructorDao instructorDao = new InstructorDao();
        Instructor instructor = instructorDao.getInstructorById(instructorId);
        return instructor.getId();
    }

    private int fetchClassroomById(int classroomId) {
        ClassroomDao classroomDao = new ClassroomDao();
        Classroom classroom = classroomDao.getClassroomById(classroomId);
        return classroom.getId();
    }

    public int countSchedulesForInstructorCourseWeek(int instructorId, int courseId) {
        String query = "SELECT COUNT(*) FROM schedules WHERE instructor_id = ? AND course_id = ?";
        try (Connection conn = DatabaseUtility.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, instructorId);
            stmt.setInt(2, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countSchedulesForCourseDay(int classroomId,LocalTime start_time, LocalTime end_time, String dayOfWeek) {

        String query = "SELECT COUNT(*) FROM generatedSchedules WHERE classroom_id = ? AND start_time = ? AND end_time = ? AND day_of_week = ?";
        try (Connection conn = DatabaseUtility.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, classroomId);
            stmt.setTime(2, Time.valueOf(start_time));
            stmt.setTime(3, Time.valueOf(end_time));
            stmt.setString(4, dayOfWeek);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
