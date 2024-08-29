package com.aastu.scheduler.models;

import com.aastu.scheduler.DAO.ClassroomDao;
import com.aastu.scheduler.DAO.CourseDao;
import com.aastu.scheduler.DAO.InstructorDao;

import java.time.LocalTime;

public class Schedule {
    private int id;
    private int courseId;
    private String courseName;
    private int instructorId;
    private String instructorName;
    private int classroomId;
    private String classroomName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dayOfWeek;
    private int year;
    private String department;

    // Constructors
    public Schedule() {}

    public Schedule(int id, int courseId, int instructorId, int classroomId, LocalTime startTime, LocalTime endTime, String dayOfWeek,int year) {
        this.id = id;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.classroomId = classroomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.year = year;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public  String toString(){
        CourseDao courseDao = new CourseDao();
        courseName = courseDao.getCourseById(courseId).getName();
        InstructorDao instructorDao = new InstructorDao();
        instructorName = instructorDao.getInstructorById(instructorId).getName();
        ClassroomDao classroomDao = new ClassroomDao();
        classroomName = classroomDao.getClassroomById(classroomId).getName();

        return
                "id=" + id +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", instructorId=" + instructorId +
                ", instructorName='" + instructorName + '\'' +
                ", classroomId=" + classroomId +
                ", classroomName='" + classroomName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayOfWeek='" + dayOfWeek + '\'';
    }
}
