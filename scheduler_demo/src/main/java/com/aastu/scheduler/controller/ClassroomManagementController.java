package com.aastu.scheduler.controller;

import com.aastu.scheduler.DAO.ClassroomDao;
import com.aastu.scheduler.models.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.aastu.scheduler.models.Classroom;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ClassroomManagementController {
    @FXML
    private TableView<Classroom> classroomTable;
    @FXML
    private TableColumn<Student, String> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> capacityColumn;
    @FXML
    private TableColumn<Student, String> locationColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField locationField;

    private final ClassroomDao classroomDao = new ClassroomDao();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        classroomTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                capacityField.setText(String.valueOf(newValue.getCapacity()));
                locationField.setText(newValue.getLocation());
            }
        });
        loadClassrooms();
    }

    private void loadClassrooms() {
        classroomTable.getItems().setAll(classroomDao.getAllClassrooms());
    }

    @FXML
    private void handleAddClassroom() {
        String name = nameField.getText();
        int capacity = Integer.parseInt(capacityField.getText());
        String location = locationField.getText();
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setCapacity(capacity);
        classroom.setLocation(location);

        if(classroomDao.handleConflict(classroom)){
            showAlert("Classroom Conflict", "Classroom already exists");
            return;
        }
        classroomDao.addClassroom(classroom);
        loadClassrooms();
    }

    @FXML
    private void handleUpdateClassroom() {
        Classroom selectedClassroom = classroomTable.getSelectionModel().getSelectedItem();
        if (selectedClassroom != null) {
            selectedClassroom.setName(nameField.getText());
            selectedClassroom.setCapacity(Integer.parseInt(capacityField.getText()));
            selectedClassroom.setLocation(locationField.getText());

            if (classroomDao.handleConflict(selectedClassroom)) {
                showAlert("Classroom Conflict", "Classroom already exists");
                return;
            }
            if (!confirmation("Update Classroom", "Are you sure you want to update this classroom?")) {
                return;
            }
            classroomDao.updateClassroom(selectedClassroom);
            loadClassrooms();
        }
    }

    @FXML
    private void handleDeleteClassroom() {
        Classroom selectedClassroom = classroomTable.getSelectionModel().getSelectedItem();
        if (selectedClassroom != null) {
            if (!confirmation("Delete Classroom", "Are you sure you want to delete this classroom?")) {
                return;
            }
            classroomDao.deleteClassroom(selectedClassroom.getId());
            loadClassrooms();
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/aastu/scheduler/main.fxml")));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint(""); // Optional: Remove hint message
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
        capacityField.clear();
        locationField.clear();
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
