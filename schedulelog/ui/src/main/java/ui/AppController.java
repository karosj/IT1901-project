package ui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import java.time.format.DateTimeFormatter;

import schedulelog.json.RestConsumer;
import schedulelog.core.Activity;
import schedulelog.core.Subject;
import schedulelog.core.Courses;

import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;


public class AppController {

    private List<Activity> activities = new ArrayList<>();
    private RestConsumer restConsumer;
    private Courses courses;

    public AppController() {
        this.restConsumer = new RestConsumer();
        courses = new Courses();
    }

    public AppController(RestConsumer restConsumer) {
        this.restConsumer = restConsumer;
        courses = new Courses();
    }

    @FXML
    private TableView<Activity> activitiesTableView;
    @FXML
    private TableColumn<Activity, String> descriptionColumn;
    @FXML
    private TableColumn<Activity, String> startTimeColumn;
    @FXML
    private TableColumn<Activity, String> endTimeColumn;
    @FXML
    private TableColumn<Activity, String> subjectsColumn;
    @FXML
    private TextField descriptionInput;
    @FXML
    private DatePicker startDateInput;
    @FXML
    private TextField startTimeInput;
    @FXML
    private TextField endTimeInput;
    @FXML
    private DatePicker endDateInput;
    @FXML
    private ListView<String> subjectSelector;

    public void initialize() {
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));
        subjectsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjects().stream()
            .map(Subject::getCode)
            .collect(Collectors.joining(", "))));
        
        ObservableList<String> subjectsList = FXCollections.observableArrayList();
        courses.getCourses().forEach((code, name) -> subjectsList.add(code + ": " + name));

        subjectSelector.setItems(subjectsList);
        subjectSelector.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        refreshActivitiesList();
    }

    public void setActivitiesList(List<Activity> activities) {
        if (activities != null && !activities.isEmpty()) {
            activitiesTableView.getItems().setAll(activities);
        }
    }

    public void refreshActivitiesList() {
        this.activities = this.restConsumer.getActivities();
        setActivitiesList(this.activities);
    }

    @FXML
    public void handleAddActivity() {
        try {
            String description = descriptionInput.getText();
            LocalDateTime startTime = LocalDateTime.of(startDateInput.getValue(), LocalTime.parse(startTimeInput.getText(), DateTimeFormatter.ofPattern("HH:mm")));
            LocalDateTime endTime = LocalDateTime.of(endDateInput.getValue(), LocalTime.parse(endTimeInput.getText(), DateTimeFormatter.ofPattern("HH:mm")));
            
            // Validate the input and create the activity
            if (description.isEmpty() || startTime.isAfter(endTime)) {
                // Handle invalid input
                showAlert("Invalid input", "Please check the activity details and try again.");
                return;
            }

            List<Subject> subjects = new ArrayList<Subject>();
            for (String subjectCode : subjectSelector.getSelectionModel().getSelectedItems()) {
                subjects.add(new Subject(subjectCode.split(":")[0], courses));
            }

            Activity newActivity = new Activity(subjects, startTime, endTime, description);
            
            // Send the activity to the server
            this.restConsumer.addActivity(newActivity);
            refreshActivitiesList();

            clearInputs();
        } catch (DateTimeParseException e) {
            showAlert("Invalid Time Format", "Please enter the time in HH:mm format.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private void clearInputs() {
        descriptionInput.clear();
        startDateInput.setValue(null);
        startTimeInput.clear();
        endDateInput.setValue(null);
        endTimeInput.clear();
        subjectSelector.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setRestConsumer(RestConsumer restConsumer) {
        this.restConsumer = restConsumer;
    }

}