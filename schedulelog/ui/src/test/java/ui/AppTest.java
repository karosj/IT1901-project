package ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.*;
import static org.testfx.matcher.control.LabeledMatchers.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import schedulelog.core.Activity;
import schedulelog.core.Courses;
import schedulelog.core.Subject;
import schedulelog.json.RestConsumer;

/**
 * Test class for the App UI.
 *
 * This class contains UI tests for the application, focusing on user
 * interactions
 * and UI behaviors. It includes tests for various scenarios like adding
 * activities, handling
 * errors, and checking initial states.
 */
public class AppTest extends ApplicationTest {

    private AppController controller;
    private App app;
    private Parent root;
    private RestConsumer mockRestConsumer;

    private List<Activity> mockActivities = Arrays.asList(
            new Activity(Collections.singletonList(new Subject("TDT4120", new Courses())),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(1),
                    "Study AppTest session"));

    // Sets up the test environment including the mock RestConsumer and loading the
    // FXML.
    @Override
    public void start(Stage stage) throws IOException {
        // Create and configure the mock RestConsumer
        mockRestConsumer = mock(RestConsumer.class);
        when(mockRestConsumer.getActivities()).thenReturn(mockActivities);
        when(mockRestConsumer.addActivity(any(Activity.class))).thenReturn("Mocked server response");

        // Load the FXML and set the controller
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("App.fxml"));
        root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        // Set the mocked RestConsumer in the controller
        controller.setRestConsumer(mockRestConsumer);

        // Now initialize the controller
        controller.initialize();

        // Set up the stage
        stage.setScene(new Scene(root));
        stage.show();
        app = new App();
    }

    public Parent getRootNode() {
        return root;
    }

    // Tests the initialization of the controller.
    @Test
    public void testControllerInitial() {
        assertNotNull(this.controller);
    }

    // Tests the initialization of the app.
    @Test
    public void testAppInitial() {
        assertNotNull(this.app);
    }

    // Tests the initial activities list in the TableView.
    @Test
    public void testInitialActivitiesList() {
        TableView<Activity> activitiesTableView = lookup("#activitiesTableView").query();
        assertNotNull(activitiesTableView);
        assert activitiesTableView.getItems().size() == 1;
    }

    // Tests the behavior of the UI when the server is not connected.
    @Test
    public void testNoConnectedServer() {
        System.out.println("testNoConnectedServer start.");

        // Mock the RestConsumer to return null for getActivities
        when(mockRestConsumer.getActivities()).thenReturn(null);

        Platform.runLater(() -> {
            // Initialize the controller with the mocked RestConsumer
            controller.setRestConsumer(mockRestConsumer);
            controller.initialize();
        });

        WaitForAsyncUtils.waitForFxEvents();

        // Assert that the activities list is empty or null
        verifyThat(".alert .content", hasText("An error occurred while retrieving the activities."));

        // Reset the mock to its original state
        when(mockRestConsumer.getActivities()).thenReturn(mockActivities);

    }

    // Tests the UI response to adding a new activity through a button click.
    @Test
    public void testAddActivityClick() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("New test Session");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Assuming you have a ListView for subjects with fx:id "subjectSelector"
        // Select a subject from the list
        clickOn("#subjectSelector");
        type(KeyCode.DOWN); // Navigate in the list
        type(KeyCode.ENTER); // Select an item

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        TableView<Activity> activitiesTableView = lookup("#activitiesTableView").query();
        ObservableList<Activity> activities = activitiesTableView.getItems();
        assertNotNull(activities);

        // Because the getActivites is mocked, the new activity will not be added to the
        // list
        // But the fact that no error is shown means that the activity was added
        // successfully

        // And that the inputs are cleared:
        assertEquals("", lookup("#descriptionInput").queryTextInputControl().getText());
    }

    // Tests the UI response when adding an activity with missing details.
    @Test
    public void testBadActivity() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        verifyThat(".alert .content", hasText("Please check the activity details and try again."));
        assertEquals("10:00", lookup("#startTimeInput").queryTextInputControl().getText());
    }

    // Tests the UI response when adding an activity with incorrect date inputs
    @Test
    public void testStrangeDates() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("Strange dates");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/25/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Assuming you have a ListView for subjects with fx:id "subjectSelector"
        // Select a subject from the list
        clickOn("#subjectSelector");
        type(KeyCode.DOWN); // Navigate in the list
        type(KeyCode.ENTER); // Select an item

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        verifyThat(".alert .content", hasText("Start time cannot be after end time."));
        assertEquals("Strange dates", lookup("#descriptionInput").queryTextInputControl().getText());
    }

    // Tests the UI response when adding an activity with wrong time format.
    @Test
    public void testWrongTimeFormat() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("Strange times");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("wrong");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("wrong");

        // Assuming you have a ListView for subjects with fx:id "subjectSelector"
        // Select a subject from the list
        clickOn("#subjectSelector");
        type(KeyCode.DOWN); // Navigate in the list
        type(KeyCode.ENTER); // Select an item

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        verifyThat(".alert .content", hasText("Please enter the time in HH:mm format."));
        assertEquals("Strange times", lookup("#descriptionInput").queryTextInputControl().getText());
    }

    // Tests the UI response when adding an activity without any subjects.
    @Test
    public void testNoSubjects() {
        // Assuming you have a button with fx:id "addActivityButton" in your FXML
        String addActivityButtonId = "#addActivityButton";

        // Simulate user input
        clickOn("#descriptionInput").write("No subjects");
        clickOn("#startDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#startTimeInput").write("10:00");
        clickOn("#endDateInput").write("10/27/2023");
        type(KeyCode.ENTER);
        clickOn("#endTimeInput").write("12:00");

        // Click the button to add the activity
        clickOn(addActivityButtonId);

        verifyThat(".alert .content", hasText("An error occurred: List of subjects cannot be null or empty."));
        assertEquals("No subjects", lookup("#descriptionInput").queryTextInputControl().getText());
    }

}
