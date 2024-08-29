package com.aastu.scheduler.controller;

import com.aastu.scheduler.DAO.CourseDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.aastu.scheduler.models.Course;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CourseManagementController {
    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> idColumn;
    @FXML
    private TableColumn<Course, String> nameColumn;
    @FXML
    private TableColumn<Course, String> descriptionColumn;
    @FXML
    private TableColumn<Course, String> creditsColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField capacityField;

    private CourseDao courseDoa = new CourseDao();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));

        courseTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                descriptionField.setText(newValue.getDescription());
                capacityField.setText(String.valueOf(newValue.getCredits()));
            }
        });
        loadCourses();
    }

    private void loadCourses() {
        courseTable.getItems().setAll(courseDoa.getAllCourses());
    }

    @FXML
    private void handleAddCourse() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        int credits = Integer.parseInt(capacityField.getText());
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setCredits(credits);

        if(courseDoa.handleConflict(course)){
            showAlert("Conflict Error", "Course with the same name and description already exists.");
            return;
        }
        courseDoa.addCourse(course);
        loadCourses();
    }

    @FXML
    private void handleUpdateCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            selectedCourse.setName(nameField.getText());
            selectedCourse.setDescription(descriptionField.getText());
            selectedCourse.setCredits(Integer.parseInt(capacityField.getText()));

            if (courseDoa.handleConflict(selectedCourse)) {
                showAlert("Conflict Error", "Course with the same name and description already exists.");
            } else {
                if(!confirmation("Update Course", "Are you sure you want to update the course?")){
                    return;
                }
                courseDoa.updateCourse(selectedCourse);
                loadCourses();
            }
        }
    }

    @FXML
    private void handleDeleteCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            if(!confirmation("Delete Course", "Are you sure you want to delete the course?")){
                return;
            }
            courseDoa.deleteCourse(selectedCourse.getId());
            loadCourses();
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/aastu/scheduler/main.fxml")));
            Stage stage = (Stage) nameField.getScene().getWindow();
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
        nameField.clear();
        descriptionField.clear();
        capacityField.clear();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public boolean confirmation(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(title);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }

}
