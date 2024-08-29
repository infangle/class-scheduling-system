package com.aastu.scheduler;

import com.aastu.scheduler.DAO.*;
import com.aastu.scheduler.models.Classroom;
import com.aastu.scheduler.models.Course;
import com.aastu.scheduler.models.Instructor;
import com.aastu.scheduler.models.Schedule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            System.out.println("Starting the application");
            Parent root = FXMLLoader.load(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("main.fxml"))));
            Scene scene = new Scene(root, 680, 560);
            Image icon = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("images/aastu_logo.png")));
            stage.getIcons().add(icon);
            stage.setTitle("AASTU Schedule Management System");
            stage.setScene(scene);
//            stage.setFullScreen(true);
//            stage.setFullScreenExitHint("");
//            stage.setFullScreenExitKeyCombination(null);
            stage.show();
            stage.setOnCloseRequest(event -> {
                event.consume();
                exiting(stage);

            });
            System.out.println("###############$$$$$$$$$$$$$$$$$$$$");
            // Initialize DAOs
            CourseDao courseDao = new CourseDao();
            InstructorDao instructorDao = new InstructorDao();
            ClassroomDao classroomDao = new ClassroomDao();
            ScheduleDao scheduleDao = new ScheduleDao();

            // Fetch data
//            List<Course> courses = courseDao.getAllCourses();
//            List<Instructor> instructors = instructorDao.getAllInstructors();
//            List<Classroom> classrooms = classroomDao.getAllClassrooms();


//            ScheduleGenerator scheduleGenerator = new ScheduleGenerator(courseDao,instructorDao,classroomDao, scheduleDao);
//            List<Schedule> schedules = scheduleGenerator.generateSchedules();


//            saveGeneratedSchedules(scheduleDao, schedules);
        }catch (Exception e){

            System.err.println(e.getMessage());
            System.out.println("Something is going wrong");
        }
    }

    public static void main(String[] args) {
        launch();
    }

    private void saveGeneratedSchedules(ScheduleDao scheduleDao, List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
//            scheduleDao.addSchedule(schedule);
            System.out.println(schedule);
        }
        System.out.println("Schedules are generated successfully");
    }

    public void exiting(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Trying to Exit 🏃‍♂️🏃‍♂️");
        alert.setHeaderText("You'r about closing the program 😭😭😭");
        alert.setContentText("Are sure to exit?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Application is exited successfully");
            stage.close();
        }
    }
}
