package ui;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import schedulelog.core.Activity;
import schedulelog.core.Courses;
import schedulelog.core.Subject;
import schedulelog.json.RestConsumer;

/**
 * Controller class for the application's UI.
 *
 * This class is responsible for handling user interactions in the JavaFX
 * application. It manages
 * the display and manipulation of Activity data, including the retrieval and
 * addition of activities
 * through a RESTful service. The controller initializes and populates JavaFX
 * components like
 * TableView, DatePicker, and ListView with data from the RestConsumer and
 * Courses services.
 */
public class AppController {

    private List<Activity> activities = new ArrayList<>();
    private RestConsumer restConsumer;
    private Courses courses;

    /**
     * Constructor.
     * Initializes the RestConsumer and fetches the initial list of activities. Also
     * initializes
     * the Courses instance used throughout the application.
     */
    public AppController() {
        this.restConsumer = new RestConsumer();
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

    /**
     * Initializes the controller. This method sets up the table columns and
     * loads activity data into the TableView. It also populates the list of
     * subjects
     * in the ListView for selection.
     */
    public void initialize() {
        descriptionColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        subjectsColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjects().stream()
                        .map(Subject::getCode)
                        .collect(Collectors.joining(", "))));

        ObservableList<String> subjectsList = FXCollections.observableArrayList();
        courses.getCourses().forEach((code, name) -> subjectsList.add(code + ": " + name));

        subjectSelector.setItems(subjectsList);
        subjectSelector.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Platform.runLater(this::refreshActivitiesList);
    }

    /**
     * Sets the RestConsumer instance for the AppController class.
     *
     * This method enables the AppController to use a specified RestConsumer for
     * HTTP communication
     * with a RESTful service. Useful in scenarios like dependency injection,
     * allowing the controller to use a different RestConsumer, such as a mock in a
     * testing environment,
     * or an instance with different configurations in production.
     *
     * @param restConsumer The RestConsumer instance to be used by this
     *                     AppController.
     */
    public void setRestConsumer(RestConsumer restConsumer) {
        this.restConsumer = restConsumer;
    }

    /**
     * Refreshes the list of activities displayed in the TableView.
     * Fetches the latest activities from the RestConsumer service.
     */
    public void setActivitiesList(List<Activity> activities) {
        if (activities != null) {
            activitiesTableView.getItems().setAll(activities);
        } else {
            showAlert("Error", "An error occurred while retrieving the activities.");
        }
    }

    /**
     * Refreshes the list of activities displayed in the TableView.
     * Fetches the latest activities from the RestConsumer service.
     */
    public void refreshActivitiesList() {
        this.activities = this.restConsumer.getActivities();
        setActivitiesList(this.activities);
    }

    /**
     * Handles the addition of a new activity.
     * Gathers input from the user, validates it, creates a new Activity object,
     * and sends it to the server via the RestConsumer. It also refreshes the
     * activities list
     * and clears the input fields after adding the activity.
     */
    @FXML
    public void handleAddActivity() {
        try {
            String description = descriptionInput.getText();
            LocalDateTime startTime = LocalDateTime.of(startDateInput.getValue(),
                    LocalTime.parse(startTimeInput.getText(), DateTimeFormatter.ofPattern("HH:mm")));
            LocalDateTime endTime = LocalDateTime.of(endDateInput.getValue(),
                    LocalTime.parse(endTimeInput.getText(), DateTimeFormatter.ofPattern("HH:mm")));

            // Validate the input and create the activity
            if (description.isEmpty()) {
                // Handle invalid input
                showAlert("Invalid input", "Please check the activity details and try again.");
                return;
            } else if (startTime.isAfter(endTime)) {
                // Handle invalid input
                showAlert("Invalid input", "Start time cannot be after end time.");
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

    /**
     * Clears all input fields in the UI.
     * This method resets all user input fields to their default state, preparing
     * the form for new input.
     */
    private void clearInputs() {
        descriptionInput.clear();
        startDateInput.setValue(null);
        startTimeInput.clear();
        endDateInput.setValue(null);
        endTimeInput.clear();
        subjectSelector.getSelectionModel().clearSelection();
    }

    /**
     * Displays an alert dialog to the user.
     *
     * @param title   The title of the alert dialog.
     * @param content The content message to be displayed in the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}