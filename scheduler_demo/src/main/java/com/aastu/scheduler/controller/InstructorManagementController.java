package com.aastu.scheduler.controller;

import com.aastu.scheduler.DAO.CourseDao;
import com.aastu.scheduler.DAO.DepartmentDao;
import com.aastu.scheduler.DAO.InstructorDao;
import com.aastu.scheduler.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.aastu.scheduler.models.Instructor;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class InstructorManagementController {
    public ComboBox<Course> courseComboBox;
    public TableColumn<Instructor,String> courseColumn;
    public ComboBox<Department> departmentComboBox;
    @FXML
    private TableView<Instructor> instructorTable;

    @FXML
    private TableColumn<Instructor, String> idColumn;
    @FXML
    private TableColumn<Instructor, String> nameColumn;
    @FXML
    private TableColumn<Instructor, String> emailColumn;
    @FXML
    private TableColumn<Instructor, String> departmentColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;


    final private InstructorDao instructorDao = new InstructorDao();
    final private CourseDao courseDao = new CourseDao();
    final private DepartmentDao departmentDao = new DepartmentDao();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));

        courseComboBox.getItems().setAll(courseDao.getAllCourses());
        departmentComboBox.getItems().setAll(departmentDao.getAllDepartments());

        instructorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                emailField.setText(newValue.getEmail());
                departmentComboBox.getSelectionModel().select(departmentDao.getDepartmentById(newValue.getDepartmentId()));
                courseComboBox.getSelectionModel().select(courseDao.getCourseById(newValue.getCourseId()));
            }
        });
        loadInstructors();
    }

    private void loadInstructors() {
        instructorTable.getItems().setAll(instructorDao.getAllInstructors());
    }

    @FXML
    private void handleAddInstructor() {
        String name = nameField.getText();
        String email = emailField.getText();
        int departmentId = departmentComboBox.getSelectionModel().getSelectedItem().getId();
        int courseId = courseComboBox.getSelectionModel().getSelectedItem().getId();
        Instructor instructor = new Instructor();
        instructor.setName(name);
        instructor.setEmail(email);
        instructor.setDepartmentId(departmentId);
        instructor.setCourseId(courseId);

        if (name.isEmpty() || email.isEmpty() || departmentId == -1 || courseId == -1) {
            showAlert("Input Error", "Please fill all the fields.");
        }
        if (instructorDao.handleConflict(instructor)) {
            showAlert("Conflict Error", "Instructor with the same name, email and department already exists.");
        } else {
            instructorDao.addInstructor(instructor);
            loadInstructors();
        }
    }
    @FXML
    private void handleUpdateInstructor() {
        Instructor selectedInstructor = instructorTable.getSelectionModel().getSelectedItem();
        if (selectedInstructor != null) {
            selectedInstructor.setName(nameField.getText());
            selectedInstructor.setEmail(emailField.getText());
            selectedInstructor.setDepartmentId(departmentComboBox.getSelectionModel().getSelectedItem().getId());
            selectedInstructor.setCourseId(courseComboBox.getSelectionModel().getSelectedItem().getId());

            if (selectedInstructor.getName().isEmpty() || selectedInstructor.getEmail().isEmpty() || selectedInstructor.getDepartmentId() == -1 || selectedInstructor.getCourseId() == -1){
                showAlert("Input Error", "Please fill all the fields.");
            }
            if (instructorDao.handleConflict(selectedInstructor)) {
                showAlert("Conflict Error", "Instructor with the same name, email and department already exists.");
            } else {
                // System.out.println(selectedInstructor.getName() + " " + selectedInstructor.getEmail() + " " + selectedInstructor.getDepartment(
                if (confirmation("Update Instructor", "Are you sure you want to update this instructor?")) {
                    instructorDao.updateInstructor(selectedInstructor);
                    loadInstructors();
                }

            }
        }
    }

    @FXML
    private void handleDeleteInstructor() {
        Instructor selectedInstructor = instructorTable.getSelectionModel().getSelectedItem();
        if (selectedInstructor != null) {
            if (!confirmation("Delete Instructor", "Are you sure you want to delete this instructor?")) {
                return;
            }
            instructorDao.deleteInstructor(selectedInstructor.getId());
            loadInstructors();
        } else {
            showAlert("Selection Error", "Please select an instructor to delete.");
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
        emailField.clear();
        departmentComboBox.getSelectionModel().clearSelection();
        courseComboBox.getSelectionModel().clearSelection();
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
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
