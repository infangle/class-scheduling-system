package com.aastu.scheduler.models;

public class Student {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private int departmentId;
    private int courseId;
    private  String courseName;
    private int year;


    // Constructor, getters, and setters
public Student(){}
    public Student(String id, String name, String email, String phone, String year, int departmentId, int courseId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
       this.year = Integer.parseInt(year);
        this.departmentId = departmentId;
        this.courseId = courseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
