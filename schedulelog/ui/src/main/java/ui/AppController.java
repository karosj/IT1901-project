package ui;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import java.time.format.DateTimeFormatter;

import schedulelog.core.Activity;
import schedulelog.core.Subject;
import schedulelog.core.Courses;

import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;


public class AppController {

    private Courses courses;

    public AppController() {
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

    @FXML
    private void handleAddActivity() {
        // TODO
    }

}