    package com.aastu.scheduler.controller;

    import com.aastu.scheduler.DAO.CourseDao;
    import com.aastu.scheduler.DAO.DepartmentDao;
    import com.aastu.scheduler.DAO.StudentDao;
    import com.aastu.scheduler.models.Course;
    import com.aastu.scheduler.models.Department;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.Alert.AlertType;
    import javafx.scene.control.cell.PropertyValueFactory;
    import com.aastu.scheduler.models.Student;
    import javafx.stage.Stage;

    import java.io.IOException;
    import java.net.URL;
    import java.util.List;
    import java.util.Objects;
    import java.util.ResourceBundle;

    public class StudentManagementController implements Initializable {

        public ComboBox<Department> departmentComboBox;
        public ComboBox<Course> courseComboBox;
        public ComboBox<String> yearComboBox;
        public TableColumn<Student, String> courseColumn;
        public TableColumn<Student, String> yearColumn;
        @FXML
        private TableView<Student> studentTable;
        @FXML
        private TableColumn<Student, String> idColumn;
        @FXML
        private TableColumn<Student, String> nameColumn;
        @FXML
        private TableColumn<Student, String> emailColumn;
        @FXML
        private TableColumn<Student, String> phoneColumn;
        @FXML
        private TableColumn<Student, String> departmentColumn;

        @FXML
        private TextField idField;
        @FXML
        private TextField nameField;
        @FXML
        private TextField emailField;
        @FXML
        private TextField phoneField;

        public StudentManagementController(){}
        private final StudentDao studentDao = new StudentDao();
        private final DepartmentDao departmentDao = new DepartmentDao();
        private final CourseDao courseDao = new CourseDao();
        private ObservableList<Student> studentData = FXCollections.observableArrayList();

        @FXML
        public void initialize(URL url, ResourceBundle resourceBundle) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
            courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

            studentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            System.out.println(departmentDao.getDepartmentById(newValue.getDepartmentId()));
                            System.out.println(courseDao.getCourseById(newValue.getCourseId()));
                            idField.setText(newValue.getId());
                            nameField.setText(newValue.getName());
                            emailField.setText(newValue.getEmail());
                            phoneField.setText(newValue.getPhone());
                            yearComboBox.getSelectionModel().select(String.valueOf(newValue.getYear()));
                            departmentComboBox.setValue(departmentDao.getDepartmentById(newValue.getDepartmentId()));
                            courseComboBox.setValue(courseDao.getCourseById(newValue.getCourseId()));
                        }
                    });
            loadComboBoxes();
            loadStudentData();
        }

        private void loadStudentData() {studentTable.getItems().setAll(studentDao.getAllStudents());
        }
        private void loadComboBoxes() {

            departmentComboBox.getItems().setAll(departmentDao.getAllDepartments());
            courseComboBox.getItems().setAll(courseDao.getAllCourses());
            ObservableList<String> years = FXCollections.observableArrayList("1", "2", "3", "4", "5");
            yearComboBox.setItems(years);
        }


        @FXML
        private void handleAddStudent() {
            String id = idField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String year = yearComboBox.getSelectionModel().getSelectedItem();
            int department = departmentComboBox.getSelectionModel().getSelectedItem().getId();
            int course = courseComboBox.getSelectionModel().getSelectedItem().getId();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()
                    || department == -1 || year.isEmpty() || course == -1){
                showAlert("Input Error", "All fields are required.");
                return;
            }

            Student newStudent = new Student(id, name, email, phone,year, department, course);
            if(studentDao.handleConflict(newStudent)){
                showAlert("Conflict Error", "Student with the same id already exists.");
                return;
            }
                studentDao.addStudent(newStudent);
                studentData.add(newStudent);
                loadStudentData();
                clearFields();

        }

        @FXML
        private void handleUpdateStudent() {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            String oldId = selectedStudent.getId();
            if (selectedStudent == null) {
                showAlert("Selection Error", "Please select a student to update.");
                return;
            }
            selectedStudent.setId(idField.getText());
            selectedStudent.setName(nameField.getText());
            selectedStudent.setEmail(emailField.getText());
            selectedStudent.setPhone(phoneField.getText());
            selectedStudent.setYear(Integer.parseInt(yearComboBox.getSelectionModel().getSelectedItem()));
            selectedStudent.setDepartmentId(departmentComboBox.getSelectionModel().getSelectedItem().getId());
            selectedStudent.setCourseId(courseComboBox.getSelectionModel().getSelectedItem().getId());

            if(studentDao.handleConflict(selectedStudent)){
                        showAlert("Conflict Error", "Student with the same id already exists.");
                        return;
                    }
            if (!confirmation("Update Student", "Are you sure you want to update this student?")) {
                return;
            }
            studentDao.updateStudent(oldId,selectedStudent);
            studentTable.refresh();
            clearFields();
        }

        @FXML
        private void handleDeleteStudent() {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            if (selectedStudent == null) {
                showAlert("Selection Error", "Please select a student to delete.");
                return;
            }
            if (!confirmation("Delete Student", "Are you sure you want to delete this student?")) {
                return;
            }
            studentDao.deleteStudent(selectedStudent.getId());
            studentData.remove(selectedStudent);
            loadStudentData();
            clearFields();
        }
        @FXML
        private void handleBack(ActionEvent event) {

            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/aastu/scheduler/main.fxml")));
                Stage stage = (Stage) idField.getScene().getWindow();
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
            clearFields();
        }

        private void clearFields() {
            idField.clear();
            nameField.clear();
            emailField.clear();
            phoneField.clear();
            departmentComboBox.getSelectionModel().clearSelection();
            yearComboBox.getSelectionModel().clearSelection();
            courseComboBox.getSelectionModel().clearSelection();
        }

        public void showAlert(String title, String message) {
            Alert alert = new Alert(AlertType.INFORMATION);
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
