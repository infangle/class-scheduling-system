package com.aastu.scheduler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleShowStudentManagement(ActionEvent event) {
        loadView(event,"studentManagement.fxml");
    }

    @FXML
    private void handleShowCourseManagement(ActionEvent event) {
        loadView(event,"courseManagement.fxml");
    }

    @FXML
    private void handleShowInstructorManagement(ActionEvent event) {
        loadView(event,"instructorManagement.fxml");
    }

    @FXML
    private void handleShowClassroomManagement(ActionEvent event) {
        loadView(event,"classroomManagement.fxml");
    }

    @FXML
    private void handleShowScheduleManagement(ActionEvent event) {
        loadView(event,"scheduleManagement.fxml");
    }

    private void loadView(ActionEvent event,String fxmlFile) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/aastu/scheduler/" +fxmlFile)));
            primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint(""); // Optional: Remove hint message
            primaryStage.setFullScreenExitKeyCombination(null);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
