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

    private List<Activity> activities;
    private RestConsumer restConsumer;
    private Courses courses;

    public AppController() {
        this.restConsumer = new RestConsumer();
        this.activities = this.restConsumer.getActivities();
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

        if (activities != null) {
            activitiesTableView.getItems().setAll(activities);
        } else {
            // Handle the case where activities is null, e.g., clear the table or show a message
            activitiesTableView.getItems().clear();
        }
    }

    public void setActivitiesList(List<Activity> activities) {
        activitiesTableView.getItems().setAll(activities);
    }

    public void refreshActivitiesList() {
        this.activities = this.restConsumer.getActivities();
        setActivitiesList(this.activities);
    }

    @FXML
    private void handleAddActivity() {
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

    private void handleConnectionError(Exception e) {
        System.err.println("Error connecting to backend: " + e.getMessage());
        // You might want to show an alert to the user or take other appropriate actions
        showAlert("Connection Error", "Unable to connect to the backend. Please check your network connection and try again.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}