package com.aastu.scheduler.controller;

import com.aastu.scheduler.DAO.*;
import com.aastu.scheduler.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ScheduleManagementController {
    public ComboBox<String> startHourComboBox;
    public ComboBox<String> startMinuteComboBox;
    public ComboBox<String> endHourComboBox;
    public ComboBox<String> endMinuteComboBox;
    public ComboBox<String> yearComboBox;
    public TableColumn<Schedule,String>departmentColumn;
    public TableColumn<Schedule, String> yearColumn;

    @FXML
    private TableView<Schedule> scheduleTable;
    public TableColumn<Schedule,String> idColumn;
    public TableColumn<Schedule,String> courseColumn;
    public TableColumn<Schedule,String> classroomColumn;
    public TableColumn<Schedule, String> instructorColumn;

    public TableColumn<Schedule, String> startColumn;
    public TableColumn<Schedule, String> endColumn;
    public TableColumn<Schedule, String> dayOfWeekColumn;

    @FXML
    private ComboBox<Course> courseComboBox;
    @FXML
    private ComboBox<Instructor> instructorComboBox;
    @FXML
    private ComboBox<Classroom> classroomComboBox;
    @FXML
    private DatePicker dayOfWeekField;

    private final ScheduleDao scheduleDao = new ScheduleDao();
    private final CourseDao courseDao = new CourseDao();
    private final InstructorDao instructorDao = new InstructorDao();
    private final ClassroomDao classroomDao = new ClassroomDao();


    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        classroomColumn.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        dayOfWeekColumn.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));


        loadSchedules();
        loadComboBoxes();
        scheduleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courseComboBox.setValue(courseDao.getCourseById(newValue.getCourseId()));
                instructorComboBox.setValue(instructorDao.getInstructorById(newValue.getInstructorId()));
                classroomComboBox.setValue(classroomDao.getClassroomById(newValue.getClassroomId()));
                startHourComboBox.setValue(newValue.getStartTime().getHour() + "");
                startMinuteComboBox.setValue(newValue.getStartTime().getMinute() + "");
                endHourComboBox.setValue(newValue.getEndTime().getHour() + "");
                endMinuteComboBox.setValue(newValue.getEndTime().getMinute() + "");
                yearComboBox.setValue(newValue.getYear() + "");
            }
        });
    }

    private void loadSchedules() {
        scheduleTable.getItems().setAll(scheduleDao.getAllSchedules());
    }

    private void loadComboBoxes() {
        courseComboBox.getItems().setAll(courseDao.getAllCourses());
        instructorComboBox.getItems().setAll(instructorDao.getAllInstructors());
        classroomComboBox.getItems().setAll(classroomDao.getAllClassrooms());

        startHourComboBox.setItems(FXCollections.observableArrayList(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
        ));
        endHourComboBox.setItems(FXCollections.observableArrayList(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
        ));

        startMinuteComboBox.setItems(FXCollections.observableArrayList(
                "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"
        ));
        endMinuteComboBox.setItems(FXCollections.observableArrayList(
                "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"
        ));
        ObservableList<String> years = FXCollections.observableArrayList("1", "2", "3", "4", "5");
        yearComboBox.setItems(years);
    }

    @FXML
    private void handleAddSchedule() throws SQLException {
        if(checkAllFields(new Schedule())){
            showAlert("Input Error", "All fields are required.");
            return;
        }
        String startHour = String.format("%02d", Integer.parseInt(startHourComboBox.getValue()));
        String startMinute = String.format("%02d", Integer.parseInt(startMinuteComboBox.getValue()));
        String endHour = String.format("%02d", Integer.parseInt(endHourComboBox.getValue()));
        String endMinute = String.format("%02d", Integer.parseInt(endMinuteComboBox.getValue()));

        Course course = courseComboBox.getSelectionModel().getSelectedItem();
        Instructor instructor = instructorComboBox.getSelectionModel().getSelectedItem();
        Classroom classroom = classroomComboBox.getSelectionModel().getSelectedItem();
        LocalTime startTime =LocalTime.parse( startHour + ":" + startMinute, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(endHour + ":" + endMinute, DateTimeFormatter.ofPattern("HH:mm"));
        String dayOfWeek = dayOfWeekField.getValue().toString();
        int year = Integer.parseInt(yearComboBox.getValue());

        if(!instructorDao.isInstructorTeachesCourse(instructor.getId(),course.getId())){
            showAlert("Input Error", "Instructor does not teach the course.");
            return;}
        if (scheduleDao.hasConflict(classroom.getId(),instructor.getId(), dayOfWeek, startTime, endTime,year)) {
            showAlert("Scheduling Conflict", "Conflict detected! The schedule overlaps with an existing schedule.");
            return;
        }

        Schedule schedule = new Schedule();
        schedule.setCourseId(course.getId());
        schedule.setInstructorId(instructor.getId());
        schedule.setClassroomId(classroom.getId());
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setYear(year);

        scheduleDao.addSchedule(schedule);
        loadSchedules();
    }

    @FXML
    private void handleUpdateSchedule() throws SQLException {
        Schedule selectedSchedule = scheduleTable.getSelectionModel().getSelectedItem();
        if (checkAllFields(selectedSchedule)) {
            String startHour = String.format("%02d", Integer.parseInt(startHourComboBox.getValue()));
            String startMinute = String.format("%02d", Integer.parseInt(startMinuteComboBox.getValue()));
            String endHour = String.format("%02d", Integer.parseInt(endHourComboBox.getValue()));
            String endMinute = String.format("%02d", Integer.parseInt(endMinuteComboBox.getValue()));

            selectedSchedule.setCourseId(courseComboBox.getSelectionModel().getSelectedItem().getId());
            selectedSchedule.setInstructorId(instructorComboBox.getSelectionModel().getSelectedItem().getId());
            selectedSchedule.setClassroomId(classroomComboBox.getSelectionModel().getSelectedItem().getId());
            selectedSchedule.setStartTime(LocalTime.parse(startHour + ":" + startMinute, DateTimeFormatter.ofPattern("HH:mm")));
            selectedSchedule.setEndTime(LocalTime.parse(endHour + ":" + endMinute, DateTimeFormatter.ofPattern("HH:mm")));
            selectedSchedule.setYear(Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem()));
            selectedSchedule.setDayOfWeek(dayOfWeekField.getValue().toString());

            int courseId = courseComboBox.getSelectionModel().getSelectedItem().getId();
            int instructorId = instructorComboBox.getSelectionModel().getSelectedItem().getId();
            int year = Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem());

            String dayOfWeek = selectedSchedule.getDayOfWeek();
            LocalTime startTime = selectedSchedule.getStartTime();
            LocalTime endTime = selectedSchedule.getEndTime();

           if(!instructorDao.isInstructorTeachesCourse(instructorId,courseId)){
               showAlert("Input Error", "Instructor does not teach the course.");
               return;}
            if (scheduleDao.hasConflict(courseId,instructorId, dayOfWeek, startTime, endTime,year)) {
                showAlert("Scheduling Conflict", "The classroom is already booked during this time.");
                return;
            }
            if (confirmation("Update Schedule", "Are you sure you want to update this schedule?")) {

            scheduleDao.updateSchedule(selectedSchedule);
            loadSchedules();}
        }
        else{
            showAlert("Input Error", "Select to Update and All fields are required.");
        }
    }

    @FXML
    private void handleDeleteSchedule() {
        Schedule selectedSchedule = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedSchedule != null) {
            if(confirmation("Delete Schedule", "Are you sure you want to delete this schedule?")){
            scheduleDao.deleteSchedule(selectedSchedule.getId());
            loadSchedules();}
        }
    }
    @FXML
    private void handleBack() {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/aastu/scheduler/main.fxml")));
            Stage stage = (Stage) startHourComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(null);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }
    @FXML
    private void handleClearFields() {
        courseComboBox.getSelectionModel().clearSelection();
        instructorComboBox.getSelectionModel().clearSelection();
        classroomComboBox.getSelectionModel().clearSelection();
        startHourComboBox.getSelectionModel().clearSelection();
        startMinuteComboBox.getSelectionModel().clearSelection();
        endHourComboBox.getSelectionModel().clearSelection();
        endMinuteComboBox.getSelectionModel().clearSelection();
        dayOfWeekField.getEditor().clear();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean checkAllFields(Schedule schedule) {
        System.out.println(schedule.getDayOfWeek() + schedule.getStartTime() + schedule.getEndTime() + schedule.getYear() + schedule.getCourseId() + schedule.getInstructorId() + schedule.getClassroomId());
        return schedule.getCourseId() != 0 && schedule.getInstructorId() != 0 &&
                schedule.getClassroomId() != 0 && schedule.getStartTime() != null
                && schedule.getEndTime() != null && schedule.getDayOfWeek() != null
                && schedule.getYear() != 0;
    }
    public boolean confirmation(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(title);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }

}
